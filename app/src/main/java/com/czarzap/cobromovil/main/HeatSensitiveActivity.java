package com.czarzap.cobromovil.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.czarzap.cobromovil.receiver.UsbDeviceReceiver;
import com.czarzap.cobromovil.rtprinter.R;
import com.czarzap.cobromovil.dialog.BluetoothDeviceChooseDialog;
import com.czarzap.cobromovil.dialog.UsbDeviceChooseDialog;
import com.czarzap.cobromovil.menu.MenuActivity;
import com.czarzap.cobromovil.observable.ConnResultObservable;
import com.czarzap.cobromovil.observable.ConnStateObservable;
import com.czarzap.cobromovil.utils.LogUtils;
import com.czarzap.cobromovil.utils.ToastUtil;

import java.util.Observable;
import java.util.Observer;

import driver.Contants;
import driver.HsBluetoothPrintDriver;
import driver.HsUsbPrintDriver;

public class HeatSensitiveActivity extends Activity implements View.OnClickListener, Observer {

    private final String TAG = getClass().getSimpleName();

    private static final int BLUETOOTH_MODE = 0x01;
    private static final int USB_MODE = 0x02;
    private static final int WIFI_MODE = 0x03;
    private static final int REQUEST_ENABLE_BT = 0xf0;

    private int currentMode = 1;
    private boolean mUsbRegistered = false;//表示UsbDeviceReceiver是否已被注册
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private UsbDeviceReceiver mUsbReceiver;
    private UsbManager mUsbManager;
    private UsbDevice mUsbDevice;
    private IntentFilter usbIntentFilter;

    private TextView llConnectSpinner;//蓝牙，usb，wifi设置
    private TextView tvConnectState;
    private RadioGroup rgConnectMode;
    int onStartCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.acitivty_heat_sensitive);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        ConnResultObservable.getInstance().addObserver(this);
        RTApplication.addActivity(this);
        initView();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }

    private void initView() {
//        tvConnectState = (TextView) this.findViewById(R.id.connect_state);
        llConnectSpinner = (TextView) this.findViewById(R.id.heat_sensitive_setting_connect_spinner);
        llConnectSpinner.setHint(R.string.choose_bluetooth_device);
        rgConnectMode = (RadioGroup) this.findViewById(R.id.rg_heat_sensitive_setting_connect_mode);
        setListener();
    }
    private void disconnect() {
        switch (RTApplication.getConnState()) {
            case Contants.CONNECTED_BY_BLUETOOTH:
                HsBluetoothPrintDriver.getInstance().stop();
                break;
            case Contants.CONNECTED_BY_USB:
                HsUsbPrintDriver.getInstance().stop();
                break;
            case Contants.CONNECTED_BY_WIFI:
//                HsWifiPrintDriver.getInstance().stop();
                break;
        }

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_heat_sensitive_connect:
                if (RTApplication.getConnState() != Contants.UNCONNECTED) {
                    disconnect();
                    connect();
                }else{
                    connect();
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");
        ConnResultObservable.getInstance().deleteObserver(this);
        if (mUsbRegistered) {
            unregisterReceiver(mUsbReceiver);
            mUsbRegistered = false;
        }
        RTApplication.removeActivity(this);
    }


    private void setListener() {

        rgConnectMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_heat_sensitive_setting_bluetooth:
                        currentMode = BLUETOOTH_MODE;
                        notifyModeChanged();
                        break;
                    case R.id.rb_heat_sensitive_setting_usb:
                        currentMode = USB_MODE;
                        notifyModeChanged();
                        break;

                }
            }
        });
        llConnectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (currentMode) {

                    case BLUETOOTH_MODE:
                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter == null) {
                            ToastUtil.show(mContext, R.string.device_does_not_support_bluetooth);
                            return;
                        }
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            return;
                        }
                        showBluetoothDeviceChooseDialog();
                        break;

                    case USB_MODE:
                        showUSBDeviceChooseDialog();
                        break;


                }


            }
        });

    }

    private void showBluetoothDeviceChooseDialog() {
        BluetoothDeviceChooseDialog bluetoothDeviceChooseDialog = new BluetoothDeviceChooseDialog();
        bluetoothDeviceChooseDialog.setOnDeviceItemClickListener(new BluetoothDeviceChooseDialog.onDeviceItemClickListener() {
            @Override
            public void onDeviceItemClick(BluetoothDevice device) {
                if (TextUtils.isEmpty(device.getName())) {
                    llConnectSpinner.setText(device.getAddress());
                } else {
                    llConnectSpinner.setText(device.getName());
                }
//                LogUtils.d(TAG,"device.getType() = " + device.getType());
                LogUtils.d(TAG, "device.getBluetoothClass() = " + device.getBluetoothClass());
                mBluetoothDevice = device;
            }
        });
        bluetoothDeviceChooseDialog.show(getFragmentManager(), null);
    }

    private void showUSBDeviceChooseDialog() {

        if (!mUsbRegistered) {
            registerUsbReceiver();
            mUsbRegistered = true;
        }
        final UsbDeviceChooseDialog usbDeviceChooseDialog = new UsbDeviceChooseDialog();
        usbDeviceChooseDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUsbDevice = (UsbDevice) parent.getAdapter().getItem(position);
                llConnectSpinner.setText(getString(R.string.print_device) + (position + 1));
                usbDeviceChooseDialog.dismiss();
            }
        });
        usbDeviceChooseDialog.show(getFragmentManager(), null);
    }

    private void registerUsbReceiver() {
        if (mUsbReceiver == null) {
            mUsbReceiver = new UsbDeviceReceiver(new UsbDeviceReceiver.CallBack() {
                @Override
                public void onPermissionGranted(UsbDevice usbDevice) {
                    connectUsb(usbDevice);
                }

                @Override
                public void onDeviceAttached(UsbDevice usbDevice) {

                }

                @Override
                public void onDeviceDetached(UsbDevice usbDevice) {
                    HsUsbPrintDriver.getInstance().stop();
                    mUsbDevice = null;
                    llConnectSpinner.setText(null);
                }
            });
        }
        if (usbIntentFilter == null) {
            usbIntentFilter = new IntentFilter();
            usbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            usbIntentFilter.addAction(UsbDeviceReceiver.ACTION_USB_PERMISSION);
        }
        registerReceiver(mUsbReceiver, usbIntentFilter);
        mUsbRegistered = true;
    }

