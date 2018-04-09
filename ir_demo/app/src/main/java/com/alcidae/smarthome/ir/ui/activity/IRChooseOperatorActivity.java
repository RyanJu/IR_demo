package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.IRUtils;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.data.IRConst;
import com.alcidae.smarthome.ir.ui.activity.match.IRMatchBaseActivity;
import com.alcidae.smarthome.ir.ui.dialog.ChooseAreaDialog;
import com.alcidae.smarthome.ir.util.LocationUtil;
import com.alcidae.smarthome.ir.util.SimpeIRequestResult;
import com.alcidae.smarthome.ir.util.SimpleOnItemClickListener;
import com.alcidae.smarthome.ir.util.ToastUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.SpList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/29 16:30 1.0
 * @time 2018/3/29 16:30
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description choose stb operator,note that stb device is based on operator
 * @updateVersion 1.0
 * @updateTime 2018/3/29 16:30
 */

public class IRChooseOperatorActivity extends Activity {

    private int mDeviceType;
    private RecyclerView mOperatorsRv;
    private List<SpList.Sp> mOperators;
    private int mAreaId;
    private SpList.Sp mOperator;
    private TextView mLocationTv;


    public static void launch(Activity from, int deviceType, int requestCode) {
        Intent intent = new Intent(from, IRChooseOperatorActivity.class);
        intent.putExtra("deviceType", deviceType);
        from.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_ir_choose_operator);
        EventBus.getDefault().register(this);
        initView();
        initData();
        loadCurrentLocation();
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

    private void loadOperators() {
        if (!checkArea()) {
            ToastUtil.toast(this, R.string.ir_error_area);
            return;
        }
        KookongSDK.getAreaId(IRUtils.getProvince(), IRUtils.getCity(), IRUtils.getArea(),new SimpeIRequestResult<Integer>(this){

            @Override
            public void onSuccess(String s, Integer integer) {
                LogUtil.i("getAreaId " + s + " ," + integer);
                mAreaId = integer;
                KookongSDK.getOperaters(mAreaId, new SimpeIRequestResult<SpList>(IRChooseOperatorActivity.this) {
                    @Override
                    public void onSuccess(String s, SpList spList) {
                        if (spList != null && spList.spList != null) {
                            mOperators = spList.spList;
                            refreshView();
                        } else {
                            mOperators = new ArrayList<>();
                            refreshView();
                        }
                    }

                });
            }
        });

    }

    private void refreshView() {
        OperatorAdapter adapter = new OperatorAdapter(this, mOperators);
        mOperatorsRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new SimpleOnItemClickListener<SpList.Sp>() {
            @Override
            public void onClickItem(RecyclerView.Adapter adapter, int position, SpList.Sp data) {
                if (data.type == IRConst.IPTV) {
                    IRChooseIPTVBrandActivity.launch(IRChooseOperatorActivity.this, 919, mDeviceType, mAreaId, data);
                } else {
                    IRMatchBaseActivity.launchByStb(IRChooseOperatorActivity.this, 666, mDeviceType, mAreaId, data);
                }
            }
        });
    }

    private boolean checkArea() {
        return !(TextUtils.isEmpty(IRUtils.getProvince()))
                && !(TextUtils.isEmpty(IRUtils.getCity()))
                ;
    }

    private void initData() {
        mDeviceType = getIntent().getIntExtra("deviceType", -1);
    }

    private void initView() {
        findViewById(R.id.id_ir_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOperatorsRv = findViewById(R.id.id_dialog_choose_operator_rv);
        mOperatorsRv.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.id_activity_ir_choose_operators_search_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search stb
                IRSearchStbActivity.launch(IRChooseOperatorActivity.this, mDeviceType, mAreaId);
            }
        });

        mLocationTv = findViewById(R.id.id_activity_ir_choose_operators_location_tv);
        findViewById(R.id.id_activity_ir_choose_operators_set_location_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAreaDialog dialog = new ChooseAreaDialog(IRChooseOperatorActivity.this);
                dialog.show();
                dialog.setProvinceAndCity(IRUtils.getProvince(), IRUtils.getCity());
                dialog.setOnAreaSelectListener(new ChooseAreaDialog.OnAreaSelectListener() {
                    @Override
                    public void onSelecteArea(String province, String city) {
                        IRUtils.setArea(province, city, city);
                        updateLocation();
                        loadOperators();
                    }
                });
            }
        });

    }

    private void loadCurrentLocation() {

        LocationUtil.getLocation(this, new LocationUtil.OnLocationListener() {
            @Override
            public void onLocationGet(double latitude, double longitude, String countryCode, String country, String province, String city, String district) {
                IRUtils.setArea(province, city, district);
                updateLocation();
                loadOperators();
            }
        });
    }


    private void updateLocation() {
        mLocationTv.setText(IRUtils.getProvince() + "" + IRUtils.getCity());
    }


    private static class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.Holder> {

        private Context mContext;
        private List<SpList.Sp> mItems;
        private SimpleOnItemClickListener<SpList.Sp> onItemClickListener;

        public OperatorAdapter(Context mContext, List<SpList.Sp> mItems) {
            this.mContext = mContext;
            this.mItems = mItems;
        }

        @Override
        public OperatorAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            return new OperatorAdapter.Holder(inflater.inflate(R.layout.adapter_ir_operator, parent, false));
        }

        @Override
        public void onBindViewHolder(OperatorAdapter.Holder holder, int position) {
            SpList.Sp itemBean = mItems.get(position);
            holder.mTextTv.setText(itemBean.spName);
        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public SimpleOnItemClickListener<SpList.Sp> getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(SimpleOnItemClickListener<SpList.Sp> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        class Holder extends RecyclerView.ViewHolder {

            private final TextView mTextTv;

            public Holder(View itemView) {
                super(itemView);
                mTextTv = itemView.findViewById(R.id.id_adapter_ir_brand_item_tv);
                mTextTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (onItemClickListener != null) {
                            SpList.Sp itemBean = mItems.get(position);
                            onItemClickListener.onClickItem(OperatorAdapter.this, position, itemBean);
                        }
                    }
                });
            }
        }


    }
}
