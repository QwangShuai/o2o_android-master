package com.gjzg.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.gjzg.R;
import com.gjzg.activity.CityActivity;
import com.gjzg.activity.LoginActivity;
import com.gjzg.activity.MessageActivity;
import com.gjzg.activity.PublishJobActivity;
import com.gjzg.activity.SkillActivity;
import com.gjzg.activity.TaskActivity;
import com.gjzg.bean.CityBean;
import com.gjzg.bean.CountryBean;
import com.gjzg.bean.LonLatBean;
import com.gjzg.bean.MessageBean;
import com.gjzg.bean.SelectAddressBean;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import firstpage.presenter.FirstPagePresenter;
import firstpage.presenter.IFirstPagePresenter;
import firstpage.view.IFirstPageFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstPageFragment extends Fragment implements IFirstPageFragment, View.OnClickListener {

    private View rootView;
    private RelativeLayout cityRl, msgRl, countRl;
    private ImageView findWorkerIv, findJobIv, sendJobIv,shareIv;
    private TextView cityTv, countTv;
    private CProgressDialog cpd;
    private LocationClient locationClient;
    private BDLocationListener bdLocationListener;
    private IFirstPagePresenter firstpagePresenter;

    private double positionX, positionY;

    private String hotJson, comJson, locCity, locId;

    private final int HOT_DONE = 1, COM_DONE = 2, LOC_DONE = 3, ID_DONE = 4, COUNT_DONE = 5;
    private int count = 0;
    private boolean isLocated = true;
    CountryBean countryBean = new CountryBean();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case HOT_DONE:
                        firstpagePresenter.loadComCity(NetConfig.comCityUrl);
                        break;
                    case COM_DONE:
                        loadCount();
                        if (isLocated) {
                            locationClient.start();
                        } else {
                            if (cpd.isShowing()) {
                                cpd.dismiss();
                            }
                        }
                        break;
                    case LOC_DONE:
                        cityTv.setText(locCity);
                        firstpagePresenter.getLocId(getActivity().getResources().getStringArray(R.array.lowerletter), locCity, comJson);
                        break;
                    case ID_DONE:
                        if (cpd.isShowing()) {
                            cpd.dismiss();
                        }
                        UserUtils.saveLonLat(getActivity(), new LonLatBean(positionX + "", positionY + ""));
                        if (UserUtils.isUserLogin(getActivity())) {
                            firstpagePresenter.changePosition(NetConfig.changePositionUrl + "?u_id=" + UserUtils.readUserData(getActivity()).getId() + "&ucp_posit_x=" + positionX + "&ucp_posit_y=" + positionY);
                        }
                        break;
                    case COUNT_DONE:
                        if (count == 0) {
                            countRl.setVisibility(View.INVISIBLE);
                        } else {
                            countRl.setVisibility(View.VISIBLE);
                            countTv.setText(count + "");
                        }
                        break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_first_page, null);
        initView();
        initData();
        setListener();
        checkLocPermission();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (firstpagePresenter != null) {
            firstpagePresenter.destroy();
            firstpagePresenter = null;
        }
        locationClient.unRegisterLocationListener(bdLocationListener);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCount();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
