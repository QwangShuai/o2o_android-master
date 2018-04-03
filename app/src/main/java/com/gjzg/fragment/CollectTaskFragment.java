package com.gjzg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gjzg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.activity.SkillActivity;
import com.gjzg.activity.TalkEmployerActivity;
import com.gjzg.activity.TalkWorkerActivity;
import com.gjzg.activity.WorkerActivity;
import com.gjzg.adapter.CollectTaskAdapter;
import com.gjzg.bean.CollectTaskBean;

import collecttask.presenter.CollectTaskPresenter;
import collecttask.presenter.ICollectTaskPresenter;
import collecttask.view.ICollectTaskFragment;

import com.gjzg.bean.ToTalkEmployerBean;
import com.gjzg.bean.ToTalkWorkerBean;
import com.gjzg.bean.ToWorkerBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.singleton.SingleGson;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 收藏的任务
 */
public class CollectTaskFragment extends Fragment implements ICollectTaskFragment, IdPosClickHelp, PullToRefreshLayout.OnRefreshListener {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private CProgressDialog cpd;
    private ICollectTaskPresenter collectTaskPresenter;

    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private List<CollectTaskBean.DataBeanX.DataBean> mList;
    private CollectTaskAdapter mAdapter;

    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;
    private final int CANCEL_SUCCESS = 3;
    private final int CANCEL_FAILURE = 4;
    private final int FIRST = 5;
    private final int REFRESH = 6;
    private int STATE = FIRST;

    private int clickPosition = 0;
    private String tip = "";

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
                    case CANCEL_SUCCESS:
                        Utils.toast(getActivity(), tip);
                        mList.remove(clickPosition);
                        notifyData();
                        break;
                    case CANCEL_FAILURE:
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
        OkHttpUtils.getInstance().cancelTag(this);
    }

    private void initView() {
        initRootView();
        initEmptyView();
    }

    private void initRootView() {
        cpd = Utils.initProgressDialog(getActivity(), cpd);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_net, null);
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
        collectTaskPresenter = new CollectTaskPresenter(this);
        mList = new ArrayList<>();
        mAdapter = new CollectTaskAdapter(getActivity(), mList, this);
    }

    private void setData() {
        plv.setAdapter(mAdapter);
    }

    private void setListener() {
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == FIRST)
            cpd.show();
        OkHttpUtils
                .get()
                .tag(this)
                .url(NetConfig.collectTaskUrl)
                .addParams("u_id", UserUtils.readUserData(getActivity()).getId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        notifyNet();
                    }

                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            CollectTaskBean collectTaskBean = SingleGson.getInstance().fromJson(response, CollectTaskBean.class);
                            if (collectTaskBean != null) {
                                if (collectTaskBean.getCode() == 1) {
                                    if (collectTaskBean.getData() != null) {
                                        if (collectTaskBean.getData().getData() != null) {
                                            if (STATE == REFRESH)
                                                mList.clear();
                                            mList.addAll(collectTaskBean.getData().getData());
                                            notifyData();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                if (mList.size() == 0) {
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
        mAdapter.notifyDataSetChanged();
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
                Utils.toast(getActivity(), VarConfig.noNetTip);
                break;
        }
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

    @Override
    public void loadSuccess(String json) {
        switch (STATE) {
            case REFRESH:
                mList.clear();
                break;
        }
//        mList.addAll(DataUtils.getCollectTaskList(json));
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void loadFailure(String failure) {
    }

    @Override
    public void cancelCollectSuccess(String json) {
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 1:
                    JSONObject dataObj = beanObj.optJSONObject("data");
                    if (dataObj != null) {
                        tip = dataObj.optString("msg");
                        handler.sendEmptyMessage(CANCEL_SUCCESS);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelCollectFailure(String failure) {
        handler.sendEmptyMessage(CANCEL_FAILURE);
    }

    @Override
    public void onClick(int id, int pos) {
        clickPosition = pos;
        switch (id) {
            case R.id.iv_item_task_collect:
                collectTaskPresenter.cancelCollect(NetConfig.delCollectUrl + "?f_id=" + mList.get(clickPosition).getF_id());
                break;
            case R.id.ll_item_task:
                Intent intent = new Intent(getActivity(), TalkEmployerActivity.class);
                ToTalkEmployerBean toTalkEmployerBean = new ToTalkEmployerBean();
                toTalkEmployerBean.setT_id(mList.get(pos).getT_id());
                intent.putExtra(IntentConfig.toTalkEmployer,toTalkEmployerBean);
                startActivity(intent);
                break;
        }
    }
}
