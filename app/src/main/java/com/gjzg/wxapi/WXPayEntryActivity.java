package com.gjzg.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gjzg.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.gjzg.config.AppConfig;
import com.gjzg.utils.Utils;
import com.gjzg.activity.WalletActivity;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
        api = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case 0:
                Utils.toast(WXPayEntryActivity.this, "支付成功");
                startActivity(new Intent(WXPayEntryActivity.this, WalletActivity.class));
                break;
            case -1:
                Utils.toast(WXPayEntryActivity.this, "支付失败");
                startActivity(new Intent(WXPayEntryActivity.this, WalletActivity.class));
                break;
            case -2:
                startActivity(new Intent(WXPayEntryActivity.this, WalletActivity.class));
                break;
        }
    }
}