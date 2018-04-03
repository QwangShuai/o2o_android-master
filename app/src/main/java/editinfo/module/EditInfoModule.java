package editinfo.module;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.config.NetConfig;
import com.gjzg.config.VarConfig;
import editinfo.listener.AddSkillListener;
import editinfo.listener.SubmitListener;
import com.gjzg.listener.JsonListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.utils.DataUtils;

public class EditInfoModule implements IEditInfoModule {

    private OkHttpClient okHttpClient;
    private Call call, submitCall, skillCall;

    public EditInfoModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void skill(String url, final JsonListener jsonListener) {
        Request skillRequest = new Request.Builder().url(url).get().build();
        skillCall = okHttpClient.newCall(skillRequest);
        skillCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsonListener.failure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    jsonListener.success(response.body().string());
                }
            }
        });
    }

    @Override
    public void load(String url, final AddSkillListener addSkillListener) {
        Request request = new Request.Builder().url(url).get().build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                addSkillListener.failure(VarConfig.noNet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    addSkillListener.success(DataUtils.getSkillBeanList(json));
                }
            }
        });
    }

    @Override
    public void submit(UserInfoBean userInfoBean, final SubmitListener submitListener) {
        RequestBody requestBody ;
//        if(!TextUtils.isEmpty(userInfoBean.getU_idcard())){
            requestBody = new FormBody.Builder()
                    .add("u_id", userInfoBean.getU_id())
                    .add("u_phone",userInfoBean.getU_mobile())
                    .add("u_true_name", userInfoBean.getU_true_name())
                    .add("u_sex", userInfoBean.getU_sex())
                    .add("u_idcard", userInfoBean.getU_idcard())
                    .add("uei_province", userInfoBean.getUei_province())
                    .add("uei_city", userInfoBean.getUei_city())
                    .add("uei_area", userInfoBean.getUei_area())
                    .add("uei_address", userInfoBean.getUei_address())
                    .add("uei_info", userInfoBean.getU_info())
                    .add("u_skills", userInfoBean.getU_skills())
                    .build();
//        } else {
//            requestBody = new FormBody.Builder()
//                    .add("u_id", userInfoBean.getU_id())
//                    .add("u_online",userInfoBean.getU_online())
//                    .build();
//        }


        Request request = new Request.Builder().url(NetConfig.userInfoEditUrl).post(requestBody).build();
        submitCall = okHttpClient.newCall(request);
        submitCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                submitListener.failure(VarConfig.noNet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject beanObj = new JSONObject(json);
                            JSONObject dataObj = beanObj.optJSONObject("data");
                            if (dataObj != null) {
                                int code = beanObj.optInt("code");
                                String msg = dataObj.optString("msg");
                                switch (code) {
                                    case 0:
                                        submitListener.failure(msg);
                                        break;
                                    case 1:
                                        submitListener.success(msg);
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void cancelTask() {
        if (call != null) {
            call.cancel();
            call = null;
        }
        if (skillCall != null) {
            skillCall.cancel();
            skillCall = null;
        }
        if (submitCall != null) {
            submitCall.cancel();
            submitCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
