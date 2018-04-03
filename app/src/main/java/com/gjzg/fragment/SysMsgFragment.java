package com.gjzg.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gjzg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.MessageAdapter;

import com.gjzg.bean.OfferBean;
import com.gjzg.singleton.SingleGson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.custom.PullToRefreshLayout;
import com.gjzg.custom.PullableListView;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class SysMsgFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener, PullToRefreshLayout.OnRefreshListener {

    private View rootView, netView, emptyView, msgPopView,delPopView;
    private TextView titleTv, timeTv, contentTv;
    private PopupWindow msgPop,delPop;
    private FrameLayout fl;
    private TextView netTv;
    private PullToRefreshLayout ptrl;
    private PullableListView plv;
    private CProgressDialog cProgressDialog;
    private List<OfferBean.DataBeanX.DataBean> mList;
    private MessageAdapter mAdapter;
    private int clickPosition;
    private final int FIRST = 1, REFRESH = 2, LOAD = 3;
    private int STATE;

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
        initPopView();
    }

    private void initRootView() {
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

    private void initPopView() {
        msgPopView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_msg, null);
        titleTv = (TextView) msgPopView.findViewById(R.id.tv_pop_msg_title);
        timeTv = (TextView) msgPopView.findViewById(R.id.tv_pop_msg_time);
        contentTv = (TextView) msgPopView.findViewById(R.id.tv_pop_msg_content);
        msgPop = new PopupWindow(msgPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        msgPop.setFocusable(true);
        msgPop.setTouchable(true);
        msgPop.setOutsideTouchable(true);
        msgPop.setBackgroundDrawable(new BitmapDrawable());
        msgPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        delPopView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_dialog_0, null);
        ((TextView) delPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认删除？");
        ((TextView) delPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        ((TextView) delPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        delPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delPop.isShowing())
                    delPop.dismiss();
            }
        });
        delPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delPop.isShowing()) {
                    delPop.dismiss();
                    delete();
                }
            }
        });
        delPop = new PopupWindow(delPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        delPop.setFocusable(true);
        delPop.setTouchable(true);
        delPop.setOutsideTouchable(true);
        delPop.setBackgroundDrawable(new BitmapDrawable());
        delPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    private void initData() {
        STATE = FIRST;
        mList = new ArrayList<>();
        mAdapter = new MessageAdapter(getActivity(), mList);
    }

    private void setData() {
        plv.setAdapter(mAdapter);
    }

    private void setListener() {
        ptrl.setOnRefreshListener(this);
        plv.setOnItemClickListener(this);
        plv.setOnItemClickListener(this);
    }

    private void loadData() {
        if (STATE == FIRST)
            cProgressDialog.show();
        String url = NetConfig.msgListUrl +
                "?u_id=" + UserUtils.readUserData(getActivity()).getId() +
                "&wm_type=0";
        OkHttpUtils.get().tag(this).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {
                notifyNet();
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    OfferBean offerBean = SingleGson.getInstance().fromJson(response, OfferBean.class);
                    if (offerBean != null) {
                        if (offerBean.getCode() == 1) {
                            if (offerBean.getData() != null) {
                                if (offerBean.getData().getData() != null) {
                                    switch (STATE) {
                                        case REFRESH:
                                            mList.clear();
                                            break;
                                        case LOAD:
                                            if (offerBean.getData().getData().size() == 0)
                                                Utils.toast(getActivity(), "到底了");
                                            break;
                                    }
                                    mList.addAll(offerBean.getData().getData());
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
            case LOAD:
                ptrl.hideFootView();
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
            case LOAD:
                ptrl.hideFootView();
                Utils.toast(getActivity(), VarConfig.noNetTip);
                break;
        }
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = f;
        getActivity().getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickPosition = position;
        OfferBean.DataBeanX.DataBean dataBean = mList.get(clickPosition);
        String status = dataBean.getUm_status();
        if (status == null || status.equals("null") || TextUtils.isEmpty(status)) {
        } else {
            if (status.equals("0")) {
                read();
            } else {
                pop();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clickPosition = position;
        backgroundAlpha(0.5f);
        delPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        return true;
    }

    private void read() {
        cProgressDialog.show();
        OfferBean.DataBeanX.DataBean dataBean = mList.get(clickPosition);
        String url = NetConfig.msgEditUrl +
                "?um_id=" + dataBean.getUm_id();
        OkHttpUtils.get().tag(this).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optInt("code") == 1) {
                            mList.get(clickPosition).setUm_status("1");
                            mAdapter.notifyDataSetChanged();
                            if (cProgressDialog.isShowing()) {
                                cProgressDialog.dismiss();
                            }
                            pop();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void pop() {
        OfferBean.DataBeanX.DataBean dataBean = mList.get(clickPosition);
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getWm_title())) {
                titleTv.setText(dataBean.getWm_title());
            }
            if (!TextUtils.isEmpty(dataBean.getUm_in_time())) {
                timeTv.setText(DataUtils.msgTimes(dataBean.getUm_in_time()));
            }
            String content = dataBean.getWm_desc();
            if (!TextUtils.isEmpty(dataBean.getWm_desc())) {
                contentTv.setText(content);
            }
            if (!msgPop.isShowing()) {
                backgroundAlpha(0.5f);
                msgPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
        }
    }

    private void delete() {
        String delUrl = NetConfig.msgDelUrl + "?um_id=" + mList.get(clickPosition).getUm_id();
        OkHttpUtils.get().tag(this).url(delUrl).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.optInt("code");
                        JSONObject dataObj = jsonObject.optJSONObject("data");
                        String msg = null;
                        if (dataObj != null) {
                            msg = dataObj.optString("msg");
                        }
                        if (!TextUtils.isEmpty(msg)) {
                            switch (code) {
                                case 0:
                                    break;
                                case 1:
                                    mList.remove(clickPosition);
                                    notifyData();
                                    break;
                            }
                            Utils.toast(getActivity(), msg);
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
        STATE = LOAD;
        loadData();
    }
}
