package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.data.EventMatchSuccess;
import com.alcidae.smarthome.ir.ui.activity.match.IRMatchBaseActivity;
import com.alcidae.smarthome.ir.util.SimpleOnItemClickListener;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandHuaWeiList;
import com.kookong.app.data.BrandList;
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
 * @version 2018/3/26 14:21 1.0
 * @time 2018/3/26 14:21
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description
 * @updateVersion 1.0
 * @updateTime 2018/3/26 14:21
 */

public class IRChooseBrandActivity extends Activity implements View.OnClickListener {
    private int mDeviceType;
    private RecyclerView mBrandRv;
    private EditText mSearchEt;

    private List<ItemBean> mItems;

    public static void launch(Activity activity, int deviceType, int requestCode) {
        Intent intent = new Intent(activity, IRChooseBrandActivity.class);
        intent.putExtra("deviceType", deviceType);
        activity.startActivityForResult(intent, requestCode);
    }

//    public static void launchByChooseIPTVBrand(Activity activity, StbList stbList)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_choose_brands);
        EventBus.getDefault().register(this);
        initViews();
        initData();
        loadBrands();
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

    private void initData() {
        mDeviceType = getIntent().getIntExtra("deviceType", -1);
    }

    private void loadBrands() {
        mItems = new ArrayList<>();

        KookongSDK.getBrandListFromNet(mDeviceType, new IRequestResult<BrandList>() {
            @Override
            public void onSuccess(String s, BrandList brandList) {
                LogUtil.i("getBrandListFromNet succ: size-->" + brandList.brandList.size());

                addBrands(brandList.hotCount, brandList.brandList);
                refreshItems();
            }

            @Override
            public void onFail(Integer integer, String s) {
                LogUtil.e("getBrandListFromNet failed: err-->" + s + ",code-->" + integer);
            }
        });
    }

    private void initViews() {
        findViewById(R.id.id_dialog_choose_brand_back_iv)
                .setOnClickListener(this);
        this.mBrandRv = findViewById(R.id.id_dialog_choose_brand_rv);
        this.mSearchEt = findViewById(R.id.id_dialog_choose_brand_search_tv);
        mBrandRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void refreshItems() {
        BrandAdapter adapter = new BrandAdapter(this, mItems);
        mBrandRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new SimpleOnItemClickListener<BrandList.Brand>() {
            @Override
            public void onClickItem(RecyclerView.Adapter adapter, int position, BrandList.Brand data) {
                IRMatchBaseActivity.launchCommon(IRChooseBrandActivity.this,
                        100, mDeviceType, data);
            }
        });
    }

    private void addBrands(int hotCount, List<BrandList.Brand> brandList) {
        if (mItems == null || brandList == null)
            return;

        addCommonBrands(hotCount, brandList);

        Collections.sort(brandList, new Comparator<BrandList.Brand>() {
            @Override
            public int compare(BrandList.Brand o1, BrandList.Brand o2) {
                String s1 = o1.ename == null ? "" : o1.ename;
                String s2 = o2.ename == null ? "" : o2.ename;
                return s1.compareToIgnoreCase(s2);
            }
        });

        ItemBean title = null;
        for (BrandList.Brand brand : brandList) {
            if (title == null || !TextUtils.equals(title.title, brand.initial)) {
                title = new ItemBean(true, brand.initial);
                mItems.add(title);
            }
            mItems.add(new ItemBean(brand));
        }
    }

    private void addCommonBrands(int hotCount, List<BrandList.Brand> brandList) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }


        mItems.add(new ItemBean(true, getResources().getString(R.string.ir_common_brands)));

        for (int i = 0; i < hotCount; i++) {
            mItems.add(new ItemBean(brandList.get(i)));
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_dialog_choose_brand_back_iv:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private class ItemBean {
        boolean isTitle;
        String title;
        BrandList.Brand brand;

        public ItemBean(boolean isTitle, String title) {
            this.isTitle = isTitle;
            this.title = title;
        }

        public ItemBean(BrandList.Brand brand) {
            this.brand = brand;
        }
    }


    private static class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.Holder> {

        private static final int VIEW_TITLE = 0;
        private static final int VIEW_BRAND = 1;
        private Context mContext;
        private List<ItemBean> mItems;
        private SimpleOnItemClickListener<BrandList.Brand> onItemClickListener;

        public BrandAdapter(Context mContext, List<ItemBean> mItems) {
            this.mContext = mContext;
            this.mItems = mItems;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (viewType == VIEW_TITLE) {
                return new TitleHolder(inflater.inflate(R.layout.adapter_ir_brand_title, parent, false));
            }
            return new BrandHolder(inflater.inflate(R.layout.adapter_ir_brand, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ItemBean itemBean = mItems.get(position);
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TITLE) {
                TitleHolder th = (TitleHolder) holder;
                th.mTextTv.setText(itemBean.title);
            } else {
                BrandHolder bh = (BrandHolder) holder;
                bh.mTextTv.setText(itemBean.brand.ename);
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

        public SimpleOnItemClickListener<BrandList.Brand> getOnItemClickListener() {
            return onItemClickListener;
        }

        public void setOnItemClickListener(SimpleOnItemClickListener<BrandList.Brand> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        class Holder extends RecyclerView.ViewHolder {

            public Holder(View itemView) {
                super(itemView);
            }
        }

        class TitleHolder extends Holder {

            private final TextView mTextTv;

            public TitleHolder(View itemView) {
                super(itemView);
                mTextTv = itemView.findViewById(R.id.id_adapter_ir_brand_item_tv);
            }
        }

        class BrandHolder extends Holder {
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
                            onItemClickListener.onClickItem(BrandAdapter.this, position, itemBean.brand);
                        }
                    }
                });
            }
        }
    }
}
