package com.gjzg.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import com.gjzg.bean.ToComplainBean;
import com.gjzg.bean.ToFireBean;
import com.gjzg.bean.UserInfoBean;

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

import org.json.JSONException;
import org.json.JSONObject;

public class FireActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private ToFireBean toFireBean;
    private EditText contentEt;
    private TextView submitTv;
    private CProgressDialog cProgressDialog;
    private LinearLayout complainLl;
    private CImageView iconIv;
    private ImageView sexIv, praise1Iv, praise2Iv, praise3Iv;
    private TextView nameTv, skillTv, highOpinionTv;

    private UserInfoBean userInfoBean;
    private String fireTip = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        cProgressDialog.dismiss();
                        if (fireTip.equals("success")) {
                            startActivity(new Intent(FireActivity.this, EmployerManageActivity.class));
                        } else if (fireTip.equals("failure")) {
                            Utils.toast(FireActivity.this, "失败");
                        } else {
                            Utils.toast(FireActivity.this, fireTip);
                        }
                        break;
                    case 2:
                        notifyData();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(FireActivity.this).inflate(R.layout.activity_fire, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1);
            handler.removeMessages(2);
            handler = null;
        }
    }

    private void initView() {
        contentEt = (EditText) rootView.findViewById(R.id.et_fire_content);
        submitTv = (TextView) rootView.findViewById(R.id.tv_fire_submit);
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_fire_return);
        cProgressDialog = Utils.initProgressDialog(FireActivity.this, cProgressDialog);
        iconIv = (CImageView) rootView.findViewById(R.id.iv_fire_icon);
        sexIv = (ImageView) rootView.findViewById(R.id.iv_fire_sex);
        praise1Iv = (ImageView) rootView.findViewById(R.id.iv_fire_praise_1);
        praise2Iv = (ImageView) rootView.findViewById(R.id.iv_fire_praise_2);
        praise3Iv = (ImageView) rootView.findViewById(R.id.iv_fire_praise_3);
        nameTv = (TextView) rootView.findViewById(R.id.tv_fire_name);
        skillTv = (TextView) rootView.findViewById(R.id.tv_fire_skill);
        highOpinionTv = (TextView) rootView.findViewById(R.id.tv_fire_count);
        complainLl = (LinearLayout) rootView.findViewById(R.id.ll_fire_complain);
    }

    private void initData() {
        toFireBean = (ToFireBean) getIntent().getSerializableExtra(IntentConfig.toFire);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        contentEt.addTextChangedListener(contentTw);
        praise1Iv.setOnClickListener(this);
        praise2Iv.setOnClickListener(this);
        praise3Iv.setOnClickListener(this);
        complainLl.setOnClickListener(this);
    }

    private void loadData() {
        cProgressDialog.show();
        String url = NetConfig.userInfoUrl + toFireBean.getFireId();
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
                    userInfoBean = DataUtils.getUserInfoBean(json);
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void notifyData() {
        cProgressDialog.dismiss();
        Picasso.with(FireActivity.this).load(userInfoBean.getU_img()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        String sex = userInfoBean.getU_sex();
        if (sex.equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (sex.equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
        nameTv.setText(userInfoBean.getU_true_name());
        skillTv.setText(toFireBean.getSkillName());
        highOpinionTv.setText("好评" + userInfoBean.getU_high_opinions() + "次");
    }

    private void submit() {
        cProgressDialog.show();
        if (!TextUtils.isEmpty(toFireBean.getContent())) {
            String fireUrl = NetConfig.orderUrl +
                    "?action=unbind" +
                    "&tew_id=" + toFireBean.getTewId() +
                    "&t_id=" + toFireBean.getTaskId() +
                    "&type=fire" +
                    "&o_worker=" + toFireBean.getFireId() +
                    "&u_id=" + UserUtils.readUserData(FireActivity.this).getId() +
                    "&s_id=" + toFireBean.getSkillId() +
                    "&start=" + toFireBean.getStart() +
                    "&appraisal=" + toFireBean.getContent();
            fire(fireUrl);
        } else {
            Utils.toast(FireActivity.this, "请填写解雇原因");
        }
    }

    private void fire(String fireUrl) {
        Log.e(this.getClass().getSimpleName(), "fireUrl\n" + fireUrl);
        Request request = new Request.Builder().url(fireUrl).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                fireTip = jsonObject.optString("data");
                                handler.sendEmptyMessage(1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fire_return:
                finish();
                break;
            case R.id.tv_fire_submit:
                submit();
                break;
            case R.id.iv_fire_praise_1:
                toFireBean.setStart("1");
                changePraise(toFireBean.getStart());
                break;
            case R.id.iv_fire_praise_2:
                toFireBean.setStart("2");
                changePraise(toFireBean.getStart());
                break;
            case R.id.iv_fire_praise_3:
                toFireBean.setStart("3");
                changePraise(toFireBean.getStart());
                break;
            case R.id.ll_fire_complain:
                ToComplainBean toComplainBean = new ToComplainBean();
                toComplainBean.setSkill(toFireBean.getSkillName());
                toComplainBean.setContent("");
                toComplainBean.setCtType("2");
                toComplainBean.setAgainstId(toFireBean.getFireId());
                toComplainBean.setAuthorId(UserUtils.readUserData(FireActivity.this).getId());
                Intent complainIntent = new Intent(FireActivity.this, ComplainActivity.class);
                complainIntent.putExtra(IntentConfig.toComplain, toComplainBean);
                startActivity(complainIntent);
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
            toFireBean.setContent(s.toString());
        }
    };

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
}
