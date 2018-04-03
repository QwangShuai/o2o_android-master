package com.gjzg.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.activity.PublishJobActivity;
import com.gjzg.bean.CountryBean;
import com.gjzg.config.NetConfig;
import com.gjzg.adapter.AddSkillAdapter;
import com.gjzg.adapter.EditInfoAdapter;
import editinfo.listener.AddSkillClickHelp;
import editinfo.listener.EditInfoClickHelp;
import editinfo.presenter.EditInfoPresenter;
import editinfo.presenter.IEditInfoPresenter;
import editinfo.view.IEditInfoFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.bean.SelectAddressBean;
import com.gjzg.activity.SelectAddressActivity;
import com.gjzg.bean.SkillsBean;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.activity.UserManageActivity;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人信息填写
 */
public class EditInfoFragment extends Fragment implements IEditInfoFragment, View.OnClickListener, EditInfoClickHelp, AddSkillClickHelp {

    private View rootView,myView;
    private RelativeLayout submitRl;
    private ListView lv;
    private CProgressDialog cpd;
    private UserInfoBean userInfoBean;
    private EditInfoAdapter editInfoAdapter;
    private TextView tv_area;
    private View addSkillPopView;
    private PopupWindow addSkillPop;
    private ListView addSkillLv;
    private List<SkillsBean> selectSkillList = new ArrayList<>();
    private AddSkillAdapter addSkillAdapter;
    private List<SkillsBean> selectList = new ArrayList<>();

