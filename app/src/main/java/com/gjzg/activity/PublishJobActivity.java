package com.gjzg.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gjzg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.gjzg.adapter.ScnDiaAdapter;

import com.gjzg.bean.CityBean;
import com.gjzg.bean.CountryBean;
import com.gjzg.bean.PublishWorkerBean;
import com.gjzg.bean.SkillsBean;

import com.gjzg.config.NetConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.gjzg.adapter.PublishKindAdapter;
import com.gjzg.adapter.SelectSkillAdapter;

import com.gjzg.bean.PublishBean;

import publishjob.listener.PublishJobClickHelp;
import publishjob.presenter.IPublishJobPresenter;
import publishjob.presenter.PublishJobPresenter;
import publishjob.view.IPublishJobActivity;

import com.gjzg.bean.SelectAddressBean;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 发布工作任务
 */

public class PublishJobActivity extends AppCompatActivity implements IPublishJobActivity, View.OnClickListener, PublishJobClickHelp {

    private View rootView, headView;
    private RelativeLayout returnRl, addRl, submitRl,typeRl;
    private EditText titleEt, descriptionEt, addressEt;
    private TextView typeTv, areaTv;
    private ListView lv;

    private PublishKindAdapter adapter;
    private List<PublishWorkerBean> publishWorkerBeanList;
    private String areaId;
    private PublishBean publishBean;
    private View typePopView,taskPopView,sumbitPopView,finishPopView;
    private PopupWindow typePop,taskPop,sumbitPop,finishPop;
    private ListView typeLv;

    private IPublishJobPresenter publishJobPresenter;
    private List<String> taskTypeList;
    private CProgressDialog cpd;
    private ScnDiaAdapter scnDiaAdapter;

