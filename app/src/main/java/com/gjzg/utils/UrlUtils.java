package com.gjzg.utils;

import android.content.Context;

import com.gjzg.config.NetConfig;


public class UrlUtils {

    public static String getEmployerManageUrl(Context context, int state) {
        String url = null;
        switch (state) {
            case 0:
                url = NetConfig.taskBaseUrl + "?t_author=" + (UserUtils.readUserData(context)).getId() + "&t_storage=0";
                break;
            case 1:
                url = NetConfig.taskBaseUrl + "?t_author=" + (UserUtils.readUserData(context)).getId() + "&t_storage=0&t_status=0";
                break;
            case 2:
                url = NetConfig.taskBaseUrl + "?t_author=" + (UserUtils.readUserData(context)).getId() + "&t_storage=0&t_status=1";
                break;
            case 3:
                url = NetConfig.taskBaseUrl + "?t_author=" + (UserUtils.readUserData(context)).getId() + "&t_storage=0&t_status=-3,2,5";
                break;
            case 4:
                url = NetConfig.taskBaseUrl + "?t_author=" + (UserUtils.readUserData(context)).getId() + "&t_storage=0&t_status=3,4";
                break;
        }
        return url;
    }

    public static String getUsersFundsLogUrl(Context context, int logState) {
        String url = null;
        switch (logState) {
            case 0:
                url = NetConfig.usersFundsLogUrl + "?u_id=" + UserUtils.readUserData(context).getId() + "&category=all";
                break;
            case 1:
                url = NetConfig.usersFundsLogUrl + "?u_id=" + UserUtils.readUserData(context).getId() + "&category=withdraw";
                break;
            case 2:
                url = NetConfig.usersFundsLogUrl + "?u_id=" + UserUtils.readUserData(context).getId() + "&category=recharge";
                break;
        }
        return url;
    }

}
