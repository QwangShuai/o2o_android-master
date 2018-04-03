package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.gjzg.bean.FundBean;
import com.gjzg.singleton.SingleGson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;

public class WalletActivity extends AppCompatActivity{

    @BindView(R.id.rl_wallet_return)
    RelativeLayout rlWalletReturn;
    @BindView(R.id.rl_wallet_detail)
    RelativeLayout rlWalletDetail;
    @BindView(R.id.tv_wallet_overage)
    TextView tvWalletOverage;
    @BindView(R.id.rl_wallet_recharge)
    RelativeLayout rlWalletRecharge;
    @BindView(R.id.rl_wallet_withdraw)
    RelativeLayout rlWalletWithdraw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @OnClick({R.id.rl_wallet_return,R.id.rl_wallet_detail,R.id.rl_wallet_recharge,R.id.rl_wallet_withdraw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wallet_return:
                startActivity(new Intent(WalletActivity.this, MainActivity.class));
                break;
            case R.id.rl_wallet_detail:
                startActivity(new Intent(WalletActivity.this, AccountDetailActivity.class));
                break;
            case R.id.rl_wallet_recharge:
                startActivity(new Intent(WalletActivity.this, RechargeActivity.class));
                break;
            case R.id.rl_wallet_withdraw:
                startActivity(new Intent(WalletActivity.this, WithDrawActivity.class));
                break;
        }
    }

    private void loadData() {
        String url = NetConfig.userFundUrl + "?u_id=" + UserUtils.readUserData(WalletActivity.this).getId();
        OkHttpUtils.get().tag(this).url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                Utils.toast(WalletActivity.this, VarConfig.noNetTip);
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    FundBean fundBean = SingleGson.getInstance().fromJson(response, FundBean.class);
                    if (fundBean != null) {
                        if (fundBean.getCode() == 1) {
                            if (fundBean.getData() != null) {
                                if (fundBean.getData().getData() != null) {
                                    if (!TextUtils.isEmpty(fundBean.getData().getData().getUef_overage()))
                                        tvWalletOverage.setText(fundBean.getData().getData().getUef_overage());
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
