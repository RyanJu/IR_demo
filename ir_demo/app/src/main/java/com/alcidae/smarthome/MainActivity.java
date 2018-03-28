package com.alcidae.smarthome;

import android.Manifest;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.data.EventSendIR;
import com.alcidae.smarthome.ir.data.db.IRBean;
import com.alcidae.smarthome.ir.ui.activity.IRChooseDeviceActivity;
import com.alcidae.smarthome.ir.ui.dialog.AcDialog;
import com.hzy.tvmao.utils.LogUtil;

import net.tsz.afinal.FinalDb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试界面，需要对接的地方写了注释
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private List<IRBean> mBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();
        EventBus.getDefault().register(this);
        IRUtils.init(this);
        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.main_listview);
        mListView.setAdapter(new ArrayAdapter<IRBean>(this, android.R.layout.simple_list_item_1, mBeans));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //对接实现，首页点击保存的遥控图标，显示遥控界面
                new AcDialog(MainActivity.this, mBeans.get(position)).show();
            }
        });
        loadFromDb();
    }

    private void loadFromDb() {
        List<IRBean> all = FinalDb.create(this)
                .findAll(IRBean.class);
        mBeans.addAll(all);
        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }


    /**
     * 需要存储权限,transmit权限不需要
     */
    private void askPermissions() {
        String p1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        String p2 = Manifest.permission.TRANSMIT_IR;
        int i = PermissionChecker.checkSelfPermission(this, p1);
        if (i != PermissionChecker.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{p1}, 1001);
            }
        }

        if (PermissionChecker.checkSelfPermission(this, p2) != PermissionChecker.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{p2}, 1002);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 对接实现
     * 发送红外码，这里应该是发送给何光远
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventSendIR event) {
        //you should send this ir data
        LogUtil.i("send ir " + event);
        ConsumerIrManager manager = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.transmit(event.getFrequency(), event.getIrDataArray());
        }
    }


    /**
     * 对接实现
     * 当匹配好遥控时，通知界面变化，在首页显示该图标供用户使用
     *
     * @param eventMatchSuccess
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMatchSuccess eventMatchSuccess) {
        mBeans.add(eventMatchSuccess.getIrBean());
        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }


    //click add remote controller button
    public void onClick(View view) {
        //对接实现，当点击添加遥控时
        IRChooseDeviceActivity.launch(this);
    }

}
