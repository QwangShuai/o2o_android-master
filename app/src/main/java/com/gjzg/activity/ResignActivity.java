package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.bean.ToComplainBean;
import com.gjzg.bean.ToResignBean;
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

public class ResignActivity extends AppCompatActivity implements View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private CImageView iconIv;
    private TextView nameTv, countTv, sumitTv;
    private ImageView sexIv, praise1Iv, praise2Iv, praise3Iv;
    private LinearLayout complainEmployerLl;
    private EditText contentEt;
    private CProgressDialog cpd;
    private OkHttpClient okHttpClient;
    private ToResignBean toResignBean;
    private UserInfoBean userInfoBean;
    private int praiseCount = 0;

    private String resignTip = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        cpd.dismiss();
                        notifyData();
                        break;
                    case 2:
                        cpd.dismiss();
                        if (resignTip.equals("success")) {
                            startActivity(new Intent(ResignActivity.this, MainActivity.class));
                        } else if (resignTip.equals("failure")) {
                            Utils.toast(ResignActivity.this, "失败");
                        } else {
                            Utils.toast(ResignActivity.this, resignTip);
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_resign, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_resign_return);
        complainEmployerLl = (LinearLayout) rootView.findViewById(R.id.ll_resign_complain);
        iconIv = (CImageView) rootView.findViewById(R.id.iv_resign_icon);
        nameTv = (TextView) rootView.findViewById(R.id.tv_resign_name);
        countTv = (TextView) rootView.findViewById(R.id.tv_resign_count);
        sumitTv = (TextView) rootView.findViewById(R.id.tv_resign_submit);
        sexIv = (ImageView) rootView.findViewById(R.id.iv_resign_sex);
        praise1Iv = (ImageView) rootView.findViewById(R.id.iv_resign_praise_1);
        praise2Iv = (ImageView) rootView.findViewById(R.id.iv_resign_praise_2);
        praise3Iv = (ImageView) rootView.findViewById(R.id.iv_resign_praise_3);
        contentEt = (EditText) rootView.findViewById(R.id.et_resign_content);
        cpd = Utils.initProgressDialog(ResignActivity.this, cpd);
    }

    private void initData() {
        okHttpClient = new OkHttpClient();
        toResignBean = (ToResignBean) getIntent().getSerializableExtra(IntentConfig.toResign);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        complainEmployerLl.setOnClickListener(this);
        sumitTv.setOnClickListener(this);
        praise1Iv.setOnClickListener(this);
        praise2Iv.setOnClickListener(this);
        praise3Iv.setOnClickListener(this);
    }

    private void loadData() {
        cpd.show();
        String infoUrl = NetConfig.userInfoUrl + toResignBean.getAuthorId();
        Request infoRequest = new Request.Builder().url(infoUrl).get().build();
        okHttpClient.newCall(infoRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    userInfoBean = DataUtils.getUserInfoBean(response.body().string());
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void notifyData() {
        Picasso.with(ResignActivity.this).load(userInfoBean.getU_img()).into(iconIv);
        nameTv.setText(userInfoBean.getU_true_name());
        countTv.setText("好评" + userInfoBean.getU_high_opinions() + "次");
        if (userInfoBean.getU_sex().equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (userInfoBean.getU_sex().equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_resign_return:
                finish();
                break;
            case R.id.ll_resign_complain:
                ToComplainBean toComplainBean = new ToComplainBean();
                toComplainBean.setAgainstId(toResignBean.getAuthorId());
                toComplainBean.setCtType("1");
                Intent complainIntent = new Intent(ResignActivity.this, ComplainActivity.class);
                complainIntent.putExtra(IntentConfig.toComplain, toComplainBean);
                startActivity(complainIntent);
                break;
            case R.id.iv_resign_praise_1:
                praiseCount = 1;
                refreshPraise();
                break;
            case R.id.iv_resign_praise_2:
                praiseCount = 2;
                refreshPraise();
                break;
            case R.id.iv_resign_praise_3:
                praiseCount = 3;
                refreshPraise();
                break;
            case R.id.tv_resign_submit:
                submit();
                break;
        }
    }

    private void refreshPraise() {
        switch (praiseCount) {
            case 0:
                praise1Iv.setImageResource(R.mipmap.praise_no);
                praise2Iv.setImageResource(R.mipmap.praise_no);
                praise3Iv.setImageResource(R.mipmap.praise_no);
                break;
            case 1:
                praise1Iv.setImageResource(R.mipmap.praise_yes);
                praise2Iv.setImageResource(R.mipmap.praise_no);
                praise3Iv.setImageResource(R.mipmap.praise_no);
                break;
            case 2:
                praise1Iv.setImageResource(R.mipmap.praise_yes);
                praise2Iv.setImageResource(R.mipmap.praise_yes);
                praise3Iv.setImageResource(R.mipmap.praise_no);
                break;
            case 3:
                praise1Iv.setImageResource(R.mipmap.praise_yes);
                praise2Iv.setImageResource(R.mipmap.praise_yes);
                praise3Iv.setImageResource(R.mipmap.praise_yes);
                break;
        }
    }

    private void submit() {
        if (!TextUtils.isEmpty(contentEt.getText().toString())) {
            cpd.show();
            String submitUrl = NetConfig.orderUrl +
                    "?action=unbind" +
                    "&tew_id=" + toResignBean.getTewId() +
                    "&t_id=" + toResignBean.getTaskId() +
                    "&type=resign" +
                    "&o_worker=" + UserUtils.readUserData(ResignActivity.this).getId() +
                    "&u_id=" + toResignBean.getAuthorId() +
                    "&s_id=" + toResignBean.getSkillId() +
                    "&start=" + praiseCount +
                    "&appraisal=" + contentEt.getText().toString();
            Request submitRequest = new Request.Builder().url(submitUrl).get().build();
            okHttpClient.newCall(submitRequest).enqueue(new Callback() {
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
                                resignTip = beanObj.optString("data");
                                handler.sendEmptyMessage(2);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Utils.toast(ResignActivity.this, "请填写辞职原因");
        }
    }
}
