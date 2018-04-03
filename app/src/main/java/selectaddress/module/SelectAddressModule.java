package selectaddress.module;


import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.NetConfig;
import com.gjzg.config.StateConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.gjzg.bean.SelectAddressBean;
import selectaddress.listener.OnLoadSelAddListener;

public class SelectAddressModule implements ISelectAddressModule {

    private OkHttpClient okHttpClient;
    private Call call;

    public SelectAddressModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void load(String id, final OnLoadSelAddListener onLoadSelAddListener) {
        String url = NetConfig.selectAddressBaseUrl + id;
        Request request = new Request.Builder().url(url).get().build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoadSelAddListener.failure(StateConfig.loadNonet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject objBean = new JSONObject(result);
                            if (objBean.optInt("code") == 200) {
                                JSONArray arrData = objBean.optJSONArray("data");
                                if (arrData != null) {
                                    List<SelectAddressBean> selectAddressBeanList = new ArrayList<>();
                                    for (int i = 0; i < arrData.length(); i++) {
                                        JSONObject o = arrData.optJSONObject(i);
                                        if (o != null) {
                                            SelectAddressBean selectAddressBean = new SelectAddressBean();
                                            selectAddressBean.setId(o.optString("r_id"));
                                            selectAddressBean.setName(o.optString("r_name"));
                                            selectAddressBeanList.add(selectAddressBean);
                                        }
                                    }
                                    onLoadSelAddListener.success(selectAddressBeanList);
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
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
