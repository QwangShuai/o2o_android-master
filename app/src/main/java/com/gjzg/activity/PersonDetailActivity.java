package com.gjzg.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.EvaluateAdapter;
import com.gjzg.bean.EvaluateBean;
import com.gjzg.bean.SkillsBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import persondetail.presenter.IPersonDetailPresenter;
import persondetail.presenter.PersonDetailPresenter;
import persondetail.view.IPersonDetailActivity;
import com.gjzg.adapter.UserSkillAdapter;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CImageView;
import com.gjzg.custom.CProgressDialog;

public class PersonDetailActivity extends AppCompatActivity implements IPersonDetailActivity, View.OnClickListener {

    private View rootView, headView;
    private CImageView iconIv;
    private TextView nameTv, sexTv, areaTv, addressTv, infoTv, roleTv, countTv;
    private RelativeLayout returnRl;
    private ListView listView;
    private GridView gridView;
    private List<SkillsBean> skillsBeanList = new ArrayList<>();
    private UserSkillAdapter userSkillAdapter;
    private CProgressDialog cpd;
    private EvaluateAdapter evaluateAdapter;
    private List<EvaluateBean> evaluateBeanList = new ArrayList<>();
    private String detailId = "";
    private IPersonDetailPresenter personDetailPresenter;

    private final int INFO_SUCCESS = 1;
    private final int INFO_FAILURE = 2;
    private final int SKILL_SUCCESS = 3;
    private final int SKILL_FAILURE = 4;
    private final int EVALUATE_SUCCESS = 5;
    private final int EVALUATE_FAILURE = 6;

    private UserInfoBean userInfoBean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case INFO_SUCCESS:
                        personDetailPresenter.getSkill(NetConfig.skillUrl);
                        break;
                    case INFO_FAILURE:
                        break;
                    case SKILL_SUCCESS:
                        personDetailPresenter.evaluate(NetConfig.otherEvaluateUrl + "?tc_u_id=" + detailId);
                        break;
                    case SKILL_FAILURE:
                        break;
                    case EVALUATE_SUCCESS:
                        cpd.dismiss();
                        notifyData();
                        break;
                    case EVALUATE_FAILURE:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(PersonDetailActivity.this).inflate(R.layout.activity_person_detail, null);
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
        if (handler != null) {
            handler.removeMessages(INFO_SUCCESS);
            handler.removeMessages(INFO_FAILURE);
            handler.removeMessages(EVALUATE_SUCCESS);
            handler.removeMessages(EVALUATE_FAILURE);
        }
    }

    private void initView() {
        initRootView();
        initHeadView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_person_detail_return);
        listView = (ListView) rootView.findViewById(R.id.lv_person_detail);
        cpd = Utils.initProgressDialog(PersonDetailActivity.this, cpd);
    }

    private void initHeadView() {
        headView = LayoutInflater.from(PersonDetailActivity.this).inflate(R.layout.head_person_detail, null);
        iconIv = (CImageView) headView.findViewById(R.id.iv_head_person_detail);
        nameTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_name);
        sexTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_sex);
        areaTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_area);
        addressTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_address);
        infoTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_info);
        roleTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_role);
        countTv = (TextView) headView.findViewById(R.id.tv_head_person_detail_count);
        gridView = (GridView) headView.findViewById(R.id.gv_head_person_detail);
        listView.addHeaderView(headView);
    }

    private void initData() {
        evaluateAdapter = new EvaluateAdapter(PersonDetailActivity.this, evaluateBeanList);
        userSkillAdapter = new UserSkillAdapter(PersonDetailActivity.this, skillsBeanList);
        detailId = getIntent().getStringExtra(IntentConfig.talkToDetail);
        personDetailPresenter = new PersonDetailPresenter(this);
    }

    private void setData() {
        listView.setAdapter(evaluateAdapter);
        gridView.setAdapter(userSkillAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
    }

    private void loadData() {
        cpd.show();
        personDetailPresenter.info(NetConfig.userInfoUrl + detailId);
    }

    private void notifyData() {
        Picasso.with(PersonDetailActivity.this).load(userInfoBean.getU_img()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        nameTv.setText(userInfoBean.getU_true_name());
        String sex = userInfoBean.getU_sex();
        if (sex.equals("-1")) {
            sexTv.setText("无");
        } else if (sex.equals("0")) {
            sexTv.setText("女");
        } else if (sex.equals("1")) {
            sexTv.setText("男");
        }
        areaTv.setText(userInfoBean.getUser_area_name());
        addressTv.setText(userInfoBean.getUei_address());
        infoTv.setText(userInfoBean.getU_info());
        String skill = userInfoBean.getU_skills();
        if (skill.equals("0")) {
            roleTv.setText("雇主");
        } else {
            roleTv.setText("工人");
        }
        userSkillAdapter.notifyDataSetChanged();
        Utils.setGridViewHeight(gridView, 4);
        countTv.setText("Ta收到的评价（" + evaluateBeanList.size() + "）");
        evaluateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_person_detail_return:
                finish();
                break;
        }
    }

    @Override
    public void infoSuccess(String json) {
        userInfoBean = DataUtils.getUserInfoBean(json);
        handler.sendEmptyMessage(INFO_SUCCESS);
    }

    @Override
    public void infoFailure(String failure) {

    }

    @Override
    public void skillSuccess(String json) {
        String skillId = userInfoBean.getU_skills();
        String[] skillArr = skillId.split(",");
        List<String> skillIdList = new ArrayList<>();
        for (int i = 0; i < skillArr.length; i++) {
            skillIdList.add(skillArr[i]);
        }
        List<String> skillNameList = new ArrayList<>();
//        skillNameList.addAll(DataUtils.getSkillNameList(json, skillIdList));
        for (int i = 0; i < skillNameList.size(); i++) {
            SkillsBean skillsBean = new SkillsBean();
//            skillsBean.setName(skillNameList.get(i));
            skillsBeanList.add(skillsBean);
        }

        handler.sendEmptyMessage(SKILL_SUCCESS);
    }

    @Override
    public void skillFailure(String failure) {

    }

    @Override
    public void evaluateSuccess(String json) {
        evaluateBeanList.addAll(DataUtils.getEvaluateBeanList(json));
        handler.sendEmptyMessage(EVALUATE_SUCCESS);
    }

    @Override
    public void evaluateFailure(String failure) {

    }
}
