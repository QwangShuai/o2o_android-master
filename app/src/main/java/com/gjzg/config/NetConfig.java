package com.gjzg.config;

public interface NetConfig {

    //钢建网
    String NetHead = "https://api.gangjianwang.com";
//    String NetHead = "http://192.168.1.240";
//    String NetHead = "http://s-api.gangjianwang.com";
//    String NetHead = "http://api.gangjianwang.com";
    //地区
    String regionUrl = NetHead + "/Regions/index";
    //工种
    String skillUrl = NetHead + "/Skills/index";
    //工人
    String workerUrl = NetHead + "/Users/getUsers";
    //收藏的工人
    String collectWorkerUrl = NetHead + "/Users/favorateUsers";
    //收藏的任务
    String collectTaskUrl = NetHead + "/Users/favorateTasks";
    //添加收藏
    String addCollectUrl = NetHead + "/Users/favorateAdd";
    //取消收藏
    String delCollectUrl = NetHead + "/Users/favorateDel";
    //修改位置
    String changePositionUrl = NetHead + "/Users/updatePosition";
    //用户详细信息
    String userInfoUrl = NetHead + "/Users/usersInfo?u_id=";
    //他人评价
    String otherEvaluateUrl = NetHead + "/Users/otherCommentUser";
    //评价他人
    String evaluateOtherUrl = NetHead + "/Users/userCommentOther";
    //用户余额
    String userFundUrl = NetHead + "/Users/usersFunds";
    //红包、代金券
    String redBagUrl = NetHead + "/Bouns/index";
    //热门城市
    String hotCityUrl = NetHead + "/Regions/index?action=hot";
    //城市列表
    String comCityUrl = NetHead + "/Regions/index?action=letter";
    //订单
    String orderUrl = NetHead + "/Orders/index";
    //投诉问题
    String complainTypeUrl = NetHead + "/Users/complaintsType";
    //投诉提交
    String complainSubmitUrl = NetHead + "/Users/complaintsAdd";
    //配置文件
    String appConfigUrl = NetHead + "/ApplicationConfig/getAppConfig";
    //评论添加
    String commentAddUrl = NetHead + "/Users/commentAdd";
    //时间
    String timeUrl = NetHead + "/Tools/index";
    //文章
    String articleUrl = NetHead + "/Articles/articlesInfo?ac_id=29";
    //站内信
    String msgListUrl = NetHead + "/Users/msgList";
    //站内信修改
    String msgEditUrl = NetHead + "/Users/msgReadEdit";
    //站内信删除
    String msgDelUrl = NetHead + "/Users/msgDel";
    //充值申请
    String toPayUrl = NetHead + "/Users/applyRechargeLog";
    //文章
    String articlesListUrl = NetHead + "/Articles/articlesList?ac_id=29";
    //文章详情
    String articlesInfoUrl = NetHead + "/Articles/articlesInfo";
    //引导密码
    String guidePwdUrl = NetHead + "/tools/internal";
    //锁
    String lockUrl = NetHead + "/Tools/lock";
    //优惠
    String discountUrl = NetHead + "/activities.php";
    //更新
    String updateUrl = NetHead + "/static/app/xinyonggong_android.apk";
    //注册
    String registerUrl = NetHead + "/Users/register";
    //设置交易密码
    String setPwdUrl = NetHead + "/Users/setPassword";
    //获取任务类型
    String taskTypeUrl = NetHead + "/Tools/taskType";
    String editPwdUrl = NetHead + "/Users/passwordEdit";
    String fgtPwdCodeUrl = NetHead + "/Users/passwordEdit?u_mobile=";
    String fgtPwdUrl = NetHead + "/Users/passwordEdit";
    String provePhoneUrl = NetHead + "/Users/passwordEdit";
    String proveIdcardUrl = NetHead + "/Users/passwordEdit";
    String proveOriPwdUrl = NetHead + "/Users/passwordEdit";
    //更改用户信息
    String userInfoEditUrl = NetHead + "/Users/usersInfoEdit";
    //更改任务state
    String taskBaseUrl = NetHead + "/Tasks/index";
    String codeUrl = NetHead + "/Users/sendVerifyCode";
    String loginUrl = NetHead + "/Users/login";
    String payWayUrl = NetHead + "/Payments/index";
    String iconUpdateUrl = NetHead + "/Users/usersHeadEidt";
    String applyWithdrawUrl = NetHead + "/Users/applyWithdraw";
    String useCplIs = NetHead + "/Users/complaintsType?ct_type=";
    //选择省所在地
    String selectAddressBaseUrl = NetHead + "/Regions/index?action=list&r_pid=";
    String usersFundsLogUrl = NetHead + "/Users/getUsersFundsLog";
    String favorateAddUrl = NetHead + "/Users/favorateAdd";
    String favorateDelUrl = NetHead + "/Users/favorateDel";
    String subTotalUrl = NetHead + "/Tools/subTotal";
    //发布任务
    String publishUrl = NetHead + "/Tasks/index?action=publish";
}
