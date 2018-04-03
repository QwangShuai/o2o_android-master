package com.gjzg.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.adapter.VoucherAdapter;
import com.gjzg.bean.VoucherBean;
import voucher.presenter.IVoucherPresenter;
import voucher.presenter.VoucherPresenter;
import voucher.view.IVoucherActivity;

public class VoucherActivity extends AppCompatActivity implements IVoucherActivity, View.OnClickListener, PullToRefreshLayout.OnRefreshListener {

    private View rootView, emptyView, netView;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private FrameLayout fl;
    private TextView netTv;
    private RelativeLayout returnRl;
    private CProgressDialog cpd;
    private List<VoucherBean> voucherBeanList = new ArrayList<>();
    private VoucherAdapter voucherAdapter;

    private IVoucherPresenter voucherPresenter;

    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;
    private final int FIRST = 3;
    private final int REFRESH = 4;
    private int STATE = FIRST;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        notifyData();
                        break;
                    case LOAD_FAILURE:
                        notifyNet();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_voucher, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voucherPresenter != null) {
            voucherPresenter.destroy();
            voucherPresenter = null;
        }
        if (handler != null) {
            handler.removeMessages(LOAD_SUCCESS);
            handler.removeMessages(LOAD_FAILURE);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
        initEmptyView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_voucher_return);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
        cpd = Utils.initProgressDialog(this, cpd);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(VoucherActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(VoucherActivity.this).inflate(R.layout.empty_net, null);
        netTv = (TextView) netView.findViewById(R.id.tv_no_net_refresh);
        netTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptrl.setVisibility(View.VISIBLE);
                netView.setVisibility(View.GONE);
                STATE = FIRST;
                loadData();
            }
        });
        fl.addView(netView);
        netView.setVisibility(View.GONE);
    }

    private void initData() {
        voucherPresenter = new VoucherPresenter(this);
        voucherAdapter = new VoucherAdapter(this, voucherBeanList);
    }

    private void setData() {
        plv.setAdapter(voucherAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        cpd.show();
        voucherPresenter.load(NetConfig.redBagUrl + "?action=list&uid=" + UserUtils.readUserData(VoucherActivity.this).getId() + "&bt_id=1");
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                if (voucherBeanList.size() == 0) {
                    ptrl.setVisibility(View.GONE);
                    netView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    ptrl.setVisibility(View.VISIBLE);
                    netView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                }
                break;
            case REFRESH:
                ptrl.hideHeadView();
                break;
        }
        voucherAdapter.notifyDataSetChanged();
    }

    private void notifyNet() {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                ptrl.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                netView.setVisibility(View.VISIBLE);
                break;
            case REFRESH:
                ptrl.hideHeadView();
                Utils.toast(VoucherActivity.this, VarConfig.noNetTip);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_voucher_return:
                finish();
                break;
        }
    }

    @Override
    public void loadSuccess(String json) {
        voucherBeanList.addAll(DataUtils.getVoucherBeanList(json));
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void loadFailure(String failure) {
        handler.sendEmptyMessage(LOAD_FAILURE);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.hideHeadView();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.hideFootView();
    }
}