    private View skillPopView;
    private PopupWindow skillPop;
    private ListView skillLv;
    private SelectSkillAdapter selectSkillAdapter;
    private List<SkillsBean> skillsBeanList;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year, month, day;
    private final int START_TIME = 1;
    private final int END_TIME = 2;
    private int pickState;
    private int pickPosition;
    private final int TASK_TYPE_SUCCESS = 1;
    private final int TASK_TYPE_FAILURE = 2;
    private final int SKILL_SUCCESS = 3;
    private final int SKILL_FAILURE = 4;
    private final int LOCATE_SUCCESS = 5;
    private final int PUBLISH_SUCCESS = 6;
    private final int PUBLISH_FAILURE = 7;
    private final int PROVINCE_ID_SUCCESS = 8;
    private final int CITY_ID_SUCCESS = 9;
    private final int AREA_ID_SUCCESS = 10;
    private final int SUMBIT_SUCCESS = 11;
    private LocationClient locationClient;
    private BDLocationListener bdLocationListener;
    private String tip = "";
    private String nowTime;
    private List<CityBean> list;
    private CountryBean countryBean;
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call;
    String country = "";
    String id = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case TASK_TYPE_SUCCESS:
                        scnDiaAdapter.notifyDataSetChanged();
                        break;
                    case TASK_TYPE_FAILURE:
                        break;
                    case SKILL_SUCCESS:
                        selectSkillAdapter.notifyDataSetChanged();
                        break;
                    case SKILL_FAILURE:
                        break;
                    case LOCATE_SUCCESS:
                        publishBean.setAreaId(publishBean.getArea());
                        break;
                    case PUBLISH_FAILURE:
                        Utils.toast(PublishJobActivity.this,tip);
                        break;
                    case PUBLISH_SUCCESS:
                        locationClient.stop();
                        cpd.dismiss();
                        backgroundAlpha(0.5f);
                        taskPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                        break;
                    case PROVINCE_ID_SUCCESS:
                        selectID(countryBean.getProvinceID(),1);
                        break;
                    case CITY_ID_SUCCESS:
                        selectID(countryBean.getCityID(),2);
                        break;
                    case AREA_ID_SUCCESS:
                        publishBean.setProvinceId(countryBean.getProvinceID());
                        publishBean.setCityId(countryBean.getCityID());
                        publishBean.setAreaId(countryBean.getDistrictID());
                        break;
                    case SUMBIT_SUCCESS:
                        backgroundAlpha(0.5f);
                        sumbitPop.showAtLocation(rootView,Gravity.CENTER,0,0);
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_send_job, null);
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
        locationClient.unRegisterLocationListener(bdLocationListener);
    }

    private void initView() {
        initRootView();
        initHeadView();
        initPopView();
    }

    private void initData() {
        initLocation();
        publishBean = new PublishBean();
        countryBean = UserUtils.readCountry(PublishJobActivity.this);
        if(!"null".equals(countryBean.getProvince())){
            publishBean.setArea(countryBean.getAddress());
            selectID("1",0);
        } else {
            publishBean.setArea("黑龙江省"+" "+"哈尔滨市"+" "+"道里区");
            publishBean.setProvinceId("12");
            publishBean.setCityId("167");
            publishBean.setAreaId("1415");
        }
        publishBean.setAuthorId(UserUtils.readUserData(PublishJobActivity.this).getId());
        publishJobPresenter = new PublishJobPresenter(this);
        publishWorkerBeanList = new ArrayList<>();
        PublishWorkerBean publishWorkerBean = new PublishWorkerBean("", "", "", "", "", "");
        publishWorkerBeanList.add(publishWorkerBean);
        publishBean.setPublishWorkerBeanList(publishWorkerBeanList);
        adapter = new PublishKindAdapter(PublishJobActivity.this, publishWorkerBeanList, this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(PublishJobActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String timeStr = year + "-" + (month + 1) + "-" + dayOfMonth;
                switch (pickState) {
                    case START_TIME:
                        publishWorkerBeanList.get(pickPosition).setStartTime(timeStr);
                        adapter.notifyDataSetChanged();
                        break;
                    case END_TIME:
                        publishWorkerBeanList.get(pickPosition).setEndTime(timeStr);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }, year, month - 1, day);
    }
    private void setData() {
        lv.setAdapter(adapter);
        locationClient.start();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_publish_job_return);
        addRl = (RelativeLayout) rootView.findViewById(R.id.rl_publish_job_add);
        submitRl = (RelativeLayout) rootView.findViewById(R.id.rl_change_price_submit);
        lv = (ListView) rootView.findViewById(R.id.lv_publish_job);
        cpd = Utils.initProgressDialog(PublishJobActivity.this, cpd);
    }

    private void initHeadView() {
        headView = LayoutInflater.from(PublishJobActivity.this).inflate(R.layout.head_publish_job, null);
        titleEt = (EditText) headView.findViewById(R.id.et_head_publish_job_title);
        descriptionEt = (EditText) headView.findViewById(R.id.et_head_publish_job_description);
        addressEt = (EditText) headView.findViewById(R.id.et_head_publish_job_address);
        typeTv = (TextView) headView.findViewById(R.id.tv_head_publish_job_type);
        areaTv = (TextView) headView.findViewById(R.id.tv_head_publish_job_area);
        lv.addHeaderView(headView);
    }

    private void initPopView() {
        areaTv.setText(UserUtils.readCountry(PublishJobActivity.this).getAddress());
        typePopView = LayoutInflater.from(PublishJobActivity.this).inflate(R.layout.dialog_scn, null);
        ((TextView) typePopView.findViewById(R.id.tv_dialog_scn_title)).setText("项目类型");
        (typePopView.findViewById(R.id.iv_dialog_scn_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typePop.isShowing()) {
                    typePop.dismiss();
                }
            }
        });
        typeLv = (ListView) typePopView.findViewById(R.id.lv_dialog_scn);
        taskTypeList = new ArrayList<>();
        scnDiaAdapter = new ScnDiaAdapter(PublishJobActivity.this, taskTypeList);
        typeLv.setAdapter(scnDiaAdapter);
        typeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publishBean.setType(taskTypeList.get(position));
                publishBean.setTypeId(position + "");
                typeTv.setText(publishBean.getType());
                typePop.dismiss();
            }
        });
        typePop = new PopupWindow(typePopView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        typePop.setFocusable(true);
        typePop.setTouchable(true);
        typePop.setOutsideTouchable(true);
        typePop.setBackgroundDrawable(new BitmapDrawable());
        typePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        skillPopView = LayoutInflater.from(PublishJobActivity.this).inflate(R.layout.dialog_scn, null);
        ((TextView) skillPopView.findViewById(R.id.tv_dialog_scn_title)).setText("招聘工种");
        (skillPopView.findViewById(R.id.iv_dialog_scn_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skillPop.isShowing()) {
                    skillPop.dismiss();
                }
            }
        });
        skillLv = (ListView) skillPopView.findViewById(R.id.lv_dialog_scn);
        skillLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publishWorkerBeanList.get(pickPosition).setId(skillsBeanList.get(position).getS_id());
                publishWorkerBeanList.get(pickPosition).setKind(skillsBeanList.get(position).getS_name());
                adapter.notifyDataSetChanged();
                skillPop.dismiss();
            }
        });
        skillsBeanList = new ArrayList<>();
        selectSkillAdapter = new SelectSkillAdapter(PublishJobActivity.this, skillsBeanList);
        skillLv.setAdapter(selectSkillAdapter);
        skillPop = new PopupWindow(skillPopView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        skillPop.setFocusable(true);
        skillPop.setTouchable(true);
        skillPop.setOutsideTouchable(true);
        skillPop.setBackgroundDrawable(new BitmapDrawable());
        skillPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        taskPopView = LayoutInflater.from(PublishJobActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) taskPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("工作发布成功可在任务管理中查看");
        ((TextView) taskPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("前往查看");
        ((TextView) taskPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("继续发布");
        taskPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskPop.isShowing()) {
                    taskPop.dismiss();
                    startActivity(new Intent(PublishJobActivity.this,EmployerManageActivity.class));
                }
            }
        });
        taskPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskPop.isShowing()) {
                    taskPop.dismiss();
                    startActivity(new Intent(PublishJobActivity.this,PublishJobActivity.class));
                    finish();
                    }
            }
        });
        taskPop = new PopupWindow(taskPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        taskPop.setFocusable(true);
        taskPop.setTouchable(true);
        taskPop.setOutsideTouchable(true);
        taskPop.setBackgroundDrawable(new BitmapDrawable());
        taskPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        sumbitPopView = LayoutInflater.from(PublishJobActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) sumbitPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("确认发布或增加工种");
        ((TextView) sumbitPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("增加工种");
        ((TextView) sumbitPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认发布");
        sumbitPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sumbitPop.isShowing()) {
                    sumbitPop.dismiss();
//                    addWorkerType();
                }
            }
        });
        sumbitPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sumbitPop.isShowing()) {
                    submitOrder();
                }
            }
        });
        sumbitPop = new PopupWindow(sumbitPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        sumbitPop.setFocusable(true);
        sumbitPop.setTouchable(true);
        sumbitPop.setOutsideTouchable(true);
        sumbitPop.setBackgroundDrawable(new BitmapDrawable());
        sumbitPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        finishPopView = LayoutInflater.from(PublishJobActivity.this).inflate(R.layout.pop_dialog_0, null);
        ((TextView) finishPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("是否选择离开");
        ((TextView) finishPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) finishPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        finishPopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finishPop.isShowing()) {
                    finishPop.dismiss();
                }
            }
        });
        finishPopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finishPop.isShowing()) {
                    finishPop.dismiss();
                    finish();
                }
            }
        });
        finishPop = new PopupWindow(finishPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        finishPop.setFocusable(true);
        finishPop.setTouchable(true);
        finishPop.setOutsideTouchable(true);
        finishPop.setBackgroundDrawable(new BitmapDrawable());
        finishPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        addRl.setOnClickListener(this);
        submitRl.setOnClickListener(this);
        titleEt.addTextChangedListener(titleTw);
        descriptionEt.addTextChangedListener(descriptionTw);
        addressEt.addTextChangedListener(addressTw);
        typeTv.setOnClickListener(this);
        areaTv.setOnClickListener(this);
    }

    private void loadData() {
        publishJobPresenter.getTaskType(NetConfig.taskTypeUrl);
        publishJobPresenter.getSkill(NetConfig.skillUrl);
    }

    private void submit() {
        if (TextUtils.isEmpty(publishBean.getTitle())) {
            Utils.toast(PublishJobActivity.this, "标题不能为空！");
        } else if (TextUtils.isEmpty(publishBean.getInfo())) {
            Utils.toast(PublishJobActivity.this, "描述不能为空！");
        } else if (TextUtils.isEmpty(publishBean.getType())) {
            Utils.toast(PublishJobActivity.this, "工程类型不能为空！");
        } else if (TextUtils.isEmpty(publishBean.getArea())) {
            Utils.toast(PublishJobActivity.this, "工作区域不能为空！");
        } else if (TextUtils.isEmpty(publishBean.getAddress())) {
            Utils.toast(PublishJobActivity.this, "详细地址不能为空！");
        } else {
            int idCount = 0;
            for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                if (!TextUtils.isEmpty(publishWorkerBeanList.get(i).getId())) {
                    idCount++;
                }
            }
            if (idCount != publishWorkerBeanList.size()) {
                Utils.toast(PublishJobActivity.this, "招聘工种不能为空！");
            } else {
                int amountCount = 0;
                for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                    if (!TextUtils.isEmpty(publishWorkerBeanList.get(i).getAmount())) {
                        amountCount++;
                    }
                }
                if (amountCount != publishWorkerBeanList.size()) {
                    Utils.toast(PublishJobActivity.this, "工人数量不能为空！");
                } else {
                    int userCount = 0;
                    for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                        if (publishWorkerBeanList.get(i).getAmount().equals("0")) {
                            userCount++;
                        }
                    }
                    if (userCount != 0) {
                        Utils.toast(PublishJobActivity.this, "工人数量不能为0！");
                        return;
                    }
                    int salaryCount = 0;
                    for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                        if (!TextUtils.isEmpty(publishWorkerBeanList.get(i).getSalary())) {
                            salaryCount++;
                        }
                    }
                    if (salaryCount != publishWorkerBeanList.size()) {
                        Utils.toast(PublishJobActivity.this, "工人工资不能为空！");
                    } else {

                        int userSalaryCount = 0;
                        for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                            if (publishWorkerBeanList.get(i).getSalary().equals("0")) {
                                userSalaryCount++;
                            }
                        }
                        if (userSalaryCount != 0) {
                            Utils.toast(PublishJobActivity.this, "工人工资不能为0！");
                            return;
                        }

                        int startTimeCount = 0;
                        for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                            if (!TextUtils.isEmpty(publishWorkerBeanList.get(i).getStartTime())) {
                                startTimeCount++;
                            }
                        }
                        if (startTimeCount != publishWorkerBeanList.size()) {
                            Utils.toast(PublishJobActivity.this, "开始日期不能为空！");
                        } else {
                            int endTimeCount = 0;
                            for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                                if (!TextUtils.isEmpty(publishWorkerBeanList.get(i).getEndTime())) {
                                    endTimeCount++;
                                }
                            }
                            if (endTimeCount != publishWorkerBeanList.size()) {
                                Utils.toast(PublishJobActivity.this, "结束日期不能为空！");
                            } else {
                                int timeCount = 0;
                                for (int i = 0; i < publishWorkerBeanList.size(); i++) {
                                    if (judgeTime(publishWorkerBeanList.get(i).getStartTime(), publishWorkerBeanList.get(i).getEndTime())) {
                                        timeCount++;
                                    }
                                }
                                if (timeCount != publishWorkerBeanList.size()) {
                                    Utils.toast(PublishJobActivity.this, "结束日期不能早于开始日期！");
                                } else {
                                    getNowTime();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public boolean check(){
        boolean checkState = false;
        if (!TextUtils.isEmpty(publishBean.getTitle())) {
            checkState = true;
        } else if (!TextUtils.isEmpty(publishBean.getInfo())) {
            checkState = true;
        }  else if (!TextUtils.isEmpty(publishBean.getPass())) {
            Log.i("Address",publishBean.getAddress());
            checkState = true;
        } else if(!TextUtils.isEmpty(publishBean.getAddress())){
            checkState = true;
        } else if(!TextUtils.isEmpty(publishWorkerBeanList.get(pickPosition).getSalary())){
            checkState = true;
        } else if(!TextUtils.isEmpty(publishWorkerBeanList.get(pickPosition).getAmount())){
            checkState = true;
        }
        return checkState;
    }
    private boolean judgeTime(String startTime, String endTime) {
        String[] startArr = startTime.split("-");
        String[] endArr = endTime.split("-");
        int startYear = Integer.parseInt(startArr[0]);
        int startMonth = Integer.parseInt(startArr[1]);
        int startDay = Integer.parseInt(startArr[2]);
        int endYear = Integer.parseInt(endArr[0]);
        int endMonth = Integer.parseInt(endArr[1]);
        int endDay = Integer.parseInt(endArr[2]);
        if (endYear < startYear) {
            return false;
        } else if (endYear == startYear) {
            if (endMonth < startMonth) {
                return false;
            } else if (endMonth == startMonth) {
                if (endDay < startDay) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_publish_job_return:
                if(check()){
                    backgroundAlpha(0.5f);
                    finishPop.showAtLocation(rootView,Gravity.CENTER,0,0);
                } else {
                    finish();
                }

                break;
            case R.id.rl_publish_job_add:
                addWorkerType();
                break;
            case R.id.rl_change_price_submit:
                submit();
                break;
            case R.id.tv_head_publish_job_type:
                backgroundAlpha(0.8f);
                typePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                break;
            case R.id.tv_head_publish_job_area:
                startActivityForResult(new Intent(PublishJobActivity.this, SelectAddressActivity.class), 1);
                break;
        }
    }
    public void addWorkerType(){
        PublishWorkerBean publishWorkerBean = new PublishWorkerBean("", "", "", "", "", "");
        publishWorkerBeanList.add(publishWorkerBean);
        adapter.notifyDataSetChanged();
        lv.setSelection(publishWorkerBeanList.size());
    }
    private void getNowTime() {
        String timeUrl = NetConfig.timeUrl;
        Request request = new Request.Builder().url(timeUrl).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
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
                        if (beanObj.optInt("code") == 200) {
                            JSONObject dataObj = beanObj.optJSONObject("data");
                            if (dataObj != null) {
                                JSONObject timeObj = dataObj.optJSONObject("datetime");
                                if (timeObj != null) {
                                    nowTime = timeObj.optString("date");
//                                    handler.sendEmptyMessage(TIME_SUCCESS);
                                    if (!TextUtils.isEmpty(nowTime)) {
                                        int timeCount = 0;
                                        for (int j = 0; j < publishBean.getPublishWorkerBeanList().size(); j++) {
                                            if (judgeTime(nowTime, publishBean.getPublishWorkerBeanList().get(j).getStartTime())) {
                                                timeCount++;
                                            }
                                        }
                                        if (timeCount != publishBean.getPublishWorkerBeanList().size()) {
                                           handler.sendEmptyMessage(PUBLISH_FAILURE);
                                        } else {
                                            handler.sendEmptyMessage(SUMBIT_SUCCESS);
                                        }
                                    }

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
    private void submitOrder() {
        String str = DataUtils.getPublishJson(publishBean, true);
        Log.i("xx",str);
        String baseStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        OkHttpClient okhttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("data", baseStr)
                .build();
        Request request = new Request.Builder()
                .url(NetConfig.publishUrl)
                .post(body)
                .build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i("FABURENWU",json);
                    try {
                        JSONObject beanObj = new JSONObject(json);
                        int code = beanObj.optInt("code");
                        switch (code) {
                            case 200:
                                JSONObject dataObj = beanObj.optJSONObject("data");
                                if (dataObj != null) {
                                    tip = dataObj.optString("msg");
                                    int status = dataObj.optInt("status");
                                   if(status==0){
                                        handler.sendEmptyMessage(PUBLISH_SUCCESS);
                                   } else {
                                       handler.sendEmptyMessage(PUBLISH_FAILURE);
                                   }
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 & data != null) {
            SelectAddressBean selectAddressBean = (SelectAddressBean) data.getSerializableExtra("sa");
            if (selectAddressBean != null) {
                areaId = selectAddressBean.getId();
                String[] areaArr = areaId.split(",");
                publishBean.setProvinceId(areaArr[0]);
                publishBean.setCityId(areaArr[1]);
                publishBean.setAreaId(areaArr[2]);
                publishBean.setArea(selectAddressBean.getName());
                areaTv.setText(publishBean.getArea());
            }
        }
    }

    @Override
    public void taskTypeSuccess(List<String> list) {
        taskTypeList.addAll(list);
        handler.sendEmptyMessage(TASK_TYPE_SUCCESS);
    }

    @Override
    public void taskTypeFailure(String failure) {
        handler.sendEmptyMessage(TASK_TYPE_FAILURE);
    }

    @Override
    public void skillSuccess(String json) {
        skillsBeanList.addAll(DataUtils.getSkillBeanList(json));
        handler.sendEmptyMessage(SKILL_SUCCESS);
    }

    @Override
    public void skillFailure(String failure) {
        handler.sendEmptyMessage(SKILL_FAILURE);
    }

    @Override
    public void onClick(int viewId, int viewPosition) {
        pickPosition = viewPosition;
        switch (viewId) {
            case R.id.tv_item_publish_job_kind:
                if (!skillPop.isShowing()) {
                    backgroundAlpha(0.5f);
                    skillPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tv_item_publish_job_start_time:
                pickState = START_TIME;
                datePickerDialog.show();
                break;
            case R.id.tv_item_publish_job_end_time:
                pickState = END_TIME;
                datePickerDialog.show();
                break;
            case R.id.rl_item_publish_job_delete:
                publishWorkerBeanList.remove(viewPosition);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    TextWatcher titleTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            publishBean.setTitle(s.toString());
        }
    };

    TextWatcher descriptionTw = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            publishBean.setInfo(s.toString());
        }
    };

    TextWatcher addressTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            publishBean.setAddress(s.toString());
        }
    };

    private void initLocation() {
        locationClient = new LocationClient(getApplicationContext());
        bdLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(bdLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 0;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        locationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            publishBean.setPositionX(location.getLongitude() + "");
            publishBean.setPositionY(location.getLatitude() + "");
//            publishBean.setAddress(location.getAddrStr());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    public void selectID(String id,final int mode){
        String url = NetConfig.selectAddressBaseUrl + id;
        Request request = new Request.Builder().url(url).get().build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject objBean = new JSONObject(result);
                            if (objBean.optInt("code") == 200) {
                                JSONArray arrData = objBean.optJSONArray("data");
                                if (arrData != null) {
                                    for (int i = 0; i < arrData.length(); i++) {
                                        JSONObject o = arrData.optJSONObject(i);
                                        if (o != null&&mode==0) {
                                            if(o.optString("r_name").equals(countryBean.getLocProvince())){
                                                countryBean.setProvinceID(o.optString("r_id"));
                                                handler.sendEmptyMessage(PROVINCE_ID_SUCCESS);
                                                Log.i("ProvinceID",o.optString("r_id"));
                                            }
                                        } else if(o!=null&&mode==1){
                                            if(o.optString("r_name").equals(countryBean.getLocCity())){
                                                countryBean.setCityID(o.optString("r_id"));
                                                handler.sendEmptyMessage(CITY_ID_SUCCESS);
                                                Log.i("CityID",o.optString("r_id"));
                                            }
                                        } else if(o!=null){
                                            if(o.optString("r_name").equals(countryBean.getDistrict())){
                                                countryBean.setDistrictID(o.optString("r_id"));
                                                handler.sendEmptyMessage(AREA_ID_SUCCESS);
                                                Log.i("AreaID",o.optString("r_id"));
                                            }
                                        }
                                    }
                                }
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
    public void onBackPressed() {
        if(check()){
            finishPop.showAtLocation(rootView,Gravity.CENTER,0,0);
        } else {
            finish();
        }
    }
}
