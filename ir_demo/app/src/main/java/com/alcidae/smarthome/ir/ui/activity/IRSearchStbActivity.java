package com.alcidae.smarthome.ir.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alcidae.smarthome.R;
import com.alcidae.smarthome.ir.ui.activity.match.IRMatchBaseActivity;
import com.alcidae.smarthome.ir.util.SimpeIRequestResult;
import com.alcidae.smarthome.ir.util.SimpleOnItemClickListener;
import com.alcidae.smarthome.ir.util.ToastUtil;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.utils.LogUtil;
import com.kookong.app.data.StbList;

import java.util.List;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/4/4 14:38 1.0
 * @time 2018/4/4 14:38
 * @project ir_demo com.alcidae.smarthome.ir.ui.activity
 * @description search stb by input
 * @updateVersion 1.0
 * @updateTime 2018/4/4 14:38
 */

public class IRSearchStbActivity extends Activity implements SimpleOnItemClickListener<StbList.Stb> {

    private EditText mSearchEt;
    private RecyclerView mStbRv;
    private List<StbList.Stb> mStbs;
    private int mAreaId;
    private int mDeviceType;

    public static void launch(Activity activity, int deviceType, int areaId) {
        Intent intent = new Intent(activity, IRSearchStbActivity.class);
        intent.putExtra("deviceType", deviceType);
        intent.putExtra("areaId", areaId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_search_stb);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mAreaId = intent.getIntExtra("areaId", 0);
        mDeviceType = intent.getIntExtra("deviceType", 0);
    }

    private void initView() {
        mSearchEt = findViewById(R.id.id_dialog_choose_brand_search_tv);
        mStbRv = findViewById(R.id.id_dialog_choose_brand_rv);

        mStbRv.setLayoutManager(new LinearLayoutManager(this));

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(v.getText().toString());
                    return true;
                }

                return false;
            }
        });
    }

    private void doSearch(String word) {
        KookongSDK.searchSTB(word, mAreaId, new SimpeIRequestResult<StbList>(this) {
            @Override
            public void onSuccess(String s, StbList stbList) {
                LogUtil.i("searchSTB onSuccess ");
                mStbs = stbList.stbList;
                SearchStbAdapter searchStbAdapter = new SearchStbAdapter(IRSearchStbActivity.this, mStbs);
                mStbRv.setAdapter(searchStbAdapter);
                searchStbAdapter.setOnItemClickListener(IRSearchStbActivity.this);
            }
        });
    }

    @Override
    public void onClickItem(RecyclerView.Adapter adapter, int position, StbList.Stb data) {
        IRMatchBaseActivity.launchByIPTV(this, 6699, mDeviceType, mAreaId, null, data);
    }


    private static class SearchStbAdapter extends RecyclerView.Adapter<SearchStbAdapter.Holder> {

        private static final int VIEW_TITLE = 0;
        private static final int VIEW_BRAND = 1;
        private Context mContext;
        private List<StbList.Stb> mItems;
        private SimpleOnItemClickListener<StbList.Stb> onItemClickListener;

        public SearchStbAdapter(Context mContext, List<StbList.Stb> mItems) {
            this.mContext = mContext;
            this.mItems = mItems;
        }

        @Override
        public SearchStbAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            return new SearchStbAdapter.Holder(inflater.inflate(R.layout.adapter_ir_search_stb, parent, false));
        }

        @Override
        public void onBindViewHolder(SearchStbAdapter.Holder holder, int position) {
            holder.mTextTv.setText(mItems.get(position).bname);
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

            private final TextView mTextTv;

            public Holder(View itemView) {
                super(itemView);
                mTextTv = itemView.findViewById(R.id.id_adapter_ir_brand_item_tv);
                mTextTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (onItemClickListener != null) {
                            StbList.Stb itemBean = mItems.get(position);
                            onItemClickListener.onClickItem(SearchStbAdapter.this, position, itemBean);
                        }
                    }
                });
            }
        }


    }

}
