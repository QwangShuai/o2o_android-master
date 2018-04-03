package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.adapter.SkillAdapter;
import com.gjzg.bean.Skill;
import com.gjzg.bean.ToWorkerBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.singleton.SingleGson;
import com.gjzg.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class SkillActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private RelativeLayout returnRl;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private CProgressDialog cProgressDialog;
    private List<Skill.DataBean> mList;
    private SkillAdapter mAdapter;
    private final int FIRST = 3, REFRESH = 4;
    private int STATE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_skills, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        initRootView();
        initEmptyView();
        initDialogView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_skills_return);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(SkillActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(SkillActivity.this).inflate(R.layout.empty_net, null);
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
        cProgressDialog = new CProgressDialog(SkillActivity.this, R.style.dialog_cprogress);
    }

    private void initData() {
        STATE = FIRST;
        mList = new ArrayList<>();
        mAdapter = new SkillAdapter(this, mList);
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
        plv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList != null && mList.size() != 0) {
                    ToWorkerBean toWorkerBean = new ToWorkerBean();
                    toWorkerBean.setS_id(mList.get(position).getS_id());
                    toWorkerBean.setS_name(mList.get(position).getS_name());
                    Intent intent = new Intent(SkillActivity.this, WorkerActivity.class);
                    intent.putExtra(IntentConfig.toWorker, toWorkerBean);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadData() {
        if (STATE == FIRST)
            cProgressDialog.show();
        OkHttpUtils.get().tag(this).url(NetConfig.skillUrl).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        notifyNet();
                    }

                    @Override
                    public void onResponse(String response) {
                        Skill skill = SingleGson.getInstance().fromJson(response, Skill.class);
                        if (skill != null && skill.getData() != null) {
                            if (STATE == REFRESH)
                                mList.clear();
                            mList.addAll(skill.getData());
                            notifyData();
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
                Utils.toast(SkillActivity.this, VarConfig.noNetTip);
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
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
