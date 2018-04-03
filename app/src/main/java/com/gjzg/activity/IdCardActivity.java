package com.gjzg.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import idcard.presenter.IIdCardPresenter;
import idcard.presenter.IdCardPresenter;
import idcard.view.IIdCardActivity;
import com.gjzg.adapter.InputPasswordAdapter;
import com.gjzg.bean.InputPasswordBean;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class IdCardActivity extends AppCompatActivity implements IIdCardActivity, View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private CProgressDialog cpd;
    private RelativeLayout returnRl, nextRl;
    private TextView idcardNumberTv;

    private GridView idCardGv;
    private List<InputPasswordBean> inputPasswordBeanList;
    private InputPasswordAdapter inputPasswordAdapter;
    private StringBuilder idCardSb;

    private IIdCardPresenter idCardPresenter;

    private final int VERIFY_SUCCESS = 0;
    private final int VERIFY_FAILURE = 1;
    private String tip;

    private String verifycode;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case VERIFY_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(IdCardActivity.this, tip);
                        Intent intent = new Intent(IdCardActivity.this, PwdActivity.class);
                        intent.putExtra("verifycode", verifycode);
                        startActivity(intent);
                        break;
                    case VERIFY_FAILURE:
                        cpd.dismiss();
                        Utils.toast(IdCardActivity.this, tip);
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(IdCardActivity.this).inflate(R.layout.activity_id_card, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }

    private void initView() {
        cpd = Utils.initProgressDialog(IdCardActivity.this, cpd);
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_idcard_return);
        nextRl = (RelativeLayout) rootView.findViewById(R.id.rl_idcard_next);
        idcardNumberTv = (TextView) rootView.findViewById(R.id.tv_idcard_number);
        idCardGv = (GridView) rootView.findViewById(R.id.gv_idcard);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            verifycode = intent.getStringExtra("verifycode");
        }
        InputPasswordBean inputPasswordBean0 = new InputPasswordBean(0, 1, "");
        InputPasswordBean inputPasswordBean1 = new InputPasswordBean(0, 2, "ABC");
        InputPasswordBean inputPasswordBean2 = new InputPasswordBean(0, 3, "DEF");
        InputPasswordBean inputPasswordBean3 = new InputPasswordBean(0, 4, "GHI");
        InputPasswordBean inputPasswordBean4 = new InputPasswordBean(0, 5, "JKL");
        InputPasswordBean inputPasswordBean5 = new InputPasswordBean(0, 6, "MNO");
        InputPasswordBean inputPasswordBean6 = new InputPasswordBean(0, 7, "PQRS");
        InputPasswordBean inputPasswordBean7 = new InputPasswordBean(0, 8, "TUV");
        InputPasswordBean inputPasswordBean8 = new InputPasswordBean(0, 9, "WXYZ");
        InputPasswordBean inputPasswordBean9 = new InputPasswordBean(1, 0, "");
        InputPasswordBean inputPasswordBean10 = new InputPasswordBean(0, 0, "");
        InputPasswordBean inputPasswordBean11 = new InputPasswordBean(2, 0, "");
        inputPasswordBeanList = new ArrayList<>();
        inputPasswordBeanList.add(inputPasswordBean0);
        inputPasswordBeanList.add(inputPasswordBean1);
        inputPasswordBeanList.add(inputPasswordBean2);
        inputPasswordBeanList.add(inputPasswordBean3);
        inputPasswordBeanList.add(inputPasswordBean4);
        inputPasswordBeanList.add(inputPasswordBean5);
        inputPasswordBeanList.add(inputPasswordBean6);
        inputPasswordBeanList.add(inputPasswordBean7);
        inputPasswordBeanList.add(inputPasswordBean8);
        inputPasswordBeanList.add(inputPasswordBean9);
        inputPasswordBeanList.add(inputPasswordBean10);
        inputPasswordBeanList.add(inputPasswordBean11);
        inputPasswordAdapter = new InputPasswordAdapter(IdCardActivity.this, inputPasswordBeanList);
        idCardGv.setAdapter(inputPasswordAdapter);
        Utils.setGridViewHeight(idCardGv, 3);
        idCardSb = new StringBuilder();
        idCardPresenter = new IdCardPresenter(this);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        nextRl.setOnClickListener(this);
        idCardGv.setOnItemClickListener(this);
    }

    @Override
    public void showVerifySuccess(String success) {
        tip = success;
        handler.sendEmptyMessage(VERIFY_SUCCESS);
    }

    @Override
    public void showVerifyFailure(String failure) {
        tip = failure;
        handler.sendEmptyMessage(VERIFY_FAILURE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_idcard_return:
                finish();
                break;
            case R.id.rl_idcard_next:
                cpd.show();
                idCardPresenter.verify(UserUtils.readUserData(IdCardActivity.this).getMobile(), idCardSb.toString());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gv_idcard:
                InputPasswordBean inputPasswordBean = inputPasswordBeanList.get(position);
                if (inputPasswordBean != null) {
                    switch (inputPasswordBean.getType()) {
                        case 0:
                            if (idCardSb.length() < 18) {
                                idCardSb.append(inputPasswordBean.getNumber());
                            }
                            break;
                        case 2:
                            if (idCardSb.length() != 0) {
                                idCardSb.deleteCharAt(idCardSb.length() - 1);
                            }
                            break;
                    }
                    refreshView();
                }
                break;
        }
    }

    private void refreshView() {
        idcardNumberTv.setText(idCardSb.toString());
    }
}