    private IEditInfoPresenter editInfoPresenter;
    private CountryBean countryBean;
    private final int SKILL_SUCCESS = 2;
    private final int SKILL_FAILURE = 3;
    private final int PROVINCE_ID_SUCCESS = 8;
    private final int CITY_ID_SUCCESS = 9;
    private final int AREA_ID_SUCCESS = 10;
    private List<SkillsBean> skillsBeanList = new ArrayList<>();
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        addSkillAdapter.notifyDataSetChanged();
                        break;
                    case SKILL_SUCCESS:
                        notifyData();
                        break;
                    case SKILL_FAILURE:
                        break;
                    case PROVINCE_ID_SUCCESS:
                        selectID(countryBean.getProvinceID(),1);
                        break;
                    case CITY_ID_SUCCESS:
                        selectID(countryBean.getCityID(),2);
                        break;
                    case AREA_ID_SUCCESS:
                        userInfoBean.setUei_province(countryBean.getProvinceID());
                        userInfoBean.setUei_city(countryBean.getCityID());
                        userInfoBean.setUei_area(countryBean.getDistrictID());
                        break;
                }
            }
        }
    };

    private Handler userManageHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_editinfo, null);
        initView();
        initData();
        setListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (editInfoPresenter != null) {
            editInfoPresenter.destroy();
            editInfoPresenter = null;
        }
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
        initPopView();
    }

    private void initRootView() {
        lv = (ListView) rootView.findViewById(R.id.lv_editinfo);
        myView = LayoutInflater.from(getActivity()).inflate(R.layout.item_editinfo,null);
        tv_area = (TextView) myView.findViewById(R.id.tv_item_editinfo_area);
        cpd = Utils.initProgressDialog(getActivity(), cpd);
        submitRl = (RelativeLayout) rootView.findViewById(R.id.rl_editinfo_submit);
        userInfoBean = ((UserManageActivity) getActivity()).userInfoBean;
    }

    private void initPopView() {
        addSkillPopView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_add_skill, null);
        addSkillLv = (ListView) addSkillPopView.findViewById(R.id.lv_add_skill);
        (addSkillPopView.findViewById(R.id.rl_pop_add_skill_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSkillPop.dismiss();
            }
        });
        (addSkillPopView.findViewById(R.id.rl_pop_add_skill_sure)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSkillPop.dismiss();
                selectList.clear();
                for (int i = 0; i < selectSkillList.size(); i++) {
                    if (selectSkillList.get(i).isCheck()) {
                        selectList.add(selectSkillList.get(i));
                    }
                }
                if (selectList.size() != 0) {
                    if (skillsBeanList == null) {
                        skillsBeanList.addAll(selectList);
                    } else {
                        skillsBeanList.addAll(defList());
                    }
                    changeSkill(skillsBeanList);
                    editInfoAdapter.notifyDataSetChanged();
                    for (int i = 0; i < selectSkillList.size(); i++) {
                        selectSkillList.get(i).setCheck(false);
                    }
                    addSkillAdapter.notifyDataSetChanged();
                }
            }
        });
        addSkillPop = new PopupWindow(addSkillPopView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        addSkillPop.setFocusable(true);
        addSkillPop.setTouchable(true);
        addSkillPop.setOutsideTouchable(true);
        addSkillPop.setBackgroundDrawable(new BitmapDrawable());
        addSkillPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        selectSkillList = new ArrayList<>();
        addSkillAdapter = new AddSkillAdapter(getActivity(), selectSkillList, this);
        addSkillLv.setAdapter(addSkillAdapter);
    }

    private void initData() {
        editInfoPresenter = new EditInfoPresenter(this);
        userManageHandler = ((UserManageActivity) getActivity()).handler;
        countryBean = UserUtils.readCountry(getActivity());
        if("".equals(tv_area.getText().toString())){
            if(!"null".equals(countryBean.getProvince())||"".equals(countryBean.getProvince())){
                userInfoBean.setUser_area_name(countryBean.getAddress());
                selectID("1",0);
            } else {
                userInfoBean.setUser_area_name("黑龙江省"+" "+"哈尔滨市"+" "+"道里区");
                userInfoBean.setUei_province("12");
                userInfoBean.setUei_city("167");
                userInfoBean.setUei_area("1415");
            }
        }
    }

    private void setListener() {
        submitRl.setOnClickListener(this);
    }

    private void loadData() {
//        userInfoBean.setUei_area(UserUtils.readCountry(getActivity()).getDistrictID());
        editInfoPresenter.skill(NetConfig.skillUrl + "?s_id=" + userInfoBean.getU_skills());

    }

    private void notifyData() {
        editInfoAdapter = new EditInfoAdapter(getActivity(), userInfoBean, skillsBeanList, this);
        lv.setAdapter(editInfoAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_editinfo_submit:
                judgeSubmit();
                break;
        }
    }

    private void judgeSubmit() {
        if (TextUtils.isEmpty(userInfoBean.getU_true_name())) {
            Utils.toast(getActivity(), "请输入姓名");
        }  else if (TextUtils.isEmpty(userInfoBean.getUser_area_name())) {
            Utils.toast(getActivity(), "请选择现居地");
        }  else if (TextUtils.isEmpty(userInfoBean.getU_info())) {
            Utils.toast(getActivity(), "请输入个人简介");
        } else if (userInfoBean.getU_skills().equals("-1")) {
            Utils.toast(getActivity(), "请添加工种");
        } else if(TextUtils.isEmpty(userInfoBean.getUei_address())){
            Utils.toast(getActivity(),"请输入详细地址");
        } else {
            if(userInfoBean.getUser_area_name().equals("黑龙江省 哈尔滨市 道外区")){
                userInfoBean.setUei_province("0");
                userInfoBean.setUei_city("0");
                userInfoBean.setUei_area("0");
            }
            userInfoBean.setU_online(UserUtils.readUserData(getActivity()).getOnline());
            editInfoPresenter.submit(userInfoBean);
        }
    }

    @Override
    public void onClick(int id, int position, int checkedId) {
        switch (id) {
            case R.id.rg_item_editinfo_sex:
                switch (checkedId) {
                    case R.id.rb_item_editinfo_male:
                        userInfoBean.setU_sex("1");
                        break;
                    case R.id.rb_item_editinfo_female:
                        userInfoBean.setU_sex("0");
                        break;
                }
                break;
            case R.id.tv_item_editinfo_area:
                startActivityForResult(new Intent(getActivity(), SelectAddressActivity.class), 1);
                break;
            case R.id.rg_item_editinfo_role:
                switch (checkedId) {
                    case R.id.rb_item_editinfo_employer:
                        userInfoBean.setU_skills("0");
                        skillsBeanList.clear();
                        notifyData();
                        break;
                    case R.id.rb_item_editinfo_worker:
                        userInfoBean.setU_skills("-1");
                        skillsBeanList.clear();
                        notifyData();
                        break;
                }
                break;
            case R.id.gv_item_editinfo:
                skillsBeanList.remove(position);
                changeSkill(skillsBeanList);
                break;
            case R.id.iv_item_editinfo_addskill:
                backgroundAlpha(0.5f);
                addSkillPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                editInfoPresenter.load(NetConfig.skillUrl);
                break;
        }
        editInfoAdapter.notifyDataSetChanged();
    }

    private void changeSkill(List<SkillsBean> skillsBeanList) {
        if (skillsBeanList != null) {
            if (skillsBeanList.size() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(",");
                for (int i = 0; i < skillsBeanList.size(); i++) {
                    sb.append(skillsBeanList.get(i).getS_id() + ",");
                }
                userInfoBean.setU_skills(sb.toString());
            } else {
                userInfoBean.setU_skills("-1");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1 && data != null) {
            SelectAddressBean selectAddressBean = (SelectAddressBean) data.getSerializableExtra("sa");
            if (selectAddressBean != null) {
                String[] idArr = selectAddressBean.getId().split(",");
                userInfoBean.setUser_area_name(selectAddressBean.getName());
                userInfoBean.setUei_province(idArr[0]);
                userInfoBean.setUei_city(idArr[1]);
                userInfoBean.setUei_area(idArr[2]);
                editInfoAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(int id, int position, boolean checked) {
        switch (id) {
            case R.id.cb_item_add_kind_checked:
                selectSkillList.get(position).setCheck(checked);
                break;
        }
        addSkillAdapter.notifyDataSetChanged();
    }

    @Override
    public void skillSuccess(String json) {
        skillsBeanList.clear();
        skillsBeanList.addAll(DataUtils.getSkillBeanList(json));
        handler.sendEmptyMessage(SKILL_SUCCESS);
    }

    @Override
    public void skillFailure(String failure) {

    }

    @Override
    public void showAddSkillSuccess(List<SkillsBean> skillsBeanList) {
        selectSkillList.clear();
        selectSkillList.addAll(skillsBeanList);
        handler.sendEmptyMessage(1);
    }

    @Override
    public void showAddSkillFailure(String failure) {

    }

    @Override
    public void showSubmitSuccess(String success) {
        Utils.toast(getActivity(), success);
        userManageHandler.sendEmptyMessage(1);
    }

    @Override
    public void showSubmitFailure(String failure) {
        Utils.toast(getActivity(), failure);
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(layoutParams);
    }

    private List<SkillsBean> defList() {
        if (skillsBeanList != null) {
            if (skillsBeanList.size() != 0) {
                List<SkillsBean> resultList = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    if (isDiff(i)) {
                        resultList.add(selectList.get(i));
                    }
                }
                return resultList;
            }
        }
        return selectList;
    }

    private boolean isDiff(int index) {
        int count = 0;
        for (int i = 0; i < skillsBeanList.size(); i++) {
            if (selectList.get(index).getS_id().equals(skillsBeanList.get(i).getS_id())) {
                count++;
            }
        }
        if (count == 0) {
            return true;
        } else {
            return false;
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
}
