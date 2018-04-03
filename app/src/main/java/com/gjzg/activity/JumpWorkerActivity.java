package com.gjzg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.gjzg.R;
import com.gjzg.bean.AppConfigBean;
import com.gjzg.singleton.SingleGson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import com.gjzg.bean.ToChangePriceBean;
import com.gjzg.bean.ToComplainBean;
import com.gjzg.bean.ToFireBean;
import com.gjzg.bean.ToJumpWorkerBean;
import com.gjzg.bean.WorkerBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import complain.view.ComplainActivity;

import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CImageView;
import com.gjzg.custom.CProgressDialog;

public class JumpWorkerActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, complainRl;
    private CProgressDialog cpd;
    private MapView mapView;
    private CImageView iconIv, phoneIv;
    private ImageView sexIv, statusIv;
    private TextView topTitleTv, nameTv, skillNameTv, infoTv, addressTv;
    private LinearLayout surePriceLl, waitWorkerLl, fireWorkerLl;
    private final int SURE_PRICE = 1, WAIT_WORKER = 2, FIRE_WORKER = 3;
    private int SHOW_STATE;
    private ToJumpWorkerBean toJumpWorkerBean;
    private OkHttpClient okHttpClient;
    private WorkerBean workerBean;

    private TextView cancelWorkerTv, cancelWorker2Tv, surePriceTv, fireWorkerTv;
    private View cancelWorkerPopView;
    private PopupWindow cancelWorkerPop;
    private View surePricePopView;
    private PopupWindow surePricePop;
    private TextView surePricePriceTv, surePriceTimeTv, surePriceServiceCashTv;
    private View fireWorkerPopView;
    private PopupWindow fireWorkerPop;

    private float charge_rate;
    private String cancelWorkerTip, employerSureTip;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        notifyData();
                        break;
                    case 3:
                        cpd.dismiss();
                        if (!TextUtils.isEmpty(cancelWorkerTip)) {
                            if (cancelWorkerTip.equals("success")) {
                                startActivity(new Intent(JumpWorkerActivity.this, EmployerManageActivity.class));
                            } else if (cancelWorkerTip.equals("failure")) {
                                Utils.toast(JumpWorkerActivity.this, "失败");
                            } else {
                                Utils.toast(JumpWorkerActivity.this, cancelWorkerTip);
                            }
                        }
                        break;
                    case 4:
                        cpd.dismiss();
                        if (!TextUtils.isEmpty(employerSureTip)) {
                            if (employerSureTip.equals("success")) {
                                startActivity(new Intent(JumpWorkerActivity.this, EmployerManageActivity.class));
                            } else if (employerSureTip.equals("failure")) {
                                Utils.toast(JumpWorkerActivity.this, "失败");
                            } else {
                                Utils.toast(JumpWorkerActivity.this, employerSureTip);
                            }
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(JumpWorkerActivity.this).inflate(R.layout.activity_jump_worker, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        getConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (toJumpWorkerBean != null) {
            toJumpWorkerBean = null;
        }
        if (workerBean != null) {
            workerBean = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }

    private void initView() {
        initRootView();
        initPopView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_jump_worker_return);
        complainRl = (RelativeLayout) rootView.findViewById(R.id.rl_jump_worker_complain);
        mapView = (MapView) rootView.findViewById(R.id.mv_jump_worker);
        iconIv = (CImageView) rootView.findViewById(R.id.iv_jump_worker_icon);
        phoneIv = (CImageView) rootView.findViewById(R.id.iv_jump_worker_phone);
        sexIv = (ImageView) rootView.findViewById(R.id.iv_jump_worker_sex);
        statusIv = (ImageView) rootView.findViewById(R.id.iv_jump_worker_status);
        topTitleTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_top_title);
        nameTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_name);
        skillNameTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_skill);
        infoTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_info);
        addressTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_address);
        surePriceLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_talk_sure_price);
        waitWorkerLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_talk_wait_worker);
        fireWorkerLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_talk_fire_worker);
        cancelWorkerTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_cancel_worker);
        cancelWorker2Tv = (TextView) rootView.findViewById(R.id.tv_jump_worker_cancel_worker_2);
        surePriceTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_sure_price);
        fireWorkerTv = (TextView) rootView.findViewById(R.id.tv_jump_worker_fire_worker);
        cpd = Utils.initProgressDialog(JumpWorkerActivity.this, cpd);
    }

    private void initPopView() {
        cancelWorkerPopView = LayoutInflater.from(JumpWorkerActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) cancelWorkerPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认不想用该工人?");
        ((TextView) cancelWorkerPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) cancelWorkerPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        cancelWorkerPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelWorkerPop.isShowing()) {
                    cancelWorkerPop.dismiss();
                }
            }
        });
        cancelWorkerPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelWorkerPop.isShowing()) {
                    cancelWorkerPop.dismiss();
                    cancelWorker();
                }
            }
        });
        cancelWorkerPop = new PopupWindow(cancelWorkerPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cancelWorkerPop.setFocusable(true);
        cancelWorkerPop.setTouchable(true);
        cancelWorkerPop.setOutsideTouchable(true);
        cancelWorkerPop.setBackgroundDrawable(new BitmapDrawable());
        cancelWorkerPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        surePricePopView = LayoutInflater.from(JumpWorkerActivity.this).inflate(R.layout.pop_employer_sure, null);
        surePricePriceTv = (TextView) surePricePopView.findViewById(R.id.tv_pop_employer_sure_price);
        surePriceTimeTv = (TextView) surePricePopView.findViewById(R.id.tv_pop_employer_sure_time);
        surePriceServiceCashTv = (TextView) surePricePopView.findViewById(R.id.tv_pop_employer_sure_service_cash);
        surePricePopView.findViewById(R.id.tv_pop_sure_price_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (surePricePop.isShowing()) {
                    surePricePop.dismiss();
                    startActivity(new Intent(JumpWorkerActivity.this, EmployerManageActivity.class));
                }
            }
        });
        surePricePopView.findViewById(R.id.tv_pop_sure_price_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePrice();
            }
        });
        surePricePopView.findViewById(R.id.tv_pop_sure_price_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employerSure();
            }
        });
        String attention1 = "工作期间未发生解雇或辞职的状况，工作完成后，雇主确认工程结束，即算完工，系统将自动把雇主预付的工人工资结算给工人。如工期结束后，雇主未确认工程结束，系统将在工期结束后3日，把工人应得的工资结算给工人。在工资结算时，系统将收取工人相应的服务费，并把扣除服务费后的薪资转入到工人账户。";
        int attention1Start = attention1.indexOf("在工资结算时");
        int attention1Bend = attention1.length();
        SpannableStringBuilder attention1Style = new SpannableStringBuilder(attention1);
        attention1Style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3e50")), attention1Start, attention1Bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) surePricePopView.findViewById(R.id.tv_pop_employer_sure_attention_1)).setText(attention1Style);

        String attention2 = "如工作中出现纠纷，请双方先进行沟通，不要轻易的辞职或者解雇。如发生解雇工人的情况，请在完成当日工作后的第二日再与工人解除工作关系，解雇工人当日需要支付工人当日的工资。如发生工人辞职的情况，请工人在完成当日工作后第二天再点击我要辞职，辞职当日雇主不需要支付当日工人工资。";
        int attention2Start = attention2.indexOf("如发生解雇");
        int attention2Bend = attention2.length();
        SpannableStringBuilder attention2Style = new SpannableStringBuilder(attention2);
        attention2Style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3e50")), attention2Start, attention2Bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) surePricePopView.findViewById(R.id.tv_pop_employer_sure_attention_2)).setText(attention2Style);

        surePricePop = new PopupWindow(surePricePopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        surePricePop.setFocusable(true);
        surePricePop.setTouchable(true);
        surePricePop.setOutsideTouchable(true);
        surePricePop.setBackgroundDrawable(new BitmapDrawable());

        fireWorkerPopView = LayoutInflater.from(JumpWorkerActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) fireWorkerPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认解雇工人？");
        ((TextView) fireWorkerPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) fireWorkerPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        fireWorkerPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fireWorkerPop.isShowing()) {
                    fireWorkerPop.dismiss();
                }
            }
        });
        fireWorkerPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fireWorkerPop.isShowing()) {
                    fireWorkerPop.dismiss();
                    fireWorker();
                }
            }
        });
        fireWorkerPop = new PopupWindow(fireWorkerPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        fireWorkerPop.setFocusable(true);
        fireWorkerPop.setTouchable(true);
        fireWorkerPop.setOutsideTouchable(true);
        fireWorkerPop.setBackgroundDrawable(new BitmapDrawable());
        fireWorkerPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    private void initData() {
        toJumpWorkerBean = (ToJumpWorkerBean) getIntent().getSerializableExtra(IntentConfig.toJumpWorker);
        okHttpClient = new OkHttpClient();
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        complainRl.setOnClickListener(this);
        iconIv.setOnClickListener(this);
        phoneIv.setOnClickListener(this);
        cancelWorkerTv.setOnClickListener(this);
        cancelWorker2Tv.setOnClickListener(this);
        surePriceTv.setOnClickListener(this);
        fireWorkerTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_jump_worker_return:
                startActivity(new Intent(JumpWorkerActivity.this, EmployerManageActivity.class));
                break;
            case R.id.rl_jump_worker_complain:
                ToComplainBean toComplainBean = new ToComplainBean();
                toComplainBean.setAgainstId(workerBean.getU_id());
                toComplainBean.setCtType("2");
                toComplainBean.setSkill(toJumpWorkerBean.getS_name());
                Intent i = new Intent(JumpWorkerActivity.this, ComplainActivity.class);
                i.putExtra(IntentConfig.toComplain, toComplainBean);
                startActivity(i);
                break;
            case R.id.iv_jump_worker_icon:
                Intent intent = new Intent(JumpWorkerActivity.this, PersonDetailActivity.class);
                intent.putExtra(IntentConfig.talkToDetail, workerBean.getU_id());
                startActivity(intent);
                break;
            case R.id.tv_jump_worker_cancel_worker:
                if (!cancelWorkerPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    cancelWorkerPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_jump_worker_cancel_worker_2:
                if (!cancelWorkerPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    cancelWorkerPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_jump_worker_sure_price:
                if (!surePricePop.isShowing()) {
                    surePricePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_jump_worker_fire_worker:
                if (!fireWorkerPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    fireWorkerPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.iv_jump_worker_phone:
                Intent in = new Intent(Intent.ACTION_DIAL);
                in.setData(Uri.parse("tel:" + workerBean.getU_mobile()));
                if (in.resolveActivity(getPackageManager()) != null) {
                    startActivity(in);
                }
                break;
        }
    }

    private void getConfig() {
        cpd.show();
        OkHttpUtils.get().tag(this).url(NetConfig.appConfigUrl).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    AppConfigBean appConfigBean = SingleGson.getInstance().fromJson(response, AppConfigBean.class);
                    if (appConfigBean.getCode() == 1) {
                        if (appConfigBean.getData() != null) {
                            if (appConfigBean.getData().getData() != null) {
                                if (!TextUtils.isEmpty(appConfigBean.getData().getData().getCharge_rate())) {
                                    charge_rate = Float.parseFloat(appConfigBean.getData().getData().getCharge_rate());
                                    loadData();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadData() {
        String url = NetConfig.workerUrl + "?u_id=" + toJumpWorkerBean.getWorkerId() + "&fu_id=" + UserUtils.readUserData(JumpWorkerActivity.this).getId() + "&o_status=0,2";
        Log.e(this.getClass().getSimpleName(), "testUrl\n" + url);
        Request request = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    workerBean = DataUtils.getWorkerBeanList(response.body().string()).get(0);
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void notifyData() {
        cpd.dismiss();
        if (!TextUtils.isEmpty(toJumpWorkerBean.getTewPrice())) {
            surePricePriceTv.setText(toJumpWorkerBean.getTewPrice());
            surePriceServiceCashTv.setText("结算工资的时候系统会收取工人" + (charge_rate * 100) + "%的服务费");
        }
        if (!TextUtils.isEmpty(toJumpWorkerBean.getTew_start_time()) && !TextUtils.isEmpty(toJumpWorkerBean.getTew_end_time())) {
            surePriceTimeTv.setText(DataUtils.times((toJumpWorkerBean.getTew_start_time())) + "-" + DataUtils.times((toJumpWorkerBean.getTew_end_time())));
        }
        Picasso.with(JumpWorkerActivity.this).load(workerBean.getU_img()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        if (workerBean.getU_sex().equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (workerBean.getU_sex().equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
        if (workerBean.getU_task_status().equals("0")) {
            statusIv.setImageResource(R.mipmap.worker_leisure);
        } else if (workerBean.getU_task_status().equals("1")) {
            statusIv.setImageResource(R.mipmap.worker_mid);
        }
        nameTv.setText(workerBean.getU_true_name());
        skillNameTv.setText(toJumpWorkerBean.getS_name());
        infoTv.setText(workerBean.getUei_info());
        addressTv.setText(workerBean.getUei_address());
        map();
        if (workerBean.getRelation() == 0) {
            if (workerBean.getO_confirm().equals("-1")) {
                Utils.toast(JumpWorkerActivity.this, "工人已辞职");
            } else {
                Utils.toast(JumpWorkerActivity.this, "工人已取消订单");
            }
        } else {
            if (workerBean.getO_confirm().equals("1")) {
                SHOW_STATE = FIRE_WORKER;
                refreshState();
            } else {
                if (toJumpWorkerBean.getO_status().equals("0")) {
                    if (toJumpWorkerBean.getO_confirm().equals("0")) {
                        SHOW_STATE = SURE_PRICE;
                    } else if (toJumpWorkerBean.getO_confirm().equals("1")) {
                        SHOW_STATE = FIRE_WORKER;
                    } else if (toJumpWorkerBean.getO_confirm().equals("2")) {
                        SHOW_STATE = WAIT_WORKER;
                    }
                }
                refreshState();
            }
        }
    }

    private void map() {
        BaiduMap baiduMap = mapView.getMap();
        UiSettings settings = baiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setOverlookingGesturesEnabled(false);
        settings.setZoomGesturesEnabled(false);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.point_blue);
        LatLng point = new LatLng(Double.parseDouble(workerBean.getUcp_posit_y()), Double.parseDouble(workerBean.getUcp_posit_x()));
        OverlayOptions overlayOptions = new MarkerOptions().position(point).icon(bitmap);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(point, Float.parseFloat("15"));
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.addOverlay(overlayOptions);
    }

    private void refreshState() {
        switch (SHOW_STATE) {
            case SURE_PRICE:
                surePriceLl.setVisibility(View.VISIBLE);
                waitWorkerLl.setVisibility(View.GONE);
                fireWorkerLl.setVisibility(View.GONE);
                topTitleTv.setText("待工人就位");
                break;
            case WAIT_WORKER:
                surePriceLl.setVisibility(View.GONE);
                waitWorkerLl.setVisibility(View.VISIBLE);
                fireWorkerLl.setVisibility(View.GONE);
                topTitleTv.setText("待工人就位");
                break;
            case FIRE_WORKER:
                surePriceLl.setVisibility(View.GONE);
                waitWorkerLl.setVisibility(View.GONE);
                fireWorkerLl.setVisibility(View.VISIBLE);
                topTitleTv.setText("已开工");
                break;
        }
    }

    private void cancelWorker() {
        cpd.show();
        String url = NetConfig.orderUrl +
                "?action=cancel" +
                "&o_id=" + toJumpWorkerBean.getOrderId() +
                "&sponsor=" + UserUtils.readUserData(JumpWorkerActivity.this).getId() +
                "&o_confirm=0,2";
        Request cancelRequest = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(cancelRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.optInt("code") == 200) {
                            cancelWorkerTip = jsonObject.optString("data");
                            handler.sendEmptyMessage(3);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void changePrice() {
        ToChangePriceBean toChangePriceBean = new ToChangePriceBean();
        toChangePriceBean.setTew_id(toJumpWorkerBean.getTewId());
        toChangePriceBean.setT_id(toJumpWorkerBean.getTaskId());
        toChangePriceBean.setT_author(UserUtils.readUserData(JumpWorkerActivity.this).getId());
        toChangePriceBean.setAmount(toJumpWorkerBean.getTewPrice());
        toChangePriceBean.setWorker_num(toJumpWorkerBean.getTew_worker_num());
        toChangePriceBean.setStart_time(DataUtils.times(toJumpWorkerBean.getTew_start_time()));
        toChangePriceBean.setEnd_time(DataUtils.times(toJumpWorkerBean.getTew_end_time()));
        toChangePriceBean.setO_worker(toJumpWorkerBean.getWorkerId());
        toChangePriceBean.setSkill(toJumpWorkerBean.getS_name());
        Intent changePriceIntent = new Intent(JumpWorkerActivity.this, ChangePriceActivity.class);
        changePriceIntent.putExtra(IntentConfig.toChangePrice, toChangePriceBean);
        startActivity(changePriceIntent);
    }

    private void employerSure() {
        cpd.show();
        String employerSureUrl = NetConfig.orderUrl +
                "?action=employerConfirm" +
                "&o_id=" + toJumpWorkerBean.getOrderId() +
                "&t_id=" + toJumpWorkerBean.getTaskId() +
                "&u_id=" + UserUtils.readUserData(JumpWorkerActivity.this).getId();
        Request employerSureRequest = new Request.Builder().url(employerSureUrl).get().build();
        okHttpClient.newCall(employerSureRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.optInt("code") == 200) {
                            employerSureTip = jsonObject.optString("data");
                            handler.sendEmptyMessage(4);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void fireWorker() {
        ToFireBean toFireBean = new ToFireBean();
        toFireBean.setTewId(toJumpWorkerBean.getTewId());
        toFireBean.setFireId(toJumpWorkerBean.getWorkerId());
        toFireBean.setSkillName(toJumpWorkerBean.getS_name());
        toFireBean.setTaskId(toJumpWorkerBean.getTaskId());
        toFireBean.setSkillId(toJumpWorkerBean.getSkillId());
        toFireBean.setStart("0");
        toFireBean.setContent("");
        Intent fireIntent = new Intent(JumpWorkerActivity.this, FireActivity.class);
        fireIntent.putExtra(IntentConfig.toFire, toFireBean);
        startActivity(fireIntent);
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().setAttributes(layoutParams);
    }
}
