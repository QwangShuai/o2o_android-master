package com.gjzg.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.ScnDiaAdapter;
import com.gjzg.utils.Utils;

public class RefuseOrderActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView, dialogView;
    private RelativeLayout returnRl, chooseAgainRl;
    private ImageView clickChooseIv, chooseAgainIv, dialogCloseIv;
    private AlertDialog dialog;
    private ListView dialogLv;
    private TextView reasonTv, dialogTitleTv, submitTv;
    private EditText desEt;

    private List<String> list;
    private ScnDiaAdapter scnDiaAdapter;

    private boolean firstClick = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_refuse_order, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
    }

    private void initView() {
        initRootView();
        initDialogView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_refuse_order_return);
        chooseAgainRl = (RelativeLayout) rootView.findViewById(R.id.rl_refuse_order_choose_again);
        clickChooseIv = (ImageView) rootView.findViewById(R.id.iv_refuse_order_click_choose);
        chooseAgainIv = (ImageView) rootView.findViewById(R.id.iv_refuse_order_choose_again);
        reasonTv = (TextView) rootView.findViewById(R.id.tv_refuse_order_reason);
        submitTv = (TextView) rootView.findViewById(R.id.tv_refuse_order_submit);
        desEt = (EditText) rootView.findViewById(R.id.et_refuse_order_des);
    }

    private void initDialogView() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_scn, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialogLv = (ListView) dialogView.findViewById(R.id.lv_dialog_scn);
        dialogTitleTv = (TextView) dialogView.findViewById(R.id.tv_dialog_scn_title);
        dialogCloseIv = (ImageView) dialogView.findViewById(R.id.iv_dialog_scn_close);
        dialogCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initData() {
        dialogTitleTv.setText("原因");
        list = new ArrayList<>();
        list.add("工资太低");
        list.add("无法胜任");
        list.add("工期太短");
        list.add("其他问题");
        scnDiaAdapter = new ScnDiaAdapter(this, list);
    }

    private void setData() {
        dialogLv.setAdapter(scnDiaAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        clickChooseIv.setOnClickListener(this);
        chooseAgainIv.setOnClickListener(this);
        dialogLv.setOnItemClickListener(this);
        submitTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_refuse_order_return:
                finish();
                break;
            case R.id.iv_refuse_order_click_choose:
                dialog.show();
                break;
            case R.id.iv_refuse_order_choose_again:
                dialog.show();
                break;
            case R.id.tv_refuse_order_submit:
                submit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (firstClick) {
            clickChooseIv.setVisibility(View.GONE);
            chooseAgainRl.setVisibility(View.VISIBLE);
            firstClick = false;
        }
        reasonTv.setText(list.get(position));
        dialog.dismiss();
    }

    private void submit() {
        Utils.toast(this, "原因：" + reasonTv.getText().toString() + "\n描述：" + desEt.getText().toString());
    }
}
