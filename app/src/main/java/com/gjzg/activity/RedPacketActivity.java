package com.gjzg.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.adapter.RedPacketAdapter;
import com.gjzg.bean.RedPacketBean;
import com.gjzg.listener.IdPosClickHelp;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import redpacket.presenter.IRedPacketPresenter;
import redpacket.presenter.RedPacketPresenter;
import redpacket.view.IRedPacketActivity;

import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class RedPacketActivity extends AppCompatActivity implements IRedPacketActivity, View.OnClickListener, PullToRefreshLayout.OnRefreshListener, IdPosClickHelp {

    private View rootView, emptyView, netView;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private RelativeLayout returnRl;
    private CProgressDialog cpd;
    private List<RedPacketBean> redPacketBeanList = new ArrayList<>();
    private RedPacketAdapter redPacketAdapter;
    private IRedPacketPresenter redPacketPresenter;

    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;
    private final int FIRST = 3;
    private final int REFRESH = 4;
    private int STATE = FIRST;
    private int clickPosition;

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
                        redPacketBeanList.get(clickPosition).setBd_use_time("1");
                        notifyData();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(RedPacketActivity.this).inflate(R.layout.activity_red_packet, null);
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
        if (redPacketPresenter != null) {
            redPacketPresenter.destroy();
            redPacketPresenter = null;
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
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_red_packet_return);
        ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.ptrl);
        plv = (PullableListView) rootView.findViewById(R.id.plv);
        cpd = Utils.initProgressDialog(this, cpd);
    }

    private void initEmptyView() {
        fl = (FrameLayout) rootView.findViewById(R.id.fl);
        emptyView = LayoutInflater.from(RedPacketActivity.this).inflate(R.layout.empty_data, null);
        fl.addView(emptyView);
        emptyView.setVisibility(View.GONE);
        netView = LayoutInflater.from(RedPacketActivity.this).inflate(R.layout.empty_net, null);
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
        redPacketPresenter = new RedPacketPresenter(this);
        redPacketAdapter = new RedPacketAdapter(this, redPacketBeanList, this);
    }

    private void setData() {
        plv.setAdapter(redPacketAdapter);
    }

    private void setListener() {
        ptrl.setOnRefreshListener(this);
        returnRl.setOnClickListener(this);
    }

    private void loadData() {
        if (STATE == FIRST)
            cpd.show();
        String url = NetConfig.redBagUrl +
                "?action=list" +
                "&uid=" + UserUtils.readUserData(RedPacketActivity.this).getId() +
                "&bt_withdraw=1";
        redPacketPresenter.load(url);
    }

    private void notifyData() {
        switch (STATE) {
            case FIRST:
                cpd.dismiss();
                if (redPacketBeanList.size() == 0) {
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
        redPacketAdapter.notifyDataSetChanged();
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
                Utils.toast(RedPacketActivity.this, VarConfig.noNetTip);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_red_packet_return:
                finish();
                break;
        }
    }

    @Override
    public void loadSuccess(String json) {
        switch (STATE) {
            case REFRESH:
                redPacketBeanList.clear();
                break;
        }
        redPacketBeanList.addAll(DataUtils.getRedPacketBeanList(json));
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
            case R.id.tv_item_red_packet_status:
                String receiveUrl = NetConfig.redBagUrl +
                        "?action=using" +
                        "&bd_id=" + redPacketBeanList.get(clickPosition).getBd_id() +
                        "&uid=" + UserUtils.readUserData(RedPacketActivity.this).getId();
                receive(receiveUrl);
                break;
        }
    }

    private void receive(String url) {
        cpd.show();
        Request request = new Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
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
                                handler.sendEmptyMessage(666);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
