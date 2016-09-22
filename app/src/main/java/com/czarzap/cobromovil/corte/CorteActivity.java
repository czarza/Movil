package com.czarzap.cobromovil.corte;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.czarzap.cobromovil.rtprinter.R;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CorteActivity extends AppCompatActivity implements View.OnClickListener, OnDateSetListener {
    TimePickerDialog mDialogAll;
    TimePickerDialog mDialogYearMonthDay;
    TextView mTvTime_ini,mTvTime_fin;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corte);
        initView();
        long tenYears = 2L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("Cancelar")
                .setSureStringId("Ok")
                .setTitleStringId("Seleccionar Fecha y Hora")
                .setYearText(" A")
                .setMonthText(" M")
                .setDayText(" D")
                .setHourText(" H")
                .setMinuteText(" Min")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis()- tenYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
    }

    void initView() {
        findViewById(R.id.btn_ini).setOnClickListener(this);
        findViewById(R.id.btn_fin).setOnClickListener(this);
        findViewById(R.id.bGenerar).setOnClickListener(this);

        mTvTime_ini = (TextView) findViewById(R.id.tv_time_ini);
        mTvTime_fin = (TextView) findViewById(R.id.tv_time_fin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ini:
                mDialogAll.show(getSupportFragmentManager(), "I");
                break;
            case R.id.btn_fin:
                mDialogAll.show(getSupportFragmentManager(), "F");
                break;
            case R.id.bGenerar:
                if(required(mTvTime_ini)&&required(mTvTime_fin)){
                    String inicio = mTvTime_ini.getText().toString();
                    String fin = mTvTime_fin.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("I",inicio);
                    bundle.putString("F",fin);
                    Intent intent = new Intent(CorteActivity.this, ReporteActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    public Boolean required(TextView editText) {
        String str = editText.getText().toString();
        if (str.equalsIgnoreCase("")) {
            editText.setError("Este campo es requerido");
            return false;
        }
        return true;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerDialog, long millseconds) {
        String text = getDateToString(millseconds);
        if(timePickerDialog.getTag().equals("I")) mTvTime_ini.setText(text);
        else  mTvTime_fin.setText(text);
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

}
