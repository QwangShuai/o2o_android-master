package com.gjzg.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.gjzg.config.AppConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class CApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        UMShareAPI.get(this);
        Config.DEBUG = true ;
    }
    {
        PlatformConfig.setWeixin(AppConfig.APP_ID,AppConfig.WX_KEY);
    }
}
