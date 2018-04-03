package com.gjzg.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.activity.ChangePwdActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.activity.AgreementActivity;
import com.gjzg.activity.CollectActivity;

import com.gjzg.config.NetConfig;

import com.gjzg.activity.MessageActivity;

import com.gjzg.bean.UserBean;

import com.gjzg.activity.LoginActivity;

import mine.presenter.IMinePresenter;
import mine.presenter.MinePresenter;
import mine.view.IMineFragment;

import com.gjzg.activity.MyEvaluateActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.gjzg.activity.SetPwdActivity;
import com.gjzg.activity.RedPacketActivity;
import com.gjzg.activity.SetActivity;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.activity.UserManageActivity;

import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;

import com.gjzg.activity.VoucherActivity;
import com.gjzg.activity.WalletActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class MineFragment extends Fragment implements IMineFragment, View.OnClickListener {

    private View rootView;
    private RelativeLayout collectRl, evaluateRl, walletRl, msgRl, redBagRl, voucherRl;
    private LinearLayout setCashPwdLl, serviceTermLl, settingLl, customerServiceLl,
            shareWXLl,infoLl;
    private TextView loginTv;
    private RelativeLayout loginRightNowRl, loginDoneRl;
    private TextView loginNameTv, loginTipTv;
    private ImageView loginIconIv, loginSexIv, loginOnlineIv;
    private IMinePresenter minePresenter;
    private final int REFRESH_DONE = 1,SHARE_WX = 2;
    private View serviceMobilePopView;
    private TextView serviceMobileContentTv;
    private PopupWindow serviceMobilePop;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case REFRESH_DONE:
                        notifyUserData();
                        break;
                    case SHARE_WX://请求微信分享数据

                        break;
                }
            }
        }
    };
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, null);
        initView();
        initData();
        setListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserUtils.isUserLogin(getActivity())) {
            minePresenter.load(UserUtils.readUserData(getActivity()).getId());
            String serviceMobileUrl = NetConfig.appConfigUrl;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(serviceMobileUrl).get().build();
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
                            if (beanObj.optInt("code") == 1) {
                                JSONObject dataObj = beanObj.optJSONObject("data");
                                if (dataObj != null) {
                                    JSONObject o = dataObj.optJSONObject("data");
                                    if (o != null) {
                                        String serviceMobile = o.optString("service_telephone");
                                        if (!TextUtils.isEmpty(serviceMobile)) {
                                            UserUtils.saveServiceMobile(getActivity(), serviceMobile);
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
        } else {
            loginRightNowRl.setVisibility(View.VISIBLE);
            loginDoneRl.setVisibility(View.GONE);
            loginIconIv.setImageResource(R.mipmap.person_face_default);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(REFRESH_DONE);
            handler = null;
        }
        if (minePresenter != null) {
            minePresenter.destroy();
            minePresenter = null;
        }
    }

    private void initView() {
        initRootView();
        initPopView();
    }

    private void initData() {
        minePresenter = new MinePresenter(this);
    }

    private void initRootView() {
        collectRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_collect);
        evaluateRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_evaluate);
//        walletRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_wallet);
        msgRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_msg);
//        redBagRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_red_bag);
//        voucherRl = (RelativeLayout) rootView.findViewById(R.id.rl_mine_voucher);
//        setCashPwdLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_set_cash_pwd);
        serviceTermLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_service_term);
        settingLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_setting);
        customerServiceLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_customer_service);
