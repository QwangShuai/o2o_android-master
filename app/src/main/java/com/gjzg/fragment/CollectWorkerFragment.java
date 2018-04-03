package com.gjzg.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.gjzg.activity.TalkWorkerActivity;
import com.gjzg.activity.WorkerActivity;
import com.gjzg.adapter.CollectWorkerAdapter;

import com.gjzg.bean.CollectWorker;
import com.gjzg.bean.ToTalkWorkerBean;
import com.gjzg.bean.ToWorkerBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.singleton.SingleGson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

/**
 * 收藏的工人
 */
public class CollectWorkerFragment extends Fragment implements PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private CProgressDialog cProgressDialog;
    private List<CollectWorker.DataBeanX.DataBean> mList;
    private CollectWorkerAdapter mAdapter;
    private int clickPosition;
    private final int FIRST = 5, REFRESH = 6;
    private int STATE;
    private String collectWorkerUrl, cancelWorkerUrl;
    ToWorkerBean toWorkerBean;

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
        collectWorkerUrl = null;
        cancelWorkerUrl = null;
        clickPosition = 0;
        STATE = FIRST;
        mList = new ArrayList<>();
        mAdapter = new CollectWorkerAdapter(getActivity(), mList, this);
        toWorkerBean = (ToWorkerBean) getActivity().getIntent().getSerializableExtra(IntentConfig.toWorker);
    }

    private void setData() {
        plv.setAdapter(mAdapter);
    }

    private void setListener() {
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == FIRST)
            cProgressDialog.show();
        collectWorkerUrl = NetConfig.collectWorkerUrl + "?u_id=" + UserUtils.readUserData(getActivity()).getId();
        OkHttpUtils.get().tag(this).url(collectWorkerUrl).build().execute(new StringCallback() {

            @Override
            public void onError(Request request, Exception e) {
                notifyNet();
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    CollectWorker collectWorker = SingleGson.getInstance().fromJson(response, CollectWorker.class);
                    if (collectWorker != null && collectWorker.getData() != null && collectWorker.getData().getData() != null) {
                        if (STATE == REFRESH)
                            mList.clear();
                        mList.addAll(collectWorker.getData().getData());
                        notifyData();
                    }
                }
            }
        });
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cProgressDialog.dismiss();
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
                cProgressDialog.dismiss();
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

    private void cancelWorker() {
        cancelWorkerUrl = NetConfig.favorateDelUrl + "?f_id=" + mList.get(clickPosition).getF_id();
        OkHttpUtils.get().tag(this).url(cancelWorkerUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                notifyNet();
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        int code = object.optInt("code");
                        switch (code) {
                            case 0:
                                Utils.toast(getActivity(), "取消收藏失败");
                                break;
                            case 1:
                                Utils.toast(getActivity(), "取消收藏成功");
                                mList.remove(clickPosition);
                                notifyData();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
    public void onClick(int id, int pos) {
        clickPosition = pos;
        switch (id) {
            case R.id.iv_item_worker_collect:
                cancelWorker();
                break;
            case R.id.ll_item_worker:
                Intent intent = new Intent(getActivity(), TalkWorkerActivity.class);
                ToTalkWorkerBean toTalkWorkerBean = new ToTalkWorkerBean();
                toTalkWorkerBean.setU_id(mList.get(pos).getU_id());
                toTalkWorkerBean.setS_id(toTalkWorkerBean.getS_id());
                toTalkWorkerBean.setS_name(toTalkWorkerBean.getS_name());
                intent.putExtra(IntentConfig.toTalkWorker, toTalkWorkerBean);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
