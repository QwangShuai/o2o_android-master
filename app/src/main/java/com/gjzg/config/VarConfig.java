package com.gjzg.config;

public interface VarConfig {

    //收藏成功
    String collectSuccess = "收藏成功";
    //收藏失败
    String collectFailure = "收藏失败";
    //已收藏过了
    String collectDone = "已收藏过了";
    //不能收藏自己
    String cannotCollectSelf = "不能收藏自己";
    //无网络提示
    String noNetTip = "无网络";
    //发布失败
    String publishSuccess = "发布成功";
    String publishFailure = "发布失败";
    String publishNoMoney = "资金不足";
    String publishNoPass = "密码错误";

    String noNet = "noNet";
    String pwdSuccessTip = "设置密码成功";//设置密码成功提示
    String pwdDifferTip = "两次密码不一致";//两次密码不一致提示
    String pwdErrorTip = "原密码不正确";//原密码不正确
    int PWD_COUNT_DOWN = 60;//重新设置密码手机验证码倒计时(单位：秒)
    int GET_LOGIN_CODE_DOWN = 61;//获取登录验证码倒计时时间(单位：秒)
    String pwdResendTip = "重新发送";//重新发送提示
    String phoneErrorTip = "手机号格式不正确";//验证手机号格式
    String getPwdTip = "获取动态密码";//获取动态密码提示
}
