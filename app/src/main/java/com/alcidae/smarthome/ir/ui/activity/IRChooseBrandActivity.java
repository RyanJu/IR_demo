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
import com.alcidae.smarthome.ir.util.SimpleOnItemClickListener;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.BrandList;

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
    private int deviceType;
    private RecyclerView mBrandRv;
    private EditText mSearchEt;

    private List<ItemBean> mItems;

    public static void launch(Activity activity, int deviceType, int requestCode) {
        Intent intent = new Intent(activity, IRChooseBrandActivity.class);
        intent.putExtra("deviceType", deviceType);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_choose_brands);
        initViews();
        initData();
        loadBrands();
    }

    private void initData() {
        deviceType = getIntent().getIntExtra("deviceType", -1);
    }

    private void loadBrands() {
        mItems = new ArrayList<>();
        mItems.add(new ItemBean(true, getResources().getString(R.string.ir_common_brands)));
        addCommonBrands();

        KookongSDK.getBrandListFromNet(deviceType, new IRequestResult<BrandList>() {
            @Override
            public void onSuccess(String s, BrandList brandList) {
                LogUtil.i("getBrandListFromNet succ: size-->" + brandList.brandList.size());
                addBrands(brandList.brandList);
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
                IRMatchTestActivity.launch(IRChooseBrandActivity.this,
                        100, deviceType, data);
            }
        });
    }

    private void addBrands(List<BrandList.Brand> brandList) {
        if (mItems == null || brandList == null)
            return;

        Collections.sort(brandList, new Comparator<BrandList.Brand>() {
            @Override
            public int compare(BrandList.Brand o1, BrandList.Brand o2) {
                char s1 = o1.initial.charAt(0);
                char s2 = o2.initial.charAt(0);
                return s1 - s2;
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

    private void addCommonBrands() {
        if (mItems == null)
            return;

        switch (deviceType) {
            case Device.AC:
                addCommonACBrands();
                break;
        }

    }

    private void addCommonACBrands() {
        /**
         * {"brandId":97,"initial":"G","cname":"Gree","ename":"Gree","pinyin":"Gree"},
         * {"brandId":182,"initial":"M","cname":"Midea","ename":"Midea","pinyin":"Midea"},
         * {"brandId":37,"initial":"H","cname":"Haier","ename":"Haier","pinyin":"Haier"},
         * {"brandId":192,"initial":"A","cname":"Aux","ename":"Aux","pinyin":"Aux"},
         * {"brandId":197,"initial":"C","cname":"Chigo","ename":"Chigo","pinyin":"Chigo"}
         */
        BrandList.Brand gree = new BrandList.Brand();
        gree.brandId = 97;
        gree.initial = "G";
        gree.cname = "Gree";
        gree.ename = "Gree";
        gree.pinyin = "Gree";
        mItems.add(new ItemBean(gree));

        BrandList.Brand midea = new BrandList.Brand();
        midea.brandId = 182;
        midea.initial = "M";
        midea.cname = "Midea";
        midea.ename = "Midea";
        midea.pinyin = "Midea";
        mItems.add(new ItemBean(midea));

        BrandList.Brand haier = new BrandList.Brand();
        haier.brandId = 37;
        haier.initial = "H";
        haier.cname = "Haier";
        haier.ename = "Haier";
        haier.pinyin = "Haier";
        mItems.add(new ItemBean(haier));

        BrandList.Brand aux = new BrandList.Brand();
        aux.brandId = 192;
        aux.initial = "A";
        aux.cname = "Aux";
        aux.ename = "Aux";
        aux.pinyin = "Aux";
        mItems.add(new ItemBean(aux));

        BrandList.Brand chigo = new BrandList.Brand();
        chigo.brandId = 197;
        chigo.initial = "C";
        chigo.cname = "Chigo";
        chigo.ename = "Chigo";
        chigo.pinyin = "Chigo";
        mItems.add(new ItemBean(chigo));
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
