package com.czarzap.cobromovil.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.czarzap.cobromovil.R;
import com.czarzap.cobromovil.main.HeatSensitiveActivity;
import com.czarzap.cobromovil.main.RTApplication;

import driver.Contants;

public class BaseActivity extends AppCompatActivity {
    int onStartCount = 0;

    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (RTApplication.getConnState() != Contants.UNCONNECTED) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_on));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_check) {
            Intent intent = new Intent(this, HeatSensitiveActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
