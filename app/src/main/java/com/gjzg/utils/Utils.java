package com.gjzg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gjzg.R;
import com.gjzg.activity.LoginActivity;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.config.NetConfig;
import com.gjzg.custom.CProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void dark(Activity activity) {
        if (activity != null) {
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            layoutParams.alpha = 0.5f;
            activity.getWindow().setAttributes(layoutParams);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    public static void light(Activity activity) {
        if (activity != null) {
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            layoutParams.alpha = 1.0f;
            activity.getWindow().setAttributes(layoutParams);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**Intent in = new Intent(Intent.ACTION_DIAL);
     in.setData(Uri.parse("tel:" + "?"));
     if (in.resolveActivity(getPackageManager()) != null) {
     startActivity(in);}
     */

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id) {
        if (id == null || id.equals("null") || TextUtils.isEmpty(id)) {
            return false;
        }
        if (id.matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    public static String Bitmap2StrByBase64(Bitmap bit) {
        if (bit != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
            byte[] bytes = bos.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            return null;
        }
    }

    //吐司
    public static void toast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }

    //日志
    public static void log(Context c, String s) {
    }

    //设置ListView高度
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //设置GridView高度
    public static void setGridViewHeight(GridView gridview, int numColumes) {
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i = i + numColumes) {
            View listItem = listAdapter.getView(i, null, gridview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        gridview.setLayoutParams(params);
    }

    //手机号
    public static boolean isPhonenumber(String str) {
        String regularStr = "[1][34578]\\d{9}";
        Pattern p = Pattern.compile(regularStr);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //判断SD卡
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    //验证密码是否是8-16位的字母数字混合
    public static boolean isMatcherFinded(String input) {
        Pattern pattern = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\\\D+$).{8,16}$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    //MD5加密
    public static String md5Encode(String inStr)
            throws UnsupportedEncodingException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    //跳到浏览器
    public static void skipBrowser(Context context, String url) {
        if (context != null && !TextUtils.isEmpty(url)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }

    //跳到登录
    public static void skipLogin(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }


    public static CProgressDialog initProgressDialog(Context context, CProgressDialog cpd) {
        return cpd = new CProgressDialog(context, R.style.dialog_cprogress);
    }

    //获取版本号
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String appName = info.applicationInfo.loadLabel(manager).toString();
            String version = info.versionName;
            return appName + version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void caculateLongLat(double longitude, double latitude, double distance) {
        double pi = Math.PI;
        double radius = 6371229;
        double x = (180 * distance) / (pi * radius * Math.cos(longitude * pi / 180));
        double y = (180 * distance) / (pi * radius * Math.cos(latitude * pi / 180));
    }

    public static String getLocCityId(Context context, String cityName, String json) {
        String cityId = null;
        try {
            JSONObject objBean = new JSONObject(json);
            if (objBean.optInt("code") == 200) {
                JSONObject objData = objBean.optJSONObject("data");
                String[] arr = context.getResources().getStringArray(R.array.lowerletter);
                for (int i = 0; i < arr.length; i++) {
                    JSONArray arrLetter = objData.optJSONArray(arr[i]);
                    if (arrLetter != null) {
                        for (int j = 0; j < arrLetter.length(); j++) {
                            JSONObject o = arrLetter.optJSONObject(j);
                            if (o != null) {
                                if (cityName.equals(o.optString("r_name"))) {
                                    cityId = o.optString("r_id");
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityId;
    }

    //头像上传url
    public static String getIconUpdateUrl(String userId, String imgName) {
        return NetConfig.iconUpdateUrl + "?u_id=" + userId + "&img_name=" + imgName;
    }

    //切割出json
    public static String cutJson(String result) {
        if (TextUtils.isEmpty(result)) {
            return null;
        } else {
            int first = result.indexOf("{");
            return result.substring(first);
        }
    }

    //获取用户投诉问题url
    public static String getUserCplIsUrl(String baseUrl, String typeId) {
        return baseUrl + typeId;
    }

    //个人信息工种
    public static String getUserSkillUrl(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            String str = userInfoBean.getU_skills();
            int a = str.indexOf(",");
            int b = str.lastIndexOf(",");
            String skill = str.substring(a + 1, b);
            return NetConfig.skillUrl + "?s_id=" + skill;
        }
        return null;
    }

    public static String getWorkerManageUrl(Context context, int state) {
        String url = null;
        switch (state) {
            case 0:
                url = NetConfig.taskBaseUrl + "?action=worked&o_worker=" + (UserUtils.readUserData(context)).getId();
                break;
            case 1:
                url = NetConfig.taskBaseUrl + "?action=worked&o_worker=" + (UserUtils.readUserData(context)).getId() + "&o_status=0&o_confirm=0,2";
                break;
            case 2:
                url = NetConfig.taskBaseUrl + "?action=worked&o_worker=" + (UserUtils.readUserData(context)).getId() + "&o_status=0&o_confirm=1";
                break;
            case 3:
                url = NetConfig.taskBaseUrl + "?action=worked&o_worker=" + (UserUtils.readUserData(context)).getId() + "&o_status=1";
                break;
        }
        return url;
    }


    /**
     * private void backgroundAlpha(float f) {
     WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
     layoutParams.alpha = f;
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
     getWindow().setAttributes(layoutParams);}
     * */

    /**private void initEmptyView() {
     fl = (FrameLayout) rootView.findViewById(R.id.fl);
     emptyView = LayoutInflater.from(this).inflate(R.layout.empty_data, null);
     fl.addView(emptyView);
     emptyView.setVisibility(View.GONE);
     netView = LayoutInflater.from(his).inflate(R.layout.empty_net, null);
     netTv = (TextView) netView.findViewById(R.id.tv_no_net_refresh);
     netTv.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
    ptrl.setVisibility(View.VISIBLE);
    netView.setVisibility(View.GONE);
    STATE = FIRST;
    loadData();
    }
    });
     fl.addView(netView);
     netView.setVisibility(View.GONE);
     }
     * */

    /**private void notifyData() {
     switch (STATE) {
     case FIRST:
     cpd.dismiss();
     if (list.size() == 0) {
     ptrl.setVisibility(View.GONE);
     netView.setVisibility(View.GONE);
     emptyView.setVisibility(View.VISIBLE);
     } else {
     ptrl.setVisibility(View.VISIBLE);
     netView.setVisibility(View.GONE);
     emptyView.setVisibility(View.GONE);
     }
     break;
     case REFRESH:
     ptrl.hideHeadView();
     break;
     }
     adapter.notifyDataSetChanged();
     }
     * */
}
