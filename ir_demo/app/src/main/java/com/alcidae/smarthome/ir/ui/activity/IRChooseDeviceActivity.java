package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.util.DisplayUtil;
import com.alcidae.smarthome.ir.util.SimpleItemDecoration;
import com.alcidae.smarthome.ir.util.SimpleOnItemClickListener;
import com.hzy.tvmao.ir.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/26 14:13 1.0
 * @time 2018/3/26 14:13
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 14:13
 */

public class IRChooseDeviceActivity extends Activity implements View.OnClickListener {


    private static final int REQUEST_CODE_CHOOSE_BRAND = 1001;
    private static final int REQUEST_CODE_CHOOSE_OPRATOR = 1002;
    private RecyclerView mTypesRv;

    private List<ItemBean> mDeviceItems;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, IRChooseDeviceActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_ir_choose_control);
        EventBus.getDefault().register(this);
        initViews();
        initData();
        fillInViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMatchSuccess eventMatchSuccess) {
        finish();
    }

    private void fillInViews() {
        ChooseItemAdapter adapter = new ChooseItemAdapter(mDeviceItems, this);
        mTypesRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new SimpleOnItemClickListener<ItemBean>() {
            @Override
            public void onClickItem(RecyclerView.Adapter adapter, int position, ItemBean data) {

                if (data.deviceType == Device.STB) {
                    //机顶盒按运营商来区分
                    IRChooseOperatorActivity.launch(IRChooseDeviceActivity.this, data.deviceType, REQUEST_CODE_CHOOSE_OPRATOR);

                } else {
                    IRChooseBrandActivity.launch(IRChooseDeviceActivity.this,
                            data.deviceType, REQUEST_CODE_CHOOSE_BRAND);
                }
            }
        });
    }

    private void initData() {
        mDeviceItems = new ArrayList<>();

        mDeviceItems.add(new ItemBean(Device.AC, R.drawable.ir_ac, R.string.ir_ac));
        mDeviceItems.add(new ItemBean(Device.TV, R.drawable.ir_tv, R.string.ir_tv));
        mDeviceItems.add(new ItemBean(Device.STB, R.drawable.ir_stb, R.string.ir_stb));
        mDeviceItems.add(new ItemBean(Device.BOX, R.drawable.ir_net_box_1, R.string.ir_net_box));
        mDeviceItems.add(new ItemBean(Device.DVD, R.drawable.ir_dvd, R.string.ir_dvd));
        mDeviceItems.add(new ItemBean(Device.PA, R.drawable.ir_amplifer, R.string.ir_amplifer));
        mDeviceItems.add(new ItemBean(Device.PRO, R.drawable.ir_projector, R.string.ir_projector));
        mDeviceItems.add(new ItemBean(Device.FAN, R.drawable.ir_fan_1, R.string.ir_fan));
        mDeviceItems.add(new ItemBean(Device.LIGHT, R.drawable.ir_bumlb, R.string.ir_bulb));
        mDeviceItems.add(new ItemBean(Device.AIR_CLEANER, R.drawable.ir_air_clean, R.string.ir_air_clean));

    }

    private void initViews() {
        findViewById(R.id.id_dialog_choose_control_back_iv)
                .setOnClickListener(this);
        this.mTypesRv = findViewById(R.id.id_dialog_choose_control_rv);

        mTypesRv.addItemDecoration(new SimpleItemDecoration(DisplayUtil.dip2px(this, 15), DisplayUtil.dip2px(this, 15)));
        mTypesRv.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_choose_control_back_iv:
                finish();
                break;

        }

    }


    private static class ChooseItemAdapter extends RecyclerView.Adapter<ChooseItemAdapter.Holder> {
        private List<ItemBean> mItems;
        private Context mContext;
        private SimpleOnItemClickListener<ItemBean> onItemClickListener;

        public ChooseItemAdapter(List<ItemBean> mItems, Context mContext) {
            this.mItems = mItems;
            this.mContext = mContext;
        }

        @Override
        public ChooseItemAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChooseItemAdapter.Holder(LayoutInflater.from(mContext)
                    .inflate(R.layout.adapter_ir_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ChooseItemAdapter.Holder holder, int position) {
            ItemBean bean = mItems.get(position);
            holder.mIconIv.setImageResource(bean.iconResource);
            holder.mNameTv.setText(bean.textResource);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            private final ImageView mIconIv;
            private final TextView mNameTv;

            public Holder(View itemView) {
                super(itemView);
                this.mIconIv = itemView.findViewById(R.id.id_adapter_ir_item_iv);
                this.mNameTv = itemView.findViewById(R.id.id_adapter_ir_item_tv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (onItemClickListener != null) {
                            onItemClickListener.onClickItem(ChooseItemAdapter.this, position,
                                    mItems.get(position));
                        }
                    }
                });
            }
        }

        public SimpleOnItemClickListener<ItemBean> getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(SimpleOnItemClickListener<ItemBean> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    private static class ItemBean {
        int deviceType;
        int iconResource;
        int textResource;

        public ItemBean(int deviceType, int iconResource, int textResource) {
            this.deviceType = deviceType;
            this.iconResource = iconResource;
            this.textResource = textResource;
        }
    }
}
