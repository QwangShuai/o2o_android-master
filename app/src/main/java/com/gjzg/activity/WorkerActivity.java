package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.Worker;
import com.gjzg.singleton.SingleGson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.bean.ToTalkWorkerBean;
import com.gjzg.bean.ToWorkerBean;

import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.listener.IdPosClickHelp;

import com.gjzg.utils.UserUtils;

import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.Utils;
import com.gjzg.adapter.WorkerAdapter;
import com.gjzg.custom.CProgressDialog;

public class WorkerActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private RelativeLayout returnRl;
    private CProgressDialog cProgressDialog;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private List<Worker.DataBeanX.DataBean> mList;
    private WorkerAdapter workerAdapter;
    private int clickPosition;
    private final int FIRST = 1, REFRESH = 2;
    private int STATE = FIRST;
    private ToWorkerBean toWorkerBean;
    private String workerUrl, collectWorkerUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_worker, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        getWorkerUrl();
        loadData();
    }

    private void initView() {
        initRootView();
        initEmptyView();
        initDialogView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_worker_return);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(WorkerActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(WorkerActivity.this).inflate(R.layout.empty_net, null);
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

    private void initDialogView() {
        cProgressDialog = new CProgressDialog(WorkerActivity.this, R.style.dialog_cprogress);
    }

    private void initData() {
        mList = new ArrayList<>();
        workerAdapter = new WorkerAdapter(WorkerActivity.this, mList, this);
        toWorkerBean = (ToWorkerBean) getIntent().getSerializableExtra(IntentConfig.toWorker);
    }

    private void setData() {
        plv.setAdapter(workerAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == FIRST)
            cProgressDialog.show();
        OkHttpUtils.get().tag(this).url(workerUrl).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {
                notifyNet();
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Worker worker = SingleGson.getInstance().fromJson(response, Worker.class);
                    if (worker != null && worker.getData() != null && worker.getData().getData() != null) {
                        if (STATE == REFRESH)
                            mList.clear();
                        mList.addAll(worker.getData().getData());
                        notifyData();
                    }
                }
            }
        });
    }

    private void getWorkerUrl() {
        if (UserUtils.isUserLogin(WorkerActivity.this)) {
            workerUrl = NetConfig.workerUrl +
                    "?u_skills=" + toWorkerBean.getS_id() +
                    "&fu_id=" + UserUtils.readUserData(WorkerActivity.this).getId();
        } else {
            workerUrl = NetConfig.workerUrl +
                    "?u_skills=" + toWorkerBean.getS_id();
        }
    }

    private void collectWorker() {
        collectWorkerUrl = NetConfig.addCollectUrl +
                "?u_id=" + UserUtils.readUserData(WorkerActivity.this).getId() +
                "&f_type_id=" + mList.get(clickPosition).getU_id() +
                "&f_type=1";
        OkHttpUtils.get().tag(this).url(collectWorkerUrl).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {
                Utils.toast(WorkerActivity.this,"莫名其妙的请求失败了");
            }

            @Override
            public void onResponse(String response) {
                Log.i("response",response);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        int code = object.optInt("code");
                        Log.i("code",code+"");
                        switch (code) {
                            case 0:
                                Utils.toast(WorkerActivity.this, "收藏失败");
                                break;
                            case 1:
                                Utils.toast(WorkerActivity.this, "已收藏");
                                mList.get(clickPosition).setIs_fav(1);
                                workerAdapter.notifyDataSetChanged();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        workerAdapter.notifyDataSetChanged();
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
                Utils.toast(WorkerActivity.this, VarConfig.noNetTip);
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
    public void onClick(int id, int pos) {
        clickPosition = pos;
        Worker.DataBeanX.DataBean dataBean = mList.get(clickPosition);
        switch (id) {
            case R.id.ll_item_worker:
                Intent intent = new Intent(WorkerActivity.this, TalkWorkerActivity.class);
                ToTalkWorkerBean toTalkWorkerBean = new ToTalkWorkerBean();
                toTalkWorkerBean.setU_id(mList.get(pos).getU_id());
                toTalkWorkerBean.setS_id(toWorkerBean.getS_id());
                toTalkWorkerBean.setS_name(toWorkerBean.getS_name());
                intent.putExtra(IntentConfig.toTalkWorker, toTalkWorkerBean);
                startActivity(intent);
                break;
            case R.id.iv_item_worker_collect:
                if (UserUtils.isUserLogin(WorkerActivity.this)) {
                    if (UserUtils.readUserData(WorkerActivity.this).getId().equals(dataBean.getU_id())) {
                        Utils.toast(WorkerActivity.this, VarConfig.cannotCollectSelf);
                    } else {
                        int fav = dataBean.getIs_fav();
                        Log.i("fav","="+dataBean.getIs_fav());
                        switch (fav) {
                            case 0:
                                collectWorker();
                                break;
                            case 1:
                                Utils.toast(WorkerActivity.this, "已收藏");
                                break;
                        }
                    }
                } else {
                    startActivity(new Intent(WorkerActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
