package com.gjzg.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.gjzg.adapter.TalkEmployerAdapter;
import com.gjzg.bean.TalkEmployerBean;
import com.gjzg.bean.TalkEmployerWorkerBean;
import com.gjzg.bean.ToTalkEmployerBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.listener.IdPosClickHelp;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CImageView;
import com.gjzg.custom.CProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import talkemployer.presenter.TalkEmployerPresenter;
import talkemployer.view.ITalkEmployerActivity;

public class TalkEmployerActivity extends AppCompatActivity implements ITalkEmployerActivity, View.OnClickListener, IdPosClickHelp {

    private View rootView;
    private RelativeLayout returnRl;
    private View headMapView;
    private CImageView iconIv, phoneIv;
    private ImageView sexIv;
    private TextView nameTv, infoTv, addressTv;
    private MapView mapView;
    private ListView talkLv;
    private BaiduMap baiduMap;
    private BitmapDescriptor bitmap;
    private CProgressDialog cpd;
    private TalkEmployerPresenter talkEmployerPresenter;
    private TalkEmployerBean talkEmployerBean;
    private final int LOAD_SUCCESS = 1;
    private final int LOAD_FAILURE = 2;
    private final int SKILL_SUCCESS = 3;
    private final int SKILL_FAILURE = 4;
    private final int INVITE_SUCCESS = 5;
    private final int INVITE_FAILURE = 6;
    private List<String> idList = new ArrayList<>();
    private List<TalkEmployerWorkerBean> talkEmployerWorkerBeanList = new ArrayList<>();
    private TalkEmployerAdapter talkEmployerAdapter;
    private int clickPosition;
    private ToTalkEmployerBean toTalkEmployerBean;

