package com.gjzg.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.EvaluateAdapter;
import com.gjzg.bean.EvaluateBean;
import com.gjzg.config.NetConfig;
import getevaluate.presenter.GetEvaluatePresenter;
import getevaluate.presenter.IGetEvaluatePresenter;
import getevaluate.view.IGetEvaluateFragment;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class GetEvaluateFragment extends Fragment implements IGetEvaluateFragment, PullToRefreshLayout.OnRefreshListener {

    private View rootView, headView;
    private TextView countTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private CProgressDialog cProgressDialog;
    private List<EvaluateBean> evaluateBeanList = new ArrayList<>();
    private EvaluateAdapter evaluateAdapter;
    private IGetEvaluatePresenter getEvaluatePresenter;

    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;

    private final int FIRST = 1, REFRESH = 2;
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
                        break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.common_listview, null);
        initView();
        initData();
        setData();
        setListener();
        loadData();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
        cProgressDialog = Utils.initProgressDialog(getActivity(), cProgressDialog);
    }

    private void initEmptyView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_evaluate, null);
        countTv = (TextView) headView.findViewById(R.id.tv_head_evaluate_count);
        plv.addHeaderView(headView);
    }

    private void initData() {
        getEvaluatePresenter = new GetEvaluatePresenter(this);
        evaluateAdapter = new EvaluateAdapter(getActivity(), evaluateBeanList);
    }

    private void setData() {
        plv.setAdapter(evaluateAdapter);
    }

    private void setListener() {
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        switch (STATE) {
            case FIRST:
                cProgressDialog.show();
                break;
        }
        getEvaluatePresenter.load(NetConfig.otherEvaluateUrl + "?tc_u_id=" + UserUtils.readUserData(getActivity()).getId());
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cProgressDialog.dismiss();
                break;
            case REFRESH:
                ptrl.hideHeadView();
                break;
        }
        countTv.setText("我收到的评价（" + evaluateBeanList.size() + "）");
        evaluateAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadSuccess(String json) {
        evaluateBeanList.clear();
        evaluateBeanList.addAll(DataUtils.getEvaluateBeanList(json));
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void loadFailure(String failure) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        STATE = REFRESH;
        loadData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.hideFootView();
    }
}
