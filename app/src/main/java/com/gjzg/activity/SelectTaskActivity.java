package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.SelectTaskAdapter;
import com.gjzg.bean.TaskBean;
import com.gjzg.bean.ToSelectTaskBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.listener.IdPosClickHelp;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;

import selecttask.presenter.ISelectTaskPresenter;
import selecttask.presenter.SelectTaskPresenter;
import selecttask.view.ISelectTaskActivity;

import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class SelectTaskActivity extends AppCompatActivity implements ISelectTaskActivity, View.OnClickListener, PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private RelativeLayout returnRl;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private CProgressDialog cpd;
    private List<TaskBean> taskBeanList = new ArrayList<>();
    private SelectTaskAdapter selectTaskAdapter;

    private ISelectTaskPresenter selectTaskPresenter;

    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;
    private final int TEW_ID = 7;
    private final int INVITE_SUCCESS = 3;
    private final int INVITE_FAILURE = 4;
    private final int FIRST = 5;
    private final int REFRESH = 6;
    private int STATE = FIRST;

    private String tew_id;
    private ToSelectTaskBean toSelectTaskBean;
    private int clickPosition;

    private String selectTip;

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
                    case TEW_ID:
                        cpd.show();
                        String url = NetConfig.orderUrl +
                                "?action=create" +
                                "&tew_id=" + tew_id +
                                "&t_id=" + taskBeanList.get(clickPosition).getTaskId() +
                                "&o_worker=" + toSelectTaskBean.getWorkerId() +
                                "&o_sponsor=" + UserUtils.readUserData(SelectTaskActivity.this).getId();
                        selectTaskPresenter.invite(url);
                        break;
                    case INVITE_SUCCESS:
                        cpd.dismiss();
                        if (selectTip.equals("success")) {
                            startActivity(new Intent(SelectTaskActivity.this, EmployerManageActivity.class));
                        } else if (selectTip.equals("failure")) {
                            Utils.toast(SelectTaskActivity.this, "失败");
                        } else {
                            Utils.toast(SelectTaskActivity.this, selectTip);
                        }
                        break;
                    case INVITE_FAILURE:
                        cpd.dismiss();
                        Utils.toast(SelectTaskActivity.this, "邀请失败");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_select_task, null);
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
        if (handler != null) {
            handler.removeMessages(LOAD_SUCCESS);
            handler.removeMessages(LOAD_FAILURE);
            handler.removeMessages(INVITE_SUCCESS);
            handler.removeMessages(INVITE_FAILURE);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
        initEmptyView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_project_return);
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
        cpd = Utils.initProgressDialog(SelectTaskActivity.this, cpd);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(SelectTaskActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(SelectTaskActivity.this).inflate(R.layout.empty_net, null);
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
        selectTaskPresenter = new SelectTaskPresenter(this);
        selectTaskAdapter = new SelectTaskAdapter(SelectTaskActivity.this, taskBeanList, this);
        toSelectTaskBean = (ToSelectTaskBean) getIntent().getSerializableExtra(IntentConfig.toSelectTask);
    }

    private void setData() {
        plv.setAdapter(selectTaskAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        switch (STATE) {
            case FIRST:
                cpd.show();
                break;
        }
        String url = NetConfig.taskBaseUrl + "?t_author=" + UserUtils.readUserData(SelectTaskActivity.this).getId() + "&t_storage=0&t_status=0,1,5&skills=" + toSelectTaskBean.getSkillId();
        selectTaskPresenter.load(url);
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                if (taskBeanList.size() == 0) {
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
        selectTaskAdapter.notifyDataSetChanged();
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
                Utils.toast(SelectTaskActivity.this, VarConfig.noNetTip);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_project_return:
                finish();
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
                taskBeanList.clear();
                break;
        }
        taskBeanList.addAll(DataUtils.getTaskBeanList(json));
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void loadFailure(String failure) {
        handler.sendEmptyMessage(LOAD_FAILURE);
    }

    @Override
    public void inviteSuccess(String json) {
        json = Utils.cutJson(json);
        Log.e(this.getClass().getSimpleName(), "json\n" + json);
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    selectTip = beanObj.optString("data");
                    handler.sendEmptyMessage(INVITE_SUCCESS);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inviteFailure(String failure) {
        handler.sendEmptyMessage(INVITE_FAILURE);
    }

    private void screenOrder(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject beanObj = new JSONObject(json);
                        if (beanObj.optInt("code") == 200) {
                            JSONObject dataObj = beanObj.optJSONObject("data");
                            if (dataObj != null) {
                                JSONArray workerArr = dataObj.optJSONArray("t_workers");
                                if (workerArr != null) {
                                    for (int i = 0; i < workerArr.length(); i++) {
                                        JSONObject o = workerArr.optJSONObject(i);
                                        if (o != null) {
                                            int remain = Integer.parseInt(o.optString("remaining"));
                                            if (remain > 0) {
                                                tew_id = o.optString("tew_id");
                                                handler.sendEmptyMessage(TEW_ID);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(int id, int pos) {
        clickPosition = pos;
        switch (id) {
            case R.id.tv_item_select_task_choose:
                String url = NetConfig.taskBaseUrl +
                        "?action=info" +
                        "&t_id=" + taskBeanList.get(clickPosition).getTaskId() +
                        "&skills=" + toSelectTaskBean.getSkillId();
                screenOrder(url);
                break;
        }
    }
}
