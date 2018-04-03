package com.gjzg.config;


public interface StateConfig {

    //加载状态
    public static final int LOAD_NO_NET = 0;
    public static final int LOAD_DONE = 1;
    public static final int LOAD_REFRESH = 2;
    public static final int LOAD_LOAD = 3;

    //第一条路 雇主招工人 5种状态
    int NO_YOURSELF = 1;//您不能招工自己
    int TALK_TO_YOU = 2;//此工人正在与您洽谈
    int DOING_FOR_YOU = 3;//此工人正在您的任务中工作
    int BUSY = 4;//此工人正在工作中
    int WAIT = 5;//我要招工

    //加载结果
    public static final String loadNonet = "网络异常";
    public static final String loadRefreshSuccess = "刷新成功";
    public static final String loadRefreshFailure = "刷新失败，请检查网络设置";
    public static final String loadLoadSuccess = "加载成功";
    public static final String loadLoadFailure = "加载失败，请检查网络设置";

    //客服电话
    public static final String cusSevNumber = "4000788889";

    //获取动态密码间隔时间（秒）
    public static final int getMovePwdSec = 3;
}