//        shareWXLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_share_wx);
//        setPwdLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_set_pwd);
        infoLl = (LinearLayout) rootView.findViewById(R.id.ll_mine_manage_user);
        loginTv = (TextView) rootView.findViewById(R.id.tv_mine_login);
        loginRightNowRl = (RelativeLayout) rootView.findViewById(R.id.rl_frag_me_login_right_now);
        loginDoneRl = (RelativeLayout) rootView.findViewById(R.id.rl_frag_me_login_done);
        loginIconIv = (ImageView) rootView.findViewById(R.id.iv_frag_me_login_icon);
        loginNameTv = (TextView) rootView.findViewById(R.id.tv_frag_me_login_name);
        loginTipTv = (TextView) rootView.findViewById(R.id.tv_frag_me_login_line_tip);
        loginSexIv = (ImageView) rootView.findViewById(R.id.iv_frag_me_login_sex);
        loginOnlineIv = (ImageView) rootView.findViewById(R.id.iv_frag_me_login_online);
    }

    private void initPopView() {
        serviceMobilePopView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_dialog_0, null);
        serviceMobileContentTv = (TextView) serviceMobilePopView.findViewById(R.id.tv_pop_dialog_0_content);
        ((TextView) serviceMobilePopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) serviceMobilePopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("拨打");
        serviceMobilePopView.findViewById(R.id.rl_pop_dialog_0_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceMobilePop.isShowing()) {
                    serviceMobilePop.dismiss();
                }
            }
        });
        serviceMobilePopView.findViewById(R.id.rl_pop_dialog_0_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceMobilePop.isShowing()) {
                    serviceMobilePop.dismiss();
                    Intent in = new Intent(Intent.ACTION_DIAL);
                    in.setData(Uri.parse("tel:" + UserUtils.getServiceMobile(getActivity())));
                    if (in.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(in);
                    }
                }
            }
        });
        serviceMobilePop = new PopupWindow(serviceMobilePopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        serviceMobilePop.setFocusable(true);
        serviceMobilePop.setTouchable(true);
        serviceMobilePop.setOutsideTouchable(true);
        serviceMobilePop.setBackgroundDrawable(new BitmapDrawable());
        serviceMobilePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    private void setListener() {
        collectRl.setOnClickListener(this);
        evaluateRl.setOnClickListener(this);
//        walletRl.setOnClickListener(this);
        msgRl.setOnClickListener(this);
//        redBagRl.setOnClickListener(this);
//        voucherRl.setOnClickListener(this);
//        setCashPwdLl.setOnClickListener(this);
        serviceTermLl.setOnClickListener(this);
        settingLl.setOnClickListener(this);
        customerServiceLl.setOnClickListener(this);
//        shareWXLl.setOnClickListener(this);
//        setPwdLl.setOnClickListener(this);
        infoLl.setOnClickListener(this);
        loginTv.setOnClickListener(this);
        loginOnlineIv.setOnClickListener(this);
        loginIconIv.setOnClickListener(this);
    }

    private void notifyUserData() {
        UserBean userBean = UserUtils.readUserData(getActivity());
        loginRightNowRl.setVisibility(View.GONE);
        loginDoneRl.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(userBean.getIcon())) {
            Picasso.with(getActivity()).load(userBean.getIcon()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(loginIconIv);
        }
        if (TextUtils.isEmpty(userBean.getName())) {
            loginNameTv.setText("游客");
        } else {
            loginNameTv.setText(userBean.getName());
        }
        String sex = userBean.getSex();
        if (!TextUtils.isEmpty(sex)) {
            if (sex.equals("-1")) {
                loginSexIv.setVisibility(View.GONE);
            } else if (sex.equals("0")) {
                loginSexIv.setVisibility(View.VISIBLE);
                loginSexIv.setImageResource(R.mipmap.female);
            } else if (sex.equals("1")) {
                loginSexIv.setVisibility(View.VISIBLE);
                loginSexIv.setImageResource(R.mipmap.male);
            }
        }
        String online = userBean.getOnline();
        if (!TextUtils.isEmpty(online)) {
            if (online.equals("-1")) {
                loginOnlineIv.setImageResource(R.mipmap.user_off_line);
                loginTipTv.setText("隐藏信息");
            } else if (online.equals("0")) {
                loginTipTv.setText("隐藏信息");
                loginOnlineIv.setImageResource(R.mipmap.user_off_line);
            } else if (online.equals("1")) {
                loginOnlineIv.setImageResource(R.mipmap.user_on_line);
                loginTipTv.setText("公开信息");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (UserUtils.isUserLogin(getActivity())) {
            switch (v.getId()) {
                case R.id.rl_mine_collect:
                    startActivity(new Intent(getActivity(), CollectActivity.class));
                    break;
                case R.id.rl_mine_evaluate:
                    startActivity(new Intent(getActivity(), MyEvaluateActivity.class));
                    break;
//                case R.id.rl_mine_wallet:
//                    startActivity(new Intent(getActivity(), WalletActivity.class));
//                    break;
                case R.id.rl_mine_msg:
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                    break;
//                case R.id.rl_mine_red_bag:
//                    startActivity(new Intent(getActivity(), RedPacketActivity.class));
//                    break;
//                case R.id.rl_mine_voucher:
//                    startActivity(new Intent(getActivity(), VoucherActivity.class));
//                    break;
//                case R.id.ll_mine_set_cash_pwd:
//                    startActivity(new Intent(getActivity(), SetPwdActivity.class));
//                    break;
                case R.id.ll_mine_service_term:
                    startActivity(new Intent(getActivity(), AgreementActivity.class));
                    break;
                case R.id.ll_mine_setting:
                    startActivity(new Intent(getActivity(), SetActivity.class));
                    break;
                case R.id.ll_mine_customer_service:
                    if (!serviceMobilePop.isShowing()) {
                        backgroundAlpha(0.5f);
                        serviceMobileContentTv.setText("客服电话：" + UserUtils.getServiceMobile(getActivity()));
                        serviceMobilePop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                    }
                    break;
//                case R.id.ll_mine_share_wx://微信分享
//                    new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN)
//                            .withText("分享").setCallback(shareListener).open();
//                    break;
//                case R.id.ll_mine_set_pwd://设置登录密码
//                    Intent it = new Intent(getActivity(), ChangePwdActivity.class);
//                    it.putExtra("activity","main");
//                    startActivity(it);
//                    break;
                case R.id.ll_mine_manage_user://个人信息
                    startActivity(new Intent(getActivity(),UserManageActivity.class));
                    break;
                case R.id.tv_mine_login:
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                case R.id.iv_frag_me_login_online:
                    String online = UserUtils.readUserData(getActivity()).getOnline();
                    Log.i("MineFragment",online+"");
                    if (online.equals("-1")) {
                        minePresenter.postOnline(UserUtils.readUserData(getActivity()).getId(), "1");
                    } else if (online.equals("1")) {
                        minePresenter.postOnline(UserUtils.readUserData(getActivity()).getId(), "-1");
                    }
                    break;
                case R.id.iv_frag_me_login_icon:
                    startActivity(new Intent(getActivity(), UserManageActivity.class));
                    break;
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void showUserInfoSuccess(UserInfoBean userInfoBean) {
        UserBean userBean = UserUtils.readUserData(getActivity());
        userBean.setId(userInfoBean.getU_id());
        userBean.setName(userInfoBean.getU_true_name());
        userBean.setIcon(userInfoBean.getU_img());
        userBean.setIdcard(userInfoBean.getU_idcard());
        userBean.setOnline(userInfoBean.getU_online());
        Log.d("Mine",userInfoBean.getU_online());
        userBean.setSex(userInfoBean.getU_sex());
        userBean.setMobile(userInfoBean.getU_mobile());
        UserUtils.saveUserData(getActivity(), userBean);
        handler.sendEmptyMessage(REFRESH_DONE);
    }

    @Override
    public void showUserInfoFailure(String failure) {
        handler.sendEmptyMessage(REFRESH_DONE);
    }

    @Override
    public void showPostOnlineSuccess(String success) {
        minePresenter.load(UserUtils.readUserData(getActivity()).getId());
    }

    @Override
    public void showPostOnlineFailure(String failure) {
        handler.sendEmptyMessage(REFRESH_DONE);
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = f;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(layoutParams);
    }
}
