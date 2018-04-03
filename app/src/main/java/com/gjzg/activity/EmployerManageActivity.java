package com.gjzg.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.bean.ToTalkEmployerBean;
import com.gjzg.config.ColorConfig;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.StateConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.adapter.EmployerManageAdapter;
import com.gjzg.bean.EmployerManageBean;

import employermanage.presenter.EmployerManagePresenter;
import employermanage.presenter.IEmployerManagePresenter;
import employermanage.view.IEmployerManageActivity;

import com.gjzg.listener.IdPosClickHelp;

import firstpage.listener.LocIdListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UrlUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class EmployerManageActivity extends AppCompatActivity implements IEmployerManageActivity, View.OnClickListener, PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private CProgressDialog cpd;
    private RelativeLayout returnRl;
    private RelativeLayout allRl, waitRl, talkRl, doingRl, doneRl;
    private TextView allTv, waitTv, talkTv, doingTv, doneTv;
    private View popView;
    private PopupWindow pop;
    private EmployerManageAdapter employerManageAdapter;
    private List<EmployerManageBean> employerManageBeanList = new ArrayList<>();
    private final int ALL = 0, WAIT = 1, TALK = 2, DOING = 3, DONE = 4;
    private int curState = ALL, tarState = -1;
    private final int FIRST = 0, REFRESH = 1, LOAD = 2;
    private int STATE = FIRST;
    private IEmployerManagePresenter employerManagePresenter;
    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;
    private final int CANCEL_SUCCESS = 3;
    private final int CANCEL_FAILURE = 4;
    private int clickPosition = 0;

    private View cancelPopView;
    private PopupWindow cancelPop;
    private View endPopView;
    private PopupWindow endPop;
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
                        break;
                    case CANCEL_SUCCESS:
                        cpd.dismiss();
                        employerManageBeanList.remove(clickPosition);
                        notifyData();
                        break;
                    case CANCEL_FAILURE:
                        cpd.dismiss();
                        loadData();
                        notifyData();
                        break;
                    case 5:
                        cpd.dismiss();
                        STATE = FIRST;
                        employerManageBeanList.remove(clickPosition);
                        notifyData();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_employer_manage, null);
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
        if (employerManagePresenter != null) {
            employerManagePresenter.destroy();
            employerManagePresenter = null;
        }
    }

    private void initView() {
        initRootView();
        initEmptyView();
        initPopView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_employer_manage_return);
        allRl = (RelativeLayout) rootView.findViewById(R.id.rl_employer_manage_all);
        allTv = (TextView) rootView.findViewById(R.id.tv_employer_manage_all);
        waitRl = (RelativeLayout) rootView.findViewById(R.id.rl_employer_manage_wait);
        waitTv = (TextView) rootView.findViewById(R.id.tv_employer_manage_wait);
        talkRl = (RelativeLayout) rootView.findViewById(R.id.rl_employer_manage_talk);
        talkTv = (TextView) rootView.findViewById(R.id.tv_employer_manage_talk);
        doingRl = (RelativeLayout) rootView.findViewById(R.id.rl_employer_manage_doing);
        doingTv = (TextView) rootView.findViewById(R.id.tv_employer_manage_doing);
        doneRl = (RelativeLayout) rootView.findViewById(R.id.rl_employer_manage_done);
        doneTv = (TextView) rootView.findViewById(R.id.tv_employer_manage_done);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
        cpd = Utils.initProgressDialog(EmployerManageActivity.this, cpd);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(EmployerManageActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(EmployerManageActivity.this).inflate(R.layout.empty_net, null);
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
        popView = LayoutInflater.from(EmployerManageActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) popView.findViewById(R.id.tv_pop_dialog_0_content)).setText("此工作还没有工人联系\n是否主动邀约工人？");
        ((TextView) popView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) popView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        popView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
        popView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                    startActivity(new Intent(EmployerManageActivity.this, SkillActivity.class));
                }
            }
        });
        pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        cancelPopView = LayoutInflater.from(EmployerManageActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) cancelPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("是否取消发布？");
        ((TextView) cancelPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) cancelPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        cancelPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelPop.isShowing()) {
                    cancelPop.dismiss();
                }
            }
        });
        cancelPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelPop.isShowing()) {
                    cancelPop.dismiss();
                    cpd.show();
                    String waitCancelUrl = NetConfig.taskBaseUrl +
                            "?action=del" +
                            "&t_id=" + employerManageBeanList.get(clickPosition).getTaskId() +
                            "&t_author=" + UserUtils.readUserData(EmployerManageActivity.this).getId();
                    employerManagePresenter.cancel(waitCancelUrl);
                }
            }
        });
        cancelPop = new PopupWindow(cancelPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cancelPop.setFocusable(true);
        cancelPop.setTouchable(true);
        cancelPop.setOutsideTouchable(true);
        cancelPop.setBackgroundDrawable(new BitmapDrawable());
        cancelPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.light(EmployerManageActivity.this);
            }
        });

        deletePopView = LayoutInflater.from(EmployerManageActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) deletePopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("是否删除？");
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
                    cpd.show();
                    String doneDelUrl = NetConfig.taskBaseUrl +
                            "?action=del2" +
                            "&t_id=" + employerManageBeanList.get(clickPosition).getTaskId() +
                            "&t_author=" + UserUtils.readUserData(EmployerManageActivity.this).getId();
                    doneDel(doneDelUrl);
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
                Utils.light(EmployerManageActivity.this);
            }
        });
        endPopView = LayoutInflater.from(EmployerManageActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) endPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认完成？");
        ((TextView) endPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) endPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        endPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endPop.isShowing())
                    endPop.dismiss();
            }
        });
        endPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endPop.isShowing()) {
                    endPop.dismiss();
                    cpd.show();
                    String endUrl = NetConfig.taskBaseUrl +
                            "?action=editTask" +
                            "&t_id=" + employerManageBeanList.get(clickPosition).getTaskId() +
                            "&t_author=" + UserUtils.readUserData(EmployerManageActivity.this).getId();
                    Log.i("endUrl",endUrl);
                    end(endUrl);
                }
            }
        });

        endPop = new PopupWindow(endPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        endPop.setFocusable(true);
        endPop.setTouchable(true);
        endPop.setOutsideTouchable(true);
        endPop.setBackgroundDrawable(new BitmapDrawable());
        endPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.light(EmployerManageActivity.this);
            }
        });
    }

    private void initData() {
        employerManagePresenter = new EmployerManagePresenter(this);
        employerManageAdapter = new EmployerManageAdapter(EmployerManageActivity.this, employerManageBeanList, this);
    }

    private void setData() {
        plv.setAdapter(employerManageAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        allRl.setOnClickListener(this);
        waitRl.setOnClickListener(this);
        talkRl.setOnClickListener(this);
        doingRl.setOnClickListener(this);
        doneRl.setOnClickListener(this);
        ptrl.setOnRefreshListener(this);
    }

    private void loadData() {
        switch (STATE) {
            case FIRST:
                cpd.show();
                break;
        }
        employerManagePresenter.load(UrlUtils.getEmployerManageUrl(EmployerManageActivity.this, curState));
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                if (employerManageBeanList.size() == 0) {
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
        employerManageAdapter.notifyDataSetChanged();
    }

    private void refreshView() {
        if (tarState != curState) {
            switch (tarState) {
                case 0:
                    allTv.setTextColor(ColorConfig.red_ff3e50);
                    waitTv.setTextColor(ColorConfig.black_252323);
                    talkTv.setTextColor(ColorConfig.black_252323);
                    doingTv.setTextColor(ColorConfig.black_252323);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case 1:
                    allTv.setTextColor(ColorConfig.black_252323);
                    waitTv.setTextColor(ColorConfig.red_ff3e50);
                    talkTv.setTextColor(ColorConfig.black_252323);
                    doingTv.setTextColor(ColorConfig.black_252323);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case 2:
                    allTv.setTextColor(ColorConfig.black_252323);
                    waitTv.setTextColor(ColorConfig.black_252323);
                    talkTv.setTextColor(ColorConfig.red_ff3e50);
                    doingTv.setTextColor(ColorConfig.black_252323);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case 3:
                    allTv.setTextColor(ColorConfig.black_252323);
                    waitTv.setTextColor(ColorConfig.black_252323);
                    talkTv.setTextColor(ColorConfig.black_252323);
                    doingTv.setTextColor(ColorConfig.red_ff3e50);
                    doneTv.setTextColor(ColorConfig.black_252323);
                    break;
                case 4:
                    allTv.setTextColor(ColorConfig.black_252323);
                    waitTv.setTextColor(ColorConfig.black_252323);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_employer_manage_return:
                startActivity(new Intent(EmployerManageActivity.this, MainActivity.class));
                break;
            case R.id.rl_employer_manage_all:
                tarState = ALL;
                refreshView();
                break;
            case R.id.rl_employer_manage_wait:
                tarState = WAIT;
                refreshView();
                break;
            case R.id.rl_employer_manage_talk:
                tarState = TALK;
                refreshView();
                break;
            case R.id.rl_employer_manage_doing:
                tarState = DOING;
                refreshView();
                break;
            case R.id.rl_employer_manage_done:
                tarState = DONE;
                refreshView();
                break;
        }
    }

    @Override
    public void loadSuccess(String json) {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                employerManageBeanList.clear();
                break;
            case REFRESH:
                employerManageBeanList.clear();
                break;
            case LOAD:
                break;
        }
        employerManageBeanList.addAll(DataUtils.getEmployerManageBeanList(json));
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void loadFailure(String failure) {
        if (failure.equals(VarConfig.noNet)) {
            switch (STATE) {
                case FIRST:
                    cpd.dismiss();
                    ptrl.setVisibility(View.GONE);
                    netView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    break;
                case REFRESH:
                    ptrl.hideHeadView();
                    Utils.toast(EmployerManageActivity.this, StateConfig.loadNonet);
                    break;
            }
        }
    }

    @Override
    public void cancelSuccess(String json) {
        json = Utils.cutJson(json);
        try {
            JSONObject beanObj = new JSONObject(json);
            int code = beanObj.optInt("code");
            switch (code) {
                case 200:
                    handler.sendEmptyMessage(CANCEL_SUCCESS);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelFailure(String failure) {

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
    public void onClick(int id, int pos) {//适配器中控件点击事件
        clickPosition = pos;
        switch (id) {
            case R.id.ll_item_employer_manage:
//                String status = employerManageBeanList.get(clickPosition).getStatus();
//                if (status.equals("0")) {
//                    if (!pop.isShowing()) {
//                        backgroundAlpha(0.5f);
//                        pop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//                    }
//                } else {
//                    Intent doingIntent = new Intent(EmployerManageActivity.this, EmployerToDoingActivity.class);
//                    doingIntent.putExtra(IntentConfig.toEmployerToDoing, employerManageBeanList.get(clickPosition).getTaskId());
//                    startActivity(doingIntent);
//                }
                break;
            case R.id.tv_item_employer_manage_wait_cancel:
                if (!cancelPop.isShowing()) {
                    Utils.dark(EmployerManageActivity.this);
                    cancelPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_item_employer_manage_talk_cancel:
                if (!cancelPop.isShowing()) {
                    Utils.dark(EmployerManageActivity.this);
                    cancelPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_item_employer_manage_done_del:
                if (!deletePop.isShowing()) {
                    Utils.dark(EmployerManageActivity.this);
                    deletePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_item_employer_manage_details:
                Intent intent = new Intent(EmployerManageActivity.this, TalkEmployerActivity.class);
                ToTalkEmployerBean toTalkEmployerBean = new ToTalkEmployerBean();
                Log.i("getID",employerManageBeanList.get(clickPosition).getTaskId());
                toTalkEmployerBean.setT_id(employerManageBeanList.get(clickPosition).getTaskId());
                intent.putExtra(IntentConfig.toTalkEmployer, toTalkEmployerBean);
                startActivity(intent);
                break;
            case R.id.tv_item_employer_manage_end:
                if(!endPop.isShowing()){
                    Utils.dark(EmployerManageActivity.this);
                    endPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
        }
    }

    private void doneDel(String doneDelUrl) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(doneDelUrl).get().build();
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
                            if (beanObj.optString("data").equals("success")) {
                                handler.sendEmptyMessage(5);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void end(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i("ress",json);
                    try {
                        JSONObject beanObj = new JSONObject(json);
                        if (beanObj.optInt("code") == 200) {
                            if (beanObj.optString("data").equals("success")) {
                                handler.sendEmptyMessage(CANCEL_FAILURE);
                            } else {
                                handler.sendEmptyMessage(CANCEL_FAILURE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(EmployerManageActivity.this, MainActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
