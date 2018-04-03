package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gjzg.R;
import com.gjzg.adapter.CityAdapter;
import com.gjzg.bean.CityBean;
import com.gjzg.bean.CountryBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;
import com.gjzg.custom.SlideBar;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import city.presenter.CityPresenter;
import city.presenter.ICityPresenter;
import city.view.ICityActivity;

public class CityActivity extends AppCompatActivity implements ICityActivity, View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private CProgressDialog cProgressDialog;
    private SlideBar sb;
    private ListView lv;
    private List<CityBean> list;
    private CityAdapter adapter;
    private ICityPresenter cityPresenter;
    private String[] letter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_city, null);
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
        cityPresenter.destroy();
        if (cityPresenter != null)
            cityPresenter = null;
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_city_return);
        sb = (SlideBar) rootView.findViewById(R.id.sb_city);
        lv = (ListView) rootView.findViewById(R.id.lv_city);
        cProgressDialog = Utils.initProgressDialog(CityActivity.this, cProgressDialog);
    }

    private void initData() {
        letter = getResources().getStringArray(R.array.lowerletter);
        cityPresenter = new CityPresenter(this);
        list = new ArrayList<>();
        adapter = new CityAdapter(this, list);

        Intent intent = getIntent();
        if (intent != null) {
            CityBean cb = (CityBean) intent.getSerializableExtra("cityBean");
            if (cb != null) {
                list.add(cb);
            }
        }
    }

    private void setData() {
        lv.setAdapter(adapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        sb.setOnTouchLetterChangeListenner(new SlideBar.OnTouchLetterChangeListenner() {
            @Override
            public void onTouchLetterChange(MotionEvent event, String s) {
                for (int i = 0; i < list.size(); i++) {
                    if (s.equals(list.get(i).getName())) {
                        lv.setSelection(i);
                    }
                }
            }
        });
        lv.setOnItemClickListener(this);
    }

    private void loadData() {
        cProgressDialog.show();
        loadHot();
//        Intent intent = getIntent();
//        if (intent != null) {
//            CityBigBean cityBigBean = (CityBigBean) intent.getSerializableExtra("cityBigBean");
//            if (cityBigBean != null) {
//                cityPresenter.load(getResources().getStringArray(R.array.lowerletter), cityBigBean);
//            }
//        }

    }

    private void loadHot() {
        OkHttpUtils
                .get()
                .tag(this)
                .url(NetConfig.hotCityUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        cProgressDialog.dismiss();
                        Utils.toast(CityActivity.this, VarConfig.noNetTip);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optInt("code") == 200) {
                                JSONArray jsonArray = jsonObject.optJSONArray("data");
                                if (jsonArray != null && jsonArray.length() != 0) {
                                    CityBean c = new CityBean();
                                    c.setId("-1");
                                    c.setName("热门城市");
                                    list.add(c);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.optJSONObject(i);
                                        if (obj != null) {
                                            CityBean cb = new CityBean();
                                            cb.setId(obj.optString("r_id"));
                                            cb.setName(obj.optString("r_name"));
                                            cb.setHot(obj.optString("r_hot"));
                                            list.add(cb);
                                        }
                                    }
                                    loadCom();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void loadCom() {
        OkHttpUtils
                .get()
                .tag(this)
                .url(NetConfig.comCityUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        cProgressDialog.dismiss();
                        Utils.toast(CityActivity.this, VarConfig.noNetTip);
                    }

                    @Override
                    public void onResponse(String response) {
                        cProgressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optInt("code") == 200) {
                                JSONObject dataObj = jsonObject.optJSONObject("data");
                                if (dataObj != null) {
                                    for (int i = 0; i < letter.length; i++) {
                                        JSONArray arr = dataObj.optJSONArray(letter[i]);
                                        if (arr != null) {
                                            CityBean c = new CityBean();
                                            CountryBean countryBean = new CountryBean();
                                            c.setId("-1");
                                            c.setName(letter[i].toUpperCase());
                                            list.add(c);
                                            for (int j = 0; j < arr.length(); j++) {
                                                JSONObject o = arr.optJSONObject(j);
                                                if (o != null) {
                                                    CityBean cityBean = new CityBean();
                                                    cityBean.setId(o.optString("r_id"));
                                                    cityBean.setPid(o.optString("r_pid"));
                                                    cityBean.setName(o.optString("r_name"));
                                                    cityBean.setHot(o.optString("r_hot"));
                                                    list.add(cityBean);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_city_return:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        CityBean cityBean = list.get(position);
        if (position == 0) {
            String cityId = cityBean.getId();
            if (cityId == null || cityId.equals("null") || TextUtils.isEmpty(cityId)) {
                intent.putExtra(IntentConfig.CITY, new CityBean());
            } else {
                intent.putExtra(IntentConfig.CITY, cityBean);
            }
        } else {
            intent.putExtra(IntentConfig.CITY, list.get(position));
        }
        setResult(IntentConfig.CITY_RESULT, intent);
        finish();
    }

    @Override
    public void showSuccess(List<CityBean> cityBeanList) {
        list.addAll(cityBeanList);
    }

    @Override
    public void showFailure(String failure) {

    }

}