    private RelativeLayout waitRl, noYourselfRl, talkToyouRl, doingForyouRl, overRl;
    private final int NONE = 0, EMPLOYER_WIAT = 1, NO_YOURSELF = 2, TALK_TO_YOU = 3, DOING_FOR_YOU = 4, OVER = 5;
    private int SHOW_STATE;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        talkEmployerPresenter.loadSkill(NetConfig.skillUrl);
                        break;
                    case LOAD_FAILURE:
                        break;
                    case SKILL_SUCCESS:
                        cpd.dismiss();
                        notifyData();
                        break;
                    case SKILL_FAILURE:
                        break;
                    case INVITE_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(TalkEmployerActivity.this, "邀约请求已发送");
                        startActivity(new Intent(TalkEmployerActivity.this, WorkerManageActivity.class));
                        break;
                    case INVITE_FAILURE:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_talk_employer, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
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
        if (handler != null) {
            handler.removeMessages(LOAD_SUCCESS);
            handler.removeMessages(LOAD_FAILURE);
            handler.removeMessages(SKILL_SUCCESS);
            handler.removeMessages(SKILL_FAILURE);
            handler.removeMessages(INVITE_SUCCESS);
            handler.removeMessages(INVITE_FAILURE);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
        initHeadView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_talk_employer_return);
        talkLv = (ListView) rootView.findViewById(R.id.lv_talk_employer);
        cpd = Utils.initProgressDialog(TalkEmployerActivity.this, cpd);
        waitRl = (RelativeLayout) rootView.findViewById(R.id.rl_talk_employer_wait);
        noYourselfRl = (RelativeLayout) rootView.findViewById(R.id.rl_talk_employer_no_yourself);
        talkToyouRl = (RelativeLayout) rootView.findViewById(R.id.rl_talk_employer_talk_to_you);
        doingForyouRl = (RelativeLayout) rootView.findViewById(R.id.rl_talk_employer_doing_for_you);
        overRl = (RelativeLayout) rootView.findViewById(R.id.rl_talk_employer_over);
    }

    private void initHeadView() {
        headMapView = LayoutInflater.from(TalkEmployerActivity.this).inflate(R.layout.head_talk_employer, null);
        iconIv = (CImageView) headMapView.findViewById(R.id.iv_head_talk_employer_icon);
        phoneIv = (CImageView) headMapView.findViewById(R.id.iv_head_talk_employer_phone);
        phoneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (SHOW_STATE) {
                    case EMPLOYER_WIAT:
                    case OVER:
                        Utils.toast(TalkEmployerActivity.this, "未邀约不能打电话！");
                        break;
                    default:
                        if (UserUtils.isUserLogin(TalkEmployerActivity.this)) {
                            Intent in = new Intent(Intent.ACTION_DIAL);
                            String phone = talkEmployerBean.getMobile();
                            if (!TextUtils.isEmpty(phone)&&!phone.equals(UserUtils.readUserData(TalkEmployerActivity.this).getMobile())) {
                                in.setData(Uri.parse("tel:" + phone));
                                if (in.resolveActivity(getPackageManager()) != null) {
                                    startActivity(in);
                                }
                            } else {
                                Utils.toast(TalkEmployerActivity.this,"请不要联系自己");
                            }
                        } else {
                            startActivity(new Intent(TalkEmployerActivity.this, LoginActivity.class));
                        }
                        break;
                }
            }
        });
        sexIv = (ImageView) headMapView.findViewById(R.id.iv_head_talk_employer_sex);
        nameTv = (TextView) headMapView.findViewById(R.id.tv_head_talk_employer_name);
        infoTv = (TextView) headMapView.findViewById(R.id.tv_head_talk_employer_info);
        addressTv = (TextView) headMapView.findViewById(R.id.tv_head_talk_employer_address);
        talkLv.addHeaderView(headMapView);
        mapView = (MapView) headMapView.findViewById(R.id.mv_head_talk_employer);
        baiduMap = mapView.getMap();
        UiSettings settings = baiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setOverlookingGesturesEnabled(false);
        settings.setZoomGesturesEnabled(false);
        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.point_blue);
    }

    private void initData() {
        talkEmployerPresenter = new TalkEmployerPresenter(this);
        toTalkEmployerBean = (ToTalkEmployerBean) getIntent().getSerializableExtra(IntentConfig.toTalkEmployer);
        talkEmployerAdapter = new TalkEmployerAdapter(TalkEmployerActivity.this, talkEmployerWorkerBeanList, this);
    }

    private void setData() {
        talkLv.setAdapter(talkEmployerAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        waitRl.setOnClickListener(this);
        iconIv.setOnClickListener(this);
    }

    private void loadData() {
        cpd.show();
        String url;
        if (UserUtils.isUserLogin(TalkEmployerActivity.this)) {
            url = NetConfig.taskBaseUrl + "?action=info&t_id=" + toTalkEmployerBean.getT_id() + "&o_worker=" + UserUtils.readUserData(TalkEmployerActivity.this).getId();
        } else {
            url = NetConfig.taskBaseUrl + "?action=info&t_id=" + toTalkEmployerBean.getT_id();
        }
        talkEmployerPresenter.load(url);
    }

    private void notifyData() {
        LatLng point = new LatLng(Double.parseDouble(talkEmployerBean.getPosY()), Double.parseDouble(talkEmployerBean.getPosX()));
        OverlayOptions overlayOptions = new MarkerOptions().position(point).icon(bitmap);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(point, Float.parseFloat("15"));
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.addOverlay(overlayOptions);
        Picasso.with(TalkEmployerActivity.this).load(talkEmployerBean.getIcon()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        String sex = talkEmployerBean.getSex();
        if (sex.equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (sex.equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
        nameTv.setText(talkEmployerBean.getName());
        infoTv.setText(talkEmployerBean.getDesc());
        addressTv.setText(talkEmployerBean.getAddress());
        talkEmployerWorkerBeanList.clear();
        talkEmployerWorkerBeanList.addAll(talkEmployerBean.getTalkEmployerWorkerBeanList());
        talkEmployerAdapter.notifyDataSetChanged();

        if (talkEmployerBean.getAuthorId().equals(UserUtils.readUserData(TalkEmployerActivity.this).getId())) {
            SHOW_STATE = NO_YOURSELF;
        } else {
            int relation = talkEmployerBean.getRelation();
            if (relation == 1) {
                int relationType = talkEmployerBean.getRelationType();
                if (relationType == 1) {
                    SHOW_STATE = DOING_FOR_YOU;
                } else if (relationType == 0) {
                    SHOW_STATE = TALK_TO_YOU;
                }
            } else if (relation == 0) {
                SHOW_STATE = EMPLOYER_WIAT;
            }
        }
        String t_status = talkEmployerBean.getT_status();
        if (t_status == null || t_status.equals("null") || TextUtils.isEmpty(t_status)) {
        } else {
            if (t_status.equals("3") || t_status.equals("4")) {
                SHOW_STATE = OVER;
            }
        }
        SHOW_STATE = NONE;
        refreshState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_talk_employer_return:
                finish();
                break;
            case R.id.rl_talk_employer_wait:
                if (UserUtils.isUserLogin(TalkEmployerActivity.this)) {
                    String idcard = UserUtils.readUserData(TalkEmployerActivity.this).getIdcard();
                    if (idcard == null || TextUtils.isEmpty(idcard) || idcard.equals("null")) {
                        Utils.toast(TalkEmployerActivity.this, "请在我的中完善个人信息");
                    } else {
                        inviteJudge();
                    }
                } else {
                    startActivity(new Intent(TalkEmployerActivity.this, LoginActivity.class));
                }
                break;
            case R.id.iv_head_talk_employer_icon:
                Intent intent = new Intent(TalkEmployerActivity.this, PersonDetailActivity.class);
                intent.putExtra(IntentConfig.talkToDetail, talkEmployerBean.getAuthorId());
                startActivity(intent);
                break;
        }
    }

    private void inviteJudge() {
        String tewId = "";
        for (int i = 0; i < talkEmployerWorkerBeanList.size(); i++) {
            if (talkEmployerWorkerBeanList.get(i).isSelect()) {
                tewId = talkEmployerWorkerBeanList.get(i).getTewId();
            }
        }
        if (TextUtils.isEmpty(tewId)) {
            Utils.toast(TalkEmployerActivity.this, "请选择任务");
        } else {
            String url = NetConfig.orderUrl + "?action=create&tew_id=" + tewId + "&t_id=" + toTalkEmployerBean.getT_id() + "&o_worker=" + UserUtils.readUserData(TalkEmployerActivity.this).getId() + "&o_sponsor=" + UserUtils.readUserData(TalkEmployerActivity.this).getId();
            cpd.show();
            talkEmployerPresenter.invite(url);
        }
    }

    @Override
    public void success(String json) {
        talkEmployerBean = DataUtils.getTalkEmployerBean(json);
        for (int i = 0; i < talkEmployerBean.getTalkEmployerWorkerBeanList().size(); i++) {
            idList.add(talkEmployerBean.getTalkEmployerWorkerBeanList().get(i).getId());
        }
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public void failure(String failure) {

    }

    @Override
    public void skillSuccess(String json) {
        List<String> skillNameList = new ArrayList<>();
        skillNameList.addAll(DataUtils.getSkillNameList(json, idList));
        for (int i = 0; i < skillNameList.size(); i++) {
            talkEmployerBean.getTalkEmployerWorkerBeanList().get(i).setSkill(skillNameList.get(i));
        }
        handler.sendEmptyMessage(SKILL_SUCCESS);
    }


    @Override
    public void skillFailure(String failure) {

    }

    @Override
    public void inviteSuccess(String json) {
        handler.sendEmptyMessage(INVITE_SUCCESS);
    }

    @Override
    public void inviteFailure(String json) {
        handler.sendEmptyMessage(INVITE_FAILURE);
    }

    @Override
    public void onClick(int id, int pos) {
        clickPosition = pos;
        switch (id) {
            case R.id.rb_item_talk_employer:
                for (int i = 0; i < talkEmployerWorkerBeanList.size(); i++) {
                    if (i == clickPosition) {
                        talkEmployerWorkerBeanList.get(i).setSelect(true);
                    } else {
                        talkEmployerWorkerBeanList.get(i).setSelect(false);
                    }
                }
                talkEmployerAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void refreshState() {
        switch (SHOW_STATE) {
            case EMPLOYER_WIAT:
                waitRl.setVisibility(View.VISIBLE);
                noYourselfRl.setVisibility(View.GONE);
                talkToyouRl.setVisibility(View.GONE);
                doingForyouRl.setVisibility(View.GONE);
                overRl.setVisibility(View.GONE);
                break;
            case NO_YOURSELF:
                waitRl.setVisibility(View.GONE);
                noYourselfRl.setVisibility(View.VISIBLE);
                talkToyouRl.setVisibility(View.GONE);
                doingForyouRl.setVisibility(View.GONE);
                overRl.setVisibility(View.GONE);
                break;
            case TALK_TO_YOU:
                waitRl.setVisibility(View.GONE);
                noYourselfRl.setVisibility(View.GONE);
                talkToyouRl.setVisibility(View.VISIBLE);
                doingForyouRl.setVisibility(View.GONE);
                overRl.setVisibility(View.GONE);
                break;
            case DOING_FOR_YOU:
                waitRl.setVisibility(View.GONE);
                noYourselfRl.setVisibility(View.GONE);
                talkToyouRl.setVisibility(View.GONE);
                doingForyouRl.setVisibility(View.VISIBLE);
                overRl.setVisibility(View.GONE);
                break;
            case OVER:
                waitRl.setVisibility(View.GONE);
                noYourselfRl.setVisibility(View.GONE);
                talkToyouRl.setVisibility(View.GONE);
                doingForyouRl.setVisibility(View.VISIBLE);
                overRl.setVisibility(View.VISIBLE);
                break;
        }
    }
}
