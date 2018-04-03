package com.gjzg.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.bean.ToJumpEmployerBean;
import com.gjzg.config.ColorConfig;
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
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.adapter.WorkerManageAdapter;
import com.gjzg.bean.WorkerManageBean;

import workermanage.presenter.IWorkerManagePresenter;
import workermanage.presenter.WorkerManagePresenter;
import workermanage.view.IWorkerManageActivity;

/**
 * 工人
 */
public class WorkerManageActivity extends AppCompatActivity implements IWorkerManageActivity, View.OnClickListener, PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private RelativeLayout returnRl, allRl, talkRl, doingRl, doneRl;
    private TextView allTv, talkTv, doingTv, doneTv;
    private CProgressDialog cpd;
    private PullableListView lv;
    private WorkerManageAdapter workerManageAdapter;
    private List<WorkerManageBean> workerManageBeanList = new ArrayList<>();
    private final int ALL = 0, TALK = 1, DOING = 2, DONE = 3;
    private int curState = ALL, tarState = -1;
    private final int FIRST = 0, REFRESH = 1;
    private int STATE = FIRST;
    private final int LOAD_SUCCESS = 4;
    private final int LOAD_FAILURE = 5;
    private int clickPosition;

    private OkHttpClient okHttpClient;
    private IWorkerManagePresenter workerManagePresenter;

    private View deletePopView;
    private PopupWindow deletePop;

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
                    case 666:
                        workerManageBeanList.remove(clickPosition);
                        workerManageAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_worker_manage, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
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
        initPopView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_worker_manage_return);
        allRl = (RelativeLayout) rootView.findViewById(R.id.rl_worker_manage_all);
        allTv = (TextView) rootView.findViewById(R.id.tv_worker_manage_all);
        talkRl = (RelativeLayout) rootView.findViewById(R.id.rl_worker_manage_talk);
        talkTv = (TextView) rootView.findViewById(R.id.tv_worker_manage_talk);
        doingRl = (RelativeLayout) rootView.findViewById(R.id.rl_worker_manage_doing);
        doingTv = (TextView) rootView.findViewById(R.id.tv_worker_manage_doing);
        doneRl = (RelativeLayout) rootView.findViewById(R.id.rl_worker_manage_done);
        doneTv = (TextView) rootView.findViewById(R.id.tv_worker_manage_done);
        cpd = Utils.initProgressDialog(WorkerManageActivity.this, cpd);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        lv = (PullableListView) rootView.findViewById(R.id.plv);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(WorkerManageActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(WorkerManageActivity.this).inflate(R.layout.empty_net, null);
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

    private void initPopView() {
        deletePopView = LayoutInflater.from(WorkerManageActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) deletePopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认删除？");
        ((TextView) deletePopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) deletePopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        deletePopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletePop.isShowing())
                    deletePop.dismiss();
            }
        });
        deletePopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletePop.isShowing()) {
                    deletePop.dismiss();
                    delete();
                }
            }
        });
        deletePop = new PopupWindow(deletePopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        deletePop.setFocusable(true);
        deletePop.setTouchable(true);
        deletePop.setOutsideTouchable(true);
        deletePop.setBackgroundDrawable(new BitmapDrawable());
        deletePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.light(WorkerManageActivity.this);
            }
        });
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        curState = ALL;
        workerManagePresenter = new WorkerManagePresenter(this);
        workerManageAdapter = new WorkerManageAdapter(WorkerManageActivity.this, workerManageBeanList, this);
    }

    private void setData() {
        lv.setAdapter(workerManageAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        allRl.setOnClickListener(this);
        talkRl.setOnClickListener(this);
        doingRl.setOnClickListener(this);
        doneRl.setOnClickListener(this);
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        if (STATE == FIRST) {
            cpd.show();
        }
        String workerManageUrl = Utils.getWorkerManageUrl(WorkerManageActivity.this, curState);
        workerManagePresenter.load(workerManageUrl);
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                if (workerManageBeanList.size() == 0) {
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
        workerManageAdapter.notifyDataSetChanged();
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
                Utils.toast(WorkerManageActivity.this, VarConfig.noNetTip);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_worker_manage_return:
                startActivity(new Intent(WorkerManageActivity.this, MainActivity.class));
                break;
            case R.id.rl_worker_manage_all:
                tarState = ALL;
                refreshView();
                break;
            case R.id.rl_worker_manage_talk:
                tarState = TALK;
                refreshView();
                break;
            case R.id.rl_worker_manage_doing:
                tarState = DOING;
                refreshView();
                break;
            case R.id.rl_worker_manage_done:
                tarState = DONE;
                refreshView();
                break;
        }
    }

    private void refreshView() {
        if (tarState != curState) {
            switch (tarState) {
                case ALL:
                    allTv.setTextColor(ColorConfig.red_ff3e50);
                    talkTv.setTextColor(ColorConfig.black_252323);
                    doingTv.setTextColor(ColorConfig.black_252323);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case TALK:
                    allTv.setTextColor(ColorConfig.black_252323);
                    talkTv.setTextColor(ColorConfig.red_ff3e50);
                    doingTv.setTextColor(ColorConfig.black_252323);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case DOING:
                    allTv.setTextColor(ColorConfig.black_252323);
                    talkTv.setTextColor(ColorConfig.black_252323);
                    doingTv.setTextColor(ColorConfig.red_ff3e50);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case DONE:
                    allTv.setTextColor(ColorConfig.black_252323);
                    talkTv.setTextColor(ColorConfig.black_252323);
                    doingTv.setTextColor(ColorConfig.black_252323);
                    doneTv.setTextColor(ColorConfig.red_ff3e50);
                    break;
            }
            curState = tarState;
            STATE = FIRST;
            loadData();
        }
    }

    @Override
    public void loadSuccess(String json) {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                workerManageBeanList.clear();
                break;
            case REFRESH:
                workerManageBeanList.clear();
                break;
        }
        workerManageBeanList.addAll(DataUtils.getWorkerManageBeanList(json));
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void loadFailure(String failure) {
        handler.sendEmptyMessage(LOAD_FAILURE);
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
            case R.id.ll_item_worker_manage:
                Intent talkIntent = new Intent(WorkerManageActivity.this, JumpEmployerActivity.class);
                ToJumpEmployerBean toJumpEmployerBean = new ToJumpEmployerBean();
                toJumpEmployerBean.setTaskId(workerManageBeanList.get(clickPosition).getTaskId());
                talkIntent.putExtra(IntentConfig.toJumpEmployer, toJumpEmployerBean);
                startActivity(talkIntent);
                break;
            case R.id.tv_item_worker_manage_delete:
                if (!deletePop.isShowing()) {
                    Utils.dark(WorkerManageActivity.this);
                    deletePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
        }
    }

    private void delete() {
        //?action=del2&o_worker=4&o_id=2
        String url = NetConfig.orderUrl + "?action=del2&o_worker=" + UserUtils.readUserData(WorkerManageActivity.this).getId() + "&o_id=" + workerManageBeanList.get(clickPosition).getOrderId();
        Request delRequest = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(delRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    handler.sendEmptyMessage(666);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(WorkerManageActivity.this, MainActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
