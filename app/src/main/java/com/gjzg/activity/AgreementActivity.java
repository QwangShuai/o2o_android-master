package com.gjzg.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gjzg.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.ArticleAdapter;
import com.gjzg.bean.ArticleBean;
import com.gjzg.config.NetConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class AgreementActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private ScrollView agreementSv;
    private TextView agreementDescTv;
    private ListView agreementLv;
    private CProgressDialog cProgressDialog;
    private List<ArticleBean> articleBeanList = new ArrayList<>();
    private boolean isList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        notifyData();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(AgreementActivity.this).inflate(R.layout.activity_agreement, null);
        setContentView(rootView);
        initView();
        setListener();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_agreement_return);
        agreementSv = (ScrollView) rootView.findViewById(R.id.sv_agreement);
        agreementDescTv = (TextView) rootView.findViewById(R.id.tv_agreement_desc);
        agreementLv = (ListView) rootView.findViewById(R.id.lv_agreement);
        cProgressDialog = Utils.initProgressDialog(AgreementActivity.this, cProgressDialog);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        agreementLv.setOnItemClickListener(this);
    }

    private void loadData() {
        String url = NetConfig.articlesListUrl;
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
                    if (DataUtils.getArticleBeanList(json) != null) {
                        articleBeanList.addAll(DataUtils.getArticleBeanList(json));
                        if (articleBeanList.size() != 0) {
                            String desc = articleBeanList.get(0).getA_desc();
                            if (desc == null || desc.equals("null") || TextUtils.isEmpty(desc)) {
                                isList = true;
                            } else {
                                isList = false;
                            }
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        });
    }

    private void notifyData() {
        if (isList) {
            agreementSv.setVisibility(View.GONE);
            agreementLv.setVisibility(View.VISIBLE);
            ArticleAdapter articleAdapter = new ArticleAdapter(AgreementActivity.this, articleBeanList);
            agreementLv.setAdapter(articleAdapter);
        } else {
            agreementSv.setVisibility(View.VISIBLE);
            agreementLv.setVisibility(View.GONE);
            if (articleBeanList.size() != 0) {
                String desc = articleBeanList.get(0).getA_desc();
                if (desc == null || desc.equals("null") || TextUtils.isEmpty(desc)) {
                } else {
                    agreementDescTv.setText(desc);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_agreement_return:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleBean articleBean = articleBeanList.get(position);
        if (articleBean != null) {
            String a_id = articleBean.getA_id();
            if (a_id == null || a_id.equals("null") || TextUtils.isEmpty(a_id)) {
            } else {
                Intent intent = new Intent(AgreementActivity.this, ArticlesInfoActivity.class);
                intent.putExtra("a_id", a_id);
                startActivity(intent);
            }
        }
    }
}