//    private void showWifiDeviceChooseDialog() {
//
//        WifiDeviceChooseDialog wifiDeviceChooseDialog = new WifiDeviceChooseDialog();
//        Bundle args = new Bundle();
//        args.putString(WifiDeviceChooseDialog.BUNDLE_KEY_ADDRESS, llConnectSpinner.getText().toString());
//        wifiDeviceChooseDialog.setArguments(args);
//        wifiDeviceChooseDialog.setOnClickListener(new CustomDialogInterface.onPositiveClickListener() {
//            @Override
//            public void onDialogPositiveClick(String text) {
//                llConnectSpinner.setText(text);
//            }
//        });
//        wifiDeviceChooseDialog.show(getFragmentManager(), null);
//    }

    private void notifyModeChanged() {
        switch (currentMode) {
            case BLUETOOTH_MODE:
                llConnectSpinner.setHint(R.string.choose_bluetooth_device);
                break;
            case USB_MODE:
                llConnectSpinner.setHint(R.string.choose_usb_device);
                break;
            case WIFI_MODE:
//                llConnectSpinner.setHint(R.string.choose_wifi_device);
                break;
        }
    }

    private void connect() {
        switch (currentMode) {
            case BLUETOOTH_MODE:
                if (TextUtils.isEmpty(llConnectSpinner.getText().toString())) {
                    ToastUtil.show(mContext, R.string.tip_choose_bluetooth_device);
                    return;
                }
                connectBluetooth();
                break;
            case USB_MODE:
                if (TextUtils.isEmpty(llConnectSpinner.getText().toString())) {
                    ToastUtil.show(mContext, R.string.tip_choose_usb_device);
                    return;
                }
                if (mUsbManager == null) {
                    mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
                }
                if (mUsbManager.hasPermission(mUsbDevice)) {
                    connectUsb(mUsbDevice);
                } else {
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(mUsbReceiver.ACTION_USB_PERMISSION), 0);
                    mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
                }
                break;
            case WIFI_MODE:
//                if (TextUtils.isEmpty(llConnectSpinner.getText().toString())) {
//                    ToastUtil.show(mContext, R.string.tip_choose_wifi_device);
//                    return;
//                }
//                connectWifi();
                break;
        }
    }

    private void connectBluetooth() {
        HsBluetoothPrintDriver hsBluetoothPrintDriver = HsBluetoothPrintDriver.getInstance();
        hsBluetoothPrintDriver.start();
        LogUtils.d(TAG, "mBluetoothDevice getAddress = " + mBluetoothDevice.getAddress());
        hsBluetoothPrintDriver.connect(mBluetoothDevice);
    }

    private void connectUsb(UsbDevice usbDevice) {
        HsUsbPrintDriver hsUsbPrintDriver = HsUsbPrintDriver.getInstance();
        hsUsbPrintDriver.connect(usbDevice);
    }

//    private void connectWifi() {
//        final String[] address = llConnectSpinner.getText().toString().trim().split(":");
//        //连接wifi
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HsWifiPrintDriver hsWifiPrintDriver = HsWifiPrintDriver.getInstance();
//                //ip-----address[0],port----address[1]
//                hsWifiPrintDriver.WIFISocket(address[0], Integer.valueOf(address[1]));
//            }
//        }).start();
//        //pingIp
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HsWifiPrintDriver hsWifiPrintDriver = HsWifiPrintDriver.getInstance();
//                boolean isNoCon = true;
//                do {
//                    isNoCon = hsWifiPrintDriver.IsNoConnection(address[0]);
//                    Log.d("ping的地址", address[0]);
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("ping的结果", isNoCon + "");
//                } while (!isNoCon);
//                if (hsWifiPrintDriver.mysocket != null) {
//                    try {
//                        hsWifiPrintDriver.mysocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    hsWifiPrintDriver.mysocket = null;
//                }
//            }
//        }).start();
//    }


    @Override
    public void update(Observable observable, final Object data) {
        if (RTApplication.mode != RTApplication.MODE_HS) {
            return;
        }
        if (observable == ConnStateObservable.getInstance()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvConnectState.setText((CharSequence) data);
                }
            });
        } else if (observable == ConnResultObservable.getInstance()) {
            switch ((int) data) {
                case Contants.FLAG_FAIL_CONNECT:
                    ToastUtil.show(mContext, getString(R.string.fail_connect));
                    break;
                case Contants.FLAG_SUCCESS_CONNECT:
                    ToastUtil.show(mContext, getString(R.string.success_connect));
                    Intent menuIntent = new Intent(HeatSensitiveActivity.this, MenuActivity.class);
                    HeatSensitiveActivity.this.startActivity(menuIntent);
                    break;
            }
        }
    }
}