//        shareIv = (ImageView) rootView.findViewById(R.id.iv_wx_share);
        cityRl = (RelativeLayout) rootView.findViewById(R.id.rl_frag_first_page_city);
        msgRl = (RelativeLayout) rootView.findViewById(R.id.rl_frag_first_page_msg);
        findWorkerIv = (ImageView) rootView.findViewById(R.id.iv_frag_first_page_find_worker);
        findJobIv = (ImageView) rootView.findViewById(R.id.iv_frag_first_page_find_job);
        sendJobIv = (ImageView) rootView.findViewById(R.id.iv_frag_first_page_send_job);
        cityTv = (TextView) rootView.findViewById(R.id.tv_fp_city);
        cpd = Utils.initProgressDialog(getActivity(), cpd);
        countRl = (RelativeLayout) rootView.findViewById(R.id.rl_fragment_first_page_count);
        countTv = (TextView) rootView.findViewById(R.id.tv_fragment_first_page_count);
    }

    private void initData() {
        firstpagePresenter = new FirstPagePresenter(this);
        initLocation();
    }

    private void initLocation() {
        locationClient = new LocationClient(getActivity().getApplicationContext());
        bdLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(bdLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 600000;
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

    private void checkLocPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permisson = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permisson != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                loadData();
            }
        } else {
            loadData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadData();
                } else {
                    isLocated = false;
                    loadData();
                }
                break;
        }
    }

    private void loadData() {
        firstpagePresenter.loadHotCity(NetConfig.hotCityUrl);
    }

    private void setListener() {
        cityRl.setOnClickListener(this);
        msgRl.setOnClickListener(this);
        findWorkerIv.setOnClickListener(this);
        findJobIv.setOnClickListener(this);
        sendJobIv.setOnClickListener(this);
//        shareIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_wx_share:
//                new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN)
//                        .withText("分享").setCallback(shareListener).open();
//                break;
            case R.id.rl_frag_first_page_city:
                Intent cityIntent = new Intent(getActivity(), CityActivity.class);
                CityBean c = new CityBean();
                c.setName(cityTv.getText().toString());
                cityIntent.putExtra("cityBean", c);
//                CityBean c = new CityBean();
//                c.setId(locId);
//                c.setName(locCity);
//                CityBigBean cityBigBean = new CityBigBean();
//                cityBigBean.setCityBean(c);
//                cityBigBean.setComJson(comJson);
//                cityBigBean.setHotJson(hotJson);
//                cityIntent.putExtra("cityBigBean", cityBigBean);
                startActivityForResult(cityIntent, IntentConfig.CITY_REQUEST);
                break;
            case R.id.rl_frag_first_page_msg:
                if (UserUtils.isUserLogin(getActivity())) {
                    String idcard = UserUtils.readUserData(getActivity()).getIdcard();
                    if (TextUtils.isEmpty(idcard)) {
                        Utils.toast(getActivity(), "请完善个人信息");
                    } else {
                        startActivity(new Intent(getActivity(), MessageActivity.class));
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.iv_frag_first_page_find_worker:
                startActivity(new Intent(getActivity(), SkillActivity.class));
                break;
            case R.id.iv_frag_first_page_find_job:
                startActivity(new Intent(getActivity(), TaskActivity.class));
                break;
            case R.id.iv_frag_first_page_send_job:
                if (UserUtils.isUserLogin(getActivity())) {
                    String idcard = UserUtils.readUserData(getActivity()).getIdcard();
                    String pass = UserUtils.readUserData(getActivity()).getPass();
                    if (idcard == null || TextUtils.isEmpty(idcard) || idcard.equals("null")) {
                        Utils.toast(getActivity(), "请完善个人信息");
//                    } else if (pass == null || TextUtils.isEmpty(pass) || pass.equals("null")) {
//                        Utils.toast(getActivity(), "请设置交易密码");
                    } else {
                        startActivity(new Intent(getActivity(), PublishJobActivity.class));
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentConfig.CITY_REQUEST && resultCode == IntentConfig.CITY_RESULT && data != null) {
            CityBean cityBean = (CityBean) data.getSerializableExtra(IntentConfig.CITY);
            if (cityBean != null) {
                if (TextUtils.isEmpty(cityBean.getName())) {
                    cityTv.setText("定位中...");
                } else {
                    cityTv.setText(cityBean.getName());
                    countryBean.setCityID(cityBean.getId());
                    countryBean.setProvinceID(cityBean.getPid());
                }
            }
        }
    }

    @Override
    public void showHotSuccess(String json) {
        hotJson = json;
        handler.sendEmptyMessage(HOT_DONE);
    }

    @Override
    public void showHotFailure(String failure) {

    }

    @Override
    public void showComSuccess(String json) {
        comJson = json;
        handler.sendEmptyMessage(COM_DONE);
    }

    @Override
    public void showComFailure(String failure) {

    }

    @Override
    public void showLocIdSuccess(String id) {
        locId = id;
        handler.sendEmptyMessage(ID_DONE);
    }

    @Override
    public void showLocIdFailure(String failure) {

    }

    @Override
    public void changePositionSuccess(String json) {
    }

    @Override
    public void changePositionFailure(String failure) {
    }

    private void loadCount() {
        if (UserUtils.isUserLogin(getActivity())) {
            String url = NetConfig.msgListUrl +
                    "?u_id=" + UserUtils.readUserData(getActivity()).getId();
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
                        List<MessageBean> messageBeanList = DataUtils.getMessageBeanList(json);
                        count = 0;
                        for (int i = 0; i < messageBeanList.size(); i++) {
                            if (messageBeanList.get(i).getUm_status().equals("0")) {
                                count++;
                            }
                        }
                        handler.sendEmptyMessage(COUNT_DONE);
                    }
                }
            });
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            positionX = location.getLongitude();
            positionY = location.getLatitude();
            Log.i("myCountry",location.getProvince()+location.getCity()+location.getDistrict());

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息
            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            String str = sb.toString();
            int cnIndex = str.indexOf("中国");
            int provinceIndex = str.indexOf("省");
            int cityIndex = str.indexOf("市");
            final int districtIndex ;
            if(location.getDistrict().indexOf("区")!=-1){
                districtIndex = location.getDistrict().indexOf("区");
            } else {
                districtIndex = location.getDistrict().indexOf("县");
            }

            locCity = str.substring(provinceIndex + 1, cityIndex);
            handler.sendEmptyMessage(LOC_DONE);
            countryBean.setProvince(location.getProvince());
            countryBean.setCity(location.getCity());
            countryBean.setDistrict(location.getDistrict());
            countryBean.setLocProvince(str.substring(cnIndex+2, provinceIndex));
            countryBean.setLocCity(str.substring(provinceIndex + 1, cityIndex));
            countryBean.setLocDistrict(location.getDistrict().substring(0, districtIndex));
            Log.i("ProvunceCity",str.substring(cnIndex+2, provinceIndex));
            UserUtils.saveCountry(getActivity(),countryBean);

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i("MineFragment","开始了");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.i("MineFragment","成功了");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.i("MineFragment","失败了");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.i("MineFragment","取消了");
        }
    };
}
