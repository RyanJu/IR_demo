package com.alcidae.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.ui.activity.IRChooseDeviceActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IRUtils.init(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //click add remote controller button
    public void onClick(View view) {
        IRChooseDeviceActivity.launch(this);
    }

}
