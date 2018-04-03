package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.bean.ToComplainBean;
import com.gjzg.bean.ToEvaluateBean;
import com.gjzg.bean.UserInfoBean;

import complain.view.ComplainActivity;

import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class EvaluateActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private CProgressDialog cpd;
    private RelativeLayout returnRl;
    private TextView nameTv, identityTv, countTv, complainTv, submitTv;
    private ImageView iconIv, sexIv, praise1Iv, praise2Iv, praise3Iv;
    private EditText desEt;
    private ToEvaluateBean toEvaluateBean;
    private UserInfoBean userInfoBean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        notifyData();
                        break;
                    case 2:
                        cpd.dismiss();
                        Utils.toast(EvaluateActivity.this, "评论成功");
                        if (toEvaluateBean.getTc_type().equals("0")) {
                            startActivity(new Intent(EvaluateActivity.this, EmployerManageActivity.class));
                        } else if (toEvaluateBean.getTc_type().equals("1")) {
                            startActivity(new Intent(EvaluateActivity.this, WorkerManageActivity.class));
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_evaluate, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    private void initView() {
        initRootView();
        initDialogView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_evaluate_return);
        nameTv = (TextView) rootView.findViewById(R.id.tv_evaluate_name);
        identityTv = (TextView) rootView.findViewById(R.id.tv_evaluate_skill);
        countTv = (TextView) rootView.findViewById(R.id.tv_evaluate_count);
        complainTv = (TextView) rootView.findViewById(R.id.tv_evaluate_complain);
        iconIv = (ImageView) rootView.findViewById(R.id.iv_evaluate_icon);
        sexIv = (ImageView) rootView.findViewById(R.id.iv_evaluate_sex);
        praise1Iv = (ImageView) rootView.findViewById(R.id.iv_evaluate_praise_1);
        praise2Iv = (ImageView) rootView.findViewById(R.id.iv_evaluate_praise_2);
        praise3Iv = (ImageView) rootView.findViewById(R.id.iv_evaluate_praise_3);
        desEt = (EditText) rootView.findViewById(R.id.et_evaluate_des);
        desEt = (EditText) rootView.findViewById(R.id.et_evaluate_des);
        submitTv = (TextView) rootView.findViewById(R.id.tv_evaluate_submit);
    }

    private void initDialogView() {
        cpd = new CProgressDialog(this, R.style.dialog_cprogress);
    }

    private void initData() {
        toEvaluateBean = (ToEvaluateBean) getIntent().getSerializableExtra(IntentConfig.toEvaluate);
        String tc_type = toEvaluateBean.getTc_type();
        if (tc_type.equals("0")) {
            complainTv.setText("投诉\n工人");
        } else if (tc_type.equals("1")) {
            complainTv.setText("投诉\n雇主");
        }
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        complainTv.setOnClickListener(this);
        praise1Iv.setOnClickListener(this);
        praise2Iv.setOnClickListener(this);
        praise3Iv.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        complainTv.setOnClickListener(this);
        desEt.addTextChangedListener(contentTw);
    }

    private void loadData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(NetConfig.userInfoUrl + toEvaluateBean.getTc_u_id()).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    userInfoBean = DataUtils.getUserInfoBean(json);
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void notifyData() {
        Picasso.with(EvaluateActivity.this).load(userInfoBean.getU_img()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        String sex = userInfoBean.getU_sex();
        if (sex.equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (sex.equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
        nameTv.setText(userInfoBean.getU_true_name());
        if (toEvaluateBean.getTc_type().equals("0")) {
            identityTv.setText(toEvaluateBean.getSkill());
        } else if (toEvaluateBean.getTc_type().equals("1")) {
            identityTv.setText("雇主");
        }
        countTv.setText("好评" + userInfoBean.getU_high_opinions() + "次");
    }

    private void submit() {
        if (!TextUtils.isEmpty(toEvaluateBean.getTce_desc())) {
            cpd.show();
            String submitUrl = NetConfig.commentAddUrl;
            RequestBody submitBody = new FormBody.Builder()
                    .add("u_id", toEvaluateBean.getU_id())
                    .add("t_id", toEvaluateBean.getT_id())
                    .add("tc_u_id", toEvaluateBean.getTc_u_id())
                    .add("tce_desc", toEvaluateBean.getTce_desc())
                    .add("tc_start", toEvaluateBean.getTc_start())
                    .add("tc_type", toEvaluateBean.getTc_type())
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(submitUrl)
                    .post(submitBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        json = Utils.cutJson(json);
                        try {
                            JSONObject beanObj = new JSONObject(json);
                            if (beanObj.optInt("code") == 1) {
                                handler.sendEmptyMessage(2);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Utils.toast(EvaluateActivity.this, "请填写评价");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_evaluate_return:
                finish();
                break;
            case R.id.tv_evaluate_complain:
                ToComplainBean toComplainBean = new ToComplainBean();
                if (toEvaluateBean.getTc_type().equals("0")) {
                    toComplainBean.setAuthorId(UserUtils.readUserData(EvaluateActivity.this).getId());
                    toComplainBean.setAgainstId(userInfoBean.getU_id());
                    toComplainBean.setContent("");
                    toComplainBean.setCtType("2");
                    toComplainBean.setSkill("");
                } else if (toEvaluateBean.getTc_type().equals("1")) {
                    toComplainBean.setAuthorId(UserUtils.readUserData(EvaluateActivity.this).getId());
                    toComplainBean.setAgainstId(userInfoBean.getU_id());
                    toComplainBean.setContent("");
                    toComplainBean.setCtType("1");
                    toComplainBean.setSkill("");
                }
                Intent intent = new Intent(EvaluateActivity.this, ComplainActivity.class);
                intent.putExtra(IntentConfig.toComplain, toComplainBean);
                startActivity(intent);
                break;
            case R.id.iv_evaluate_praise_1:
                toEvaluateBean.setTc_start("1");
                changePraise(toEvaluateBean.getTc_start());
                break;
            case R.id.iv_evaluate_praise_2:
                toEvaluateBean.setTc_start("2");
                changePraise(toEvaluateBean.getTc_start());
                break;
            case R.id.iv_evaluate_praise_3:
                toEvaluateBean.setTc_start("3");
                changePraise(toEvaluateBean.getTc_start());
                break;
            case R.id.tv_evaluate_submit:
                toEvaluateBean.setU_id(UserUtils.readUserData(EvaluateActivity.this).getId());
                submit();
                break;
        }
    }

    private void changePraise(String praise) {
        switch (praise) {
            case "1":
                praise1Iv.setImageResource(R.mipmap.praise_yes);
                praise2Iv.setImageResource(R.mipmap.praise_no);
                praise3Iv.setImageResource(R.mipmap.praise_no);
                break;
            case "2":
                praise1Iv.setImageResource(R.mipmap.praise_yes);
                praise2Iv.setImageResource(R.mipmap.praise_yes);
                praise3Iv.setImageResource(R.mipmap.praise_no);
                break;
            case "3":
                praise1Iv.setImageResource(R.mipmap.praise_yes);
                praise2Iv.setImageResource(R.mipmap.praise_yes);
                praise3Iv.setImageResource(R.mipmap.praise_yes);
                break;
        }
    }

    TextWatcher contentTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            toEvaluateBean.setTce_desc(s.toString());
        }
    };
}
