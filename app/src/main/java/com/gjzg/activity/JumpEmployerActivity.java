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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.bean.JumpEmployerBean;
import com.gjzg.bean.ToComplainBean;
import com.gjzg.bean.ToEvaluateBean;
import com.gjzg.bean.ToJumpEmployerBean;
import com.gjzg.bean.ToResignBean;

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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

public class JumpEmployerActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, complainRl;

    private MapView mapView;
    private CImageView iconIv, phoneIv;
    private ImageView sexIv;
    private TextView nameTv, skillCountTv, priceTv, addressTv, timeTv;

    private View cancelPopView;
    private PopupWindow cancelPop;

    private View beginDoView;
    private PopupWindow beginDoPop;
    private TextView beginDoPriceTv, beginDoTimeTv, beginDoServiceCashTv;

    private View resignPopView;
    private PopupWindow resignPop;

    private LinearLayout waitEmployerLl, sureDoLl, resignLl, evaluateLl;
    private TextView waitEmployerCancelTv, sureDoCancelTv, sureDoDoTv, resignTv, evaluateTv;
    private final int WAIT_EMPLOYER = 1, SURE_DO = 2, RESIGN = 3, EVALUATE = 4;
    private int SHOW_STATE;

    private ToJumpEmployerBean toJumpEmployerBean;
    private OkHttpClient okHttpClient;
    private JumpEmployerBean jumpEmployerBean;

    private String beginDoTip;

    private float charge_rate;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        loadSkill();
                        break;
                    case 2:
                        notifyData();
                        break;
                    case 3:
                        if (!TextUtils.isEmpty(beginDoTip)) {
                            if (beginDoTip.equals("success")) {
                                startActivity(new Intent(JumpEmployerActivity.this, WorkerManageActivity.class));
                            } else if (beginDoTip.equals("failure")) {
                                Utils.toast(JumpEmployerActivity.this, "失败");
                            } else {
                                Utils.toast(JumpEmployerActivity.this, beginDoTip);
                            }
                        }
                        break;
                    case 4:
                        startActivity(new Intent(JumpEmployerActivity.this, WorkerManageActivity.class));
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(JumpEmployerActivity.this).inflate(R.layout.activity_jump_employer, null);
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
    }

    private void initView() {
        initRootView();
        initPopView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_jump_employer_return);
        complainRl = (RelativeLayout) rootView.findViewById(R.id.rl_jump_employer_complain);

        mapView = (MapView) rootView.findViewById(R.id.mv_jump_employer);
        iconIv = (CImageView) rootView.findViewById(R.id.iv_jump_employer_icon);
        phoneIv = (CImageView) rootView.findViewById(R.id.iv_jump_employer_phone);
        sexIv = (ImageView) rootView.findViewById(R.id.iv_jump_employer_sex);
        nameTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_name);
        skillCountTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_skill_count);
        priceTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_price);
        addressTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_address);
        timeTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_time);

        waitEmployerLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_employer_wait_employer);
        sureDoLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_employer_sure_do);
        resignLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_employer_resign);
        evaluateLl = (LinearLayout) rootView.findViewById(R.id.ll_jump_employer_evaluate);
        waitEmployerCancelTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_wait_employer_cancel);
        sureDoCancelTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_sure_do_cancel);
        sureDoDoTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_sure_do_do);
        resignTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_resign);
        evaluateTv = (TextView) rootView.findViewById(R.id.tv_jump_employer_evaluate);
    }

    private void initPopView() {
        cancelPopView = LayoutInflater.from(JumpEmployerActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) cancelPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认不想接该工作？");
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
                    cancel();
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
                backgroundAlpha(1.0f);
            }
        });

        beginDoView = LayoutInflater.from(JumpEmployerActivity.this).inflate(R.layout.pop_worker_sure, null);
        beginDoPriceTv = (TextView) beginDoView.findViewById(R.id.tv_pop_worker_sure_price);
        beginDoTimeTv = (TextView) beginDoView.findViewById(R.id.tv_pop_worker_sure_time);
        beginDoServiceCashTv = (TextView) beginDoView.findViewById(R.id.tv_pop_worker_sure_service_cash);

        beginDoView.findViewById(R.id.tv_pop_worker_sure_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beginDoPop.isShowing()) {
                    beginDoPop.dismiss();
                    startActivity(new Intent(JumpEmployerActivity.this, WorkerManageActivity.class));
                }
            }
        });
        beginDoView.findViewById(R.id.tv_pop_worker_sure_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beginDoPop.isShowing()) {
                    beginDoPop.dismiss();
                    beginDo();
                }
            }
        });

        String attention1 = "工作期间未发生解雇或辞职的状况，工作完成后，雇主确认工程结束，即算完工，系统将自动把雇主预付的工人工资结算给工人。如工期结束后，雇主未确认工程结束，系统将在工期结束后3日，把工人应得的工资结算给工人。在工资结算时，系统将收取工人相应的服务费，并把扣除服务费后的薪资转入到工人账户。";
        int attention1Start = attention1.indexOf("在工资结算时");
        int attention1Bend = attention1.length();
        SpannableStringBuilder attention1Style = new SpannableStringBuilder(attention1);
        attention1Style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3e50")), attention1Start, attention1Bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) beginDoView.findViewById(R.id.tv_pop_worker_sure_attention_1)).setText(attention1Style);

        String attention2 = "如工作中出现纠纷，请双方先进行沟通，不要轻易的辞职或者解雇。如发生解雇工人的情况，请在完成当日工作后的第二日再与工人解除工作关系，解雇工人当日需要支付工人当日的工资。如发生工人辞职的情况，请工人在完成当日工作后第二天再点击我要辞职，辞职当日雇主不需要支付当日工人工资。";
        int attention2Start = attention2.indexOf("如发生解雇");
        int attention2Bend = attention2.length();
        SpannableStringBuilder attention2Style = new SpannableStringBuilder(attention2);
        attention2Style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3e50")), attention2Start, attention2Bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ((TextView) beginDoView.findViewById(R.id.tv_pop_worker_sure_attention_2)).setText(attention2Style);

        beginDoPop = new PopupWindow(beginDoView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        resignPopView = LayoutInflater.from(JumpEmployerActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) resignPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("请与雇主先沟通再进行此项操作\n当日辞职，雇主不需要支付您当日工资\n建议今日工作完毕后，明天再辞职");
        ((TextView) resignPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) resignPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        resignPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resignPop.isShowing()) {
                    resignPop.dismiss();
                }
            }
        });
        resignPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resignPop.isShowing()) {
                    resignPop.dismiss();
                    resign();
                }
            }
        });
        resignPop = new PopupWindow(resignPopView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void initData() {
        toJumpEmployerBean = (ToJumpEmployerBean) getIntent().getSerializableExtra(IntentConfig.toJumpEmployer);
        okHttpClient = new OkHttpClient();
        jumpEmployerBean = new JumpEmployerBean();
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        complainRl.setOnClickListener(this);
        iconIv.setOnClickListener(this);
        phoneIv.setOnClickListener(this);
        waitEmployerCancelTv.setOnClickListener(this);
        sureDoCancelTv.setOnClickListener(this);
        sureDoDoTv.setOnClickListener(this);
        resignTv.setOnClickListener(this);
        evaluateTv.setOnClickListener(this);
    }

    private void loadData() {
        String url = NetConfig.taskBaseUrl +
                "?action=info&t_id=" + toJumpEmployerBean.getTaskId() +
                "&o_worker=" + UserUtils.readUserData(JumpEmployerActivity.this).getId();
        Log.e(this.getClass().getSimpleName(), "testUrl\n" + url);
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
                        int code = beanObj.optInt("code");
                        switch (code) {
                            case 200:
                                JSONObject dataObj = beanObj.optJSONObject("data");
                                if (dataObj != null) {
                                    jumpEmployerBean.setPosX(dataObj.optString("t_posit_x"));
                                    jumpEmployerBean.setPosY(dataObj.optString("t_posit_y"));
                                    jumpEmployerBean.setIcon(dataObj.optString("u_img"));
                                    jumpEmployerBean.setName(dataObj.optString("u_true_name"));
                                    jumpEmployerBean.setSex(dataObj.optString("u_sex"));
                                    jumpEmployerBean.setMobile(dataObj.optString("u_mobile"));
                                    jumpEmployerBean.setAuthorId(dataObj.optString("t_author"));
                                    jumpEmployerBean.setRelation(dataObj.optString("relation"));
                                    jumpEmployerBean.setResult(dataObj.optString("result"));
                                    JSONArray workerArr = dataObj.optJSONArray("t_workers");
                                    if (workerArr != null) {
                                        for (int i = 0; i < workerArr.length(); i++) {
                                            JSONObject workerObj = workerArr.optJSONObject(i);
                                            if (workerObj != null) {
                                                JSONArray orderArr = workerObj.optJSONArray("orders");
                                                if (orderArr != null) {
                                                    for (int j = 0; j < orderArr.length(); j++) {
                                                        JSONObject o = orderArr.optJSONObject(j);
                                                        if (o != null) {
                                                            if (o.optString("o_worker").equals(UserUtils.readUserData(JumpEmployerActivity.this).getId())) {
                                                                jumpEmployerBean.setSkillId(workerObj.optString("tew_skills"));
                                                                jumpEmployerBean.setCount(workerObj.optString("tew_worker_num"));
                                                                jumpEmployerBean.setStartTime(workerObj.optString("tew_start_time"));
                                                                jumpEmployerBean.setEndTime(workerObj.optString("tew_end_time"));
                                                                jumpEmployerBean.setAddress(workerObj.optString("tew_address"));
                                                                jumpEmployerBean.setO_status(o.optString("o_status"));
                                                                jumpEmployerBean.setO_confirm(o.optString("o_confirm"));
                                                                jumpEmployerBean.setOrderId(o.optString("o_id"));
                                                                jumpEmployerBean.setTewId(o.optString("tew_id"));
                                                                jumpEmployerBean.setTaskId(o.optString("t_id"));
                                                                jumpEmployerBean.setPrice(o.optString("o_amount"));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                handler.sendEmptyMessage(1);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getConfig() {
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

    private void loadSkill() {
        String url = NetConfig.skillUrl;
        Request request = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    jumpEmployerBean.setSkillName(DataUtils.getSkillName(json, jumpEmployerBean.getSkillId()));
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void cancel() {
        String url = NetConfig.orderUrl +
                "?action=cancel" +
                "&o_id=" + jumpEmployerBean.getOrderId() +
                "&sponsor=" + UserUtils.readUserData(JumpEmployerActivity.this).getId() + "" +
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
                            String data = jsonObject.optString("data");
                            if (!TextUtils.isEmpty(data)) {
                                if (data.equals("success")) {
                                    handler.sendEmptyMessage(4);
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

    private void beginDo() {
        String url = NetConfig.orderUrl + "?action=workerConfirm&o_id=" + jumpEmployerBean.getOrderId() + "&t_id=" + jumpEmployerBean.getTaskId() + "&o_worker=" + UserUtils.readUserData(JumpEmployerActivity.this).getId();
        Request beginRequest = new Request.Builder().url(url).get().build();
        okHttpClient.newCall(beginRequest).enqueue(new Callback() {
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
                            beginDoTip = jsonObject.optString("data");
                            handler.sendEmptyMessage(3);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void resign() {
        ToResignBean toResignBean = new ToResignBean();
        toResignBean.setTewId(jumpEmployerBean.getTewId());
        toResignBean.setTaskId(jumpEmployerBean.getTaskId());
        toResignBean.setAuthorId(jumpEmployerBean.getAuthorId());
        toResignBean.setSkillId(jumpEmployerBean.getSkillId());
        Intent resignIntent = new Intent(JumpEmployerActivity.this, ResignActivity.class);
        resignIntent.putExtra(IntentConfig.toResign, toResignBean);
        startActivity(resignIntent);
    }

    private void notifyData() {
        if (!TextUtils.isEmpty(jumpEmployerBean.getName())) {
            nameTv.setText(jumpEmployerBean.getName());
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getPrice())) {
            beginDoPriceTv.setText(jumpEmployerBean.getPrice());
            beginDoServiceCashTv.setText("结算工资的时候系统会收取工人" + (charge_rate * 100) + "%的服务费");
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getStartTime()) && !TextUtils.isEmpty(jumpEmployerBean.getEndTime())) {
            beginDoTimeTv.setText(DataUtils.times(jumpEmployerBean.getStartTime()) + "-" + DataUtils.times(jumpEmployerBean.getEndTime()));
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getIcon())) {
            Picasso.with(JumpEmployerActivity.this).load(jumpEmployerBean.getIcon()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        }
        if (jumpEmployerBean.getSex().equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (jumpEmployerBean.getSex().equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
        if (TextUtils.isEmpty(jumpEmployerBean.getName())) {
            nameTv.setText(jumpEmployerBean.getName());
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getSkillName()) && !TextUtils.isEmpty(jumpEmployerBean.getCount())) {
            skillCountTv.setText("招" + jumpEmployerBean.getSkillName() + jumpEmployerBean.getCount() + "人");
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getPrice())) {
            priceTv.setText("工资" + jumpEmployerBean.getPrice() + "元/天");
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getAddress())) {
            addressTv.setText(jumpEmployerBean.getAddress());
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getStartTime()) && !TextUtils.isEmpty(jumpEmployerBean.getEndTime())) {
            timeTv.setText("工期" + DataUtils.times(jumpEmployerBean.getStartTime()) + "——" + DataUtils.times(jumpEmployerBean.getEndTime()));
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getPosX()) && !TextUtils.isEmpty(jumpEmployerBean.getPosY())) {
            map();
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getO_status()) && !TextUtils.isEmpty(jumpEmployerBean.getO_confirm())) {
            String o_status = jumpEmployerBean.getO_status();
            String o_confirm = jumpEmployerBean.getO_confirm();
            if (o_status.equals("0")) {
                if (o_confirm.equals("0")) {
                    SHOW_STATE = WAIT_EMPLOYER;
                } else if (o_confirm.equals("1")) {
                    SHOW_STATE = RESIGN;
                } else if (o_confirm.equals("2")) {
                    SHOW_STATE = SURE_DO;
                }
                refreshState();
            } else if (o_status.equals("1")) {
                SHOW_STATE = EVALUATE;
                refreshState();
            } else if (o_status.equals("-2")) {
                Utils.toast(JumpEmployerActivity.this, "对方已将你解雇");
            }
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getRelation())) {
            if (jumpEmployerBean.getRelation().equals("0"))
                Utils.toast(JumpEmployerActivity.this, "对方已取消订单");
        }
        if (!TextUtils.isEmpty(jumpEmployerBean.getResult())) {
            if (jumpEmployerBean.getResult().equals("0"))
                Utils.toast(JumpEmployerActivity.this, "对方已取消订单");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_jump_employer_return:
                finish();
                break;
            case R.id.rl_jump_employer_complain:
                ToComplainBean toComplainBean = new ToComplainBean();
                toComplainBean.setAuthorId(UserUtils.readUserData(JumpEmployerActivity.this).getId());
                toComplainBean.setAgainstId(jumpEmployerBean.getAuthorId());
                toComplainBean.setCtType("1");
                toComplainBean.setContent("");
                toComplainBean.setSkill("");
                Intent intent = new Intent(JumpEmployerActivity.this, ComplainActivity.class);
                intent.putExtra(IntentConfig.toComplain, toComplainBean);
                startActivity(intent);
                break;
            case R.id.iv_jump_employer_icon:
                Intent i = new Intent(JumpEmployerActivity.this, PersonDetailActivity.class);
                i.putExtra(IntentConfig.talkToDetail, jumpEmployerBean.getAuthorId());
                startActivity(i);
                break;
            case R.id.iv_jump_employer_phone:
                Intent in = new Intent(Intent.ACTION_DIAL);
                in.setData(Uri.parse("tel:" + jumpEmployerBean.getMobile()));
                if (in.resolveActivity(getPackageManager()) != null) {
                    startActivity(in);
                }
                break;
            case R.id.tv_jump_employer_wait_employer_cancel:
                if (!cancelPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    cancelPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_jump_employer_sure_do_cancel:
                if (!cancelPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    cancelPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_jump_employer_sure_do_do:
                if (!beginDoPop.isShowing()) {
                    beginDoPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_jump_employer_resign:
                resign();
                break;
            case R.id.tv_jump_employer_evaluate:
                ToEvaluateBean toEvaluateBean = new ToEvaluateBean();
                toEvaluateBean.setTc_type("1");
                toEvaluateBean.setTc_start("0");
                toEvaluateBean.setT_id(jumpEmployerBean.getTaskId());
                toEvaluateBean.setU_id(UserUtils.readUserData(JumpEmployerActivity.this).getId());
                toEvaluateBean.setTc_u_id(jumpEmployerBean.getAuthorId());
                toEvaluateBean.setTce_desc("");
                toEvaluateBean.setSkill("");
                Intent evaluateIntent = new Intent(JumpEmployerActivity.this, EvaluateActivity.class);
                evaluateIntent.putExtra(IntentConfig.toEvaluate, toEvaluateBean);
                startActivity(evaluateIntent);
                break;
        }
    }

    private void map() {
        BaiduMap baiduMap = mapView.getMap();
        UiSettings settings = baiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setOverlookingGesturesEnabled(false);
        settings.setZoomGesturesEnabled(false);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.point_blue);
        LatLng point = new LatLng(Double.parseDouble(jumpEmployerBean.getPosY()), Double.parseDouble(jumpEmployerBean.getPosX()));
        OverlayOptions overlayOptions = new MarkerOptions().position(point).icon(bitmap);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(point, Float.parseFloat("15"));
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.addOverlay(overlayOptions);
    }

    private void refreshState() {
        switch (SHOW_STATE) {
            case WAIT_EMPLOYER:
                waitEmployerLl.setVisibility(View.VISIBLE);
                sureDoLl.setVisibility(View.GONE);
                resignLl.setVisibility(View.GONE);
                evaluateLl.setVisibility(View.GONE);
                break;
            case SURE_DO:
                waitEmployerLl.setVisibility(View.GONE);
                sureDoLl.setVisibility(View.VISIBLE);
                resignLl.setVisibility(View.GONE);
                evaluateLl.setVisibility(View.GONE);
                break;
            case RESIGN:
                waitEmployerLl.setVisibility(View.GONE);
                sureDoLl.setVisibility(View.GONE);
                resignLl.setVisibility(View.VISIBLE);
                evaluateLl.setVisibility(View.GONE);
                break;
            case EVALUATE:
                waitEmployerLl.setVisibility(View.GONE);
                sureDoLl.setVisibility(View.GONE);
                resignLl.setVisibility(View.GONE);
                evaluateLl.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().setAttributes(layoutParams);
    }
}
