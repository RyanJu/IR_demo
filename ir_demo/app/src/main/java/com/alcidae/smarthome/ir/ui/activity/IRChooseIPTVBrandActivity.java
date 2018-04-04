package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.ui.activity.match.IRMatchBaseActivity;
import com.alcidae.smarthome.ir.util.SimpleOnItemClickListener;
import com.alcidae.smarthome.ir.util.ToastUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.SpList;
import com.kookong.app.data.StbList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/2 18:07 1.0
 * @time 2018/4/2 18:07
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/4/2 18:07
 */

public class IRChooseIPTVBrandActivity extends Activity implements View.OnClickListener, SimpleOnItemClickListener<StbList.Stb> {

    private RecyclerView mBrandRv;
    private EditText mSearchEt;
    private int mDeviceType;
    private int mAreaId;
    private SpList.Sp mSp;

    private List<ItemBean> mItems;

    public static void launch(Activity from, int requestCode, int deviceType, int areaId, SpList.Sp sp) {
        Intent intent = new Intent(from, IRChooseIPTVBrandActivity.class);
        intent.putExtra("deviceType", deviceType);
        intent.putExtra("areaId", areaId);
        intent.putExtra("sp", sp);
        from.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_choose_iptv_brand);
        initViews();
        initData();
        loadAndRefresh();
        EventBus.getDefault().register(this);
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

    private void loadAndRefresh() {
        KookongSDK.getIPTV(mSp.spId, new IRequestResult<StbList>() {
            @Override
            public void onSuccess(String s, StbList stbList) {
                mItems = new ArrayList<>();
                addBrands(6, stbList.stbList);
                BrandAdapter brandAdapter = new BrandAdapter(IRChooseIPTVBrandActivity.this, mItems);
                mBrandRv.setAdapter(brandAdapter);
                brandAdapter.setOnItemClickListener(IRChooseIPTVBrandActivity.this);
            }

            @Override
            public void onFail(Integer integer, String s) {
                ToastUtil.toast(IRChooseIPTVBrandActivity.this, R.string.ir_error_network);
            }
        });
    }

    private void addBrands(int hotCount, List<StbList.Stb> brandList) {
        if (mItems == null || brandList == null)
            return;

        addCommonBrands(hotCount, brandList);

        Collections.sort(brandList, new Comparator<StbList.Stb>() {
            @Override
            public int compare(StbList.Stb o1, StbList.Stb o2) {
                String s1 = o1.bname == null ? "" : o1.bname;
                String s2 = o2.bname == null ? "" : o2.bname;
                return s1.compareToIgnoreCase(s2);
            }
        });

        ItemBean title = null;
        for (StbList.Stb brand : brandList) {
            if (title == null || !brand.bname.startsWith(title.title)) {
                title = new ItemBean(true, String.valueOf(brand.bname.charAt(0)));
                mItems.add(title);
            }
            mItems.add(new ItemBean(brand));
        }
    }


    private void addCommonBrands(int hotCount, List<StbList.Stb> stbs) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }


        mItems.add(new ItemBean(true, getResources().getString(R.string.ir_common_brands)));

        hotCount = hotCount > stbs.size() ? stbs.size() : hotCount;

        for (int i = 0; i < hotCount; i++) {
            mItems.add(new ItemBean(stbs.get(i)));
        }

    }

    private void initData() {
        Intent intent = getIntent();
        mDeviceType = intent.getIntExtra("deviceType", -1);
        mAreaId = intent.getIntExtra("areaId", -1);
        mSp = (SpList.Sp) intent.getSerializableExtra("sp");
    }


    private void initViews() {
        findViewById(R.id.id_dialog_choose_brand_back_iv)
                .setOnClickListener(this);
        this.mBrandRv = findViewById(R.id.id_dialog_choose_brand_rv);
        this.mSearchEt = findViewById(R.id.id_dialog_choose_brand_search_tv);
        mBrandRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_choose_brand_back_iv:
                finish();
                break;

        }
    }

    //click brand item
    @Override
    public void onClickItem(RecyclerView.Adapter adapter, int position, StbList.Stb data) {
        IRMatchBaseActivity.launchByIPTV(this, 8818, mDeviceType, mAreaId, mSp, data);
    }

    private class ItemBean {
        boolean isTitle;
        String title;
        StbList.Stb stb;

        public ItemBean(boolean isTitle, String title) {
            this.isTitle = isTitle;
            this.title = title;
        }

        public ItemBean(StbList.Stb stb) {
            this.stb = stb;
        }
    }


    private static class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.Holder> {

        private static final int VIEW_TITLE = 0;
        private static final int VIEW_BRAND = 1;
        private Context mContext;
        private List<ItemBean> mItems;
        private SimpleOnItemClickListener<StbList.Stb> onItemClickListener;

        public BrandAdapter(Context mContext, List<ItemBean> mItems) {
            this.mContext = mContext;
            this.mItems = mItems;
        }

        @Override
        public BrandAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (viewType == VIEW_TITLE) {
                return new BrandAdapter.TitleHolder(inflater.inflate(R.layout.adapter_ir_brand_title, parent, false));
            }
            return new BrandAdapter.BrandHolder(inflater.inflate(R.layout.adapter_ir_brand, parent, false));
        }

        @Override
        public void onBindViewHolder(BrandAdapter.Holder holder, int position) {
            ItemBean itemBean = mItems.get(position);
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TITLE) {
                BrandAdapter.TitleHolder th = (BrandAdapter.TitleHolder) holder;
                th.mTextTv.setText(itemBean.title);
            } else {
                BrandAdapter.BrandHolder bh = (BrandAdapter.BrandHolder) holder;
                bh.mTextTv.setText(itemBean.stb.bname);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mItems.get(position).isTitle ? VIEW_TITLE : VIEW_BRAND;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public SimpleOnItemClickListener<StbList.Stb> getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(SimpleOnItemClickListener<StbList.Stb> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        class Holder extends RecyclerView.ViewHolder {

            public Holder(View itemView) {
                super(itemView);
            }
        }

        class TitleHolder extends BrandAdapter.Holder {

            private final TextView mTextTv;

            public TitleHolder(View itemView) {
                super(itemView);
                mTextTv = itemView.findViewById(R.id.id_adapter_ir_brand_item_tv);
            }
        }

        class BrandHolder extends BrandAdapter.Holder {
            private final TextView mTextTv;

            public BrandHolder(View itemView) {
                super(itemView);
                mTextTv = itemView.findViewById(R.id.id_adapter_ir_brand_item_tv);
                mTextTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (onItemClickListener != null) {
                            ItemBean itemBean = mItems.get(position);
                            if (itemBean.isTitle) {
                                return;
                            }
                            onItemClickListener.onClickItem(BrandAdapter.this, position, itemBean.stb);
                        }
                    }
                });
            }
        }
    }
}
