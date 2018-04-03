package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.adapter.TaskAdapter;
import com.gjzg.bean.Task;
import com.gjzg.bean.ToTalkEmployerBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.singleton.SingleGson;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private CProgressDialog cProgressDialog;
    private RelativeLayout returnRl;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private List<Task.DataBean> mList;
    private TaskAdapter mAdapter;
    private final int FIRST = 0, REFRESH = 1;
    private int STATE;
    private String taskUrl, collectTaskUrl;
    private int clickPostion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_task, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        getTaskUrl();
        loadData();
    }

    private void initView() {
        initRootView();
        initEmptyView();
    }

    private void initRootView() {
        cProgressDialog = Utils.initProgressDialog(TaskActivity.this, cProgressDialog);
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_task_return);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(TaskActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(TaskActivity.this).inflate(R.layout.empty_net, null);
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
        mList = new ArrayList<>();
        mAdapter = new TaskAdapter(this, mList, this);
        STATE = FIRST;
    }

    private void setData() {
        plv.setAdapter(mAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(new View.OnClickListener() {
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
        OkHttpUtils.get().tag(this).url(taskUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Task task = SingleGson.getInstance().fromJson(response, Task.class);
                    if (task != null && task.getData() != null) {
                        if (STATE == REFRESH)
                            mList.clear();
                        mList.addAll(task.getData());
                        notifyData();
                    }
                }
            }
        });
    }

    private void collectTask() {
        collectTaskUrl = NetConfig.favorateAddUrl
                + "?u_id=" + UserUtils.readUserData(TaskActivity.this).getId()
                + "&f_type_id=" + mList.get(clickPostion).getT_id()
                + "&f_type=0";
        OkHttpUtils.get().tag(this).url(collectTaskUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        int code = object.optInt("code");
                        switch (code) {
                            case 0:
                                Utils.toast(TaskActivity.this, "收藏失败");
                                break;
                            case 1:
                                Utils.toast(TaskActivity.this, "已收藏");
                                mList.get(clickPostion).setFavorate(1);
                                mAdapter.notifyDataSetChanged();
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
                    emptyView.setVisibility(View.VISIBLE);
                    netView.setVisibility(View.GONE);
                } else {
                    ptrl.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    netView.setVisibility(View.GONE);
                }
                break;
            case REFRESH:
                ptrl.hideHeadView();
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int id, int pos) {
        clickPostion = pos;
        switch (id) {
            case R.id.ll_item_task:
                Intent intent = new Intent(TaskActivity.this, TalkEmployerActivity.class);
                ToTalkEmployerBean toTalkEmployerBean = new ToTalkEmployerBean();
                toTalkEmployerBean.setT_id(mList.get(clickPostion).getT_id());
                intent.putExtra(IntentConfig.toTalkEmployer, toTalkEmployerBean);
                startActivity(intent);
                break;
            case R.id.iv_item_task_collect:
                if (UserUtils.isUserLogin(TaskActivity.this)) {
                    if (UserUtils.readUserData(TaskActivity.this).getId().equals(mList.get(clickPostion).getT_author())) {
                        Utils.toast(TaskActivity.this, VarConfig.cannotCollectSelf);
                    } else {
                        switch (mList.get(clickPostion).getFavorate()) {
                            case 0:
                                collectTask();
                                break;
                            case 1:
                                Utils.toast(TaskActivity.this, "已收藏");
                                break;
                        }
                    }
                } else {
                    startActivity(new Intent(TaskActivity.this, LoginActivity.class));
                }
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

    private void getTaskUrl() {
        if (UserUtils.isUserLogin(TaskActivity.this)) {
            taskUrl = NetConfig.taskBaseUrl + "?u_id=" + UserUtils.readUserData(TaskActivity.this).getId();
        } else {
            taskUrl = NetConfig.taskBaseUrl;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
