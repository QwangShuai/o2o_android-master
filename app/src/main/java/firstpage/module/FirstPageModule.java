package firstpage.module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.gjzg.config.VarConfig;
import firstpage.listener.ComCityListener;
import firstpage.listener.HotCityListener;
import firstpage.listener.LocIdListener;
import com.gjzg.listener.JsonListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirstPageModule implements IFirstPageModule {

    private OkHttpClient okHttpClient;
    private Call hotCall, comCall, changePositionCall;

    public FirstPageModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void loadHotCity(String hotUrl, final HotCityListener hotCityListener) {
        Request hotReq = new Request.Builder().url(hotUrl).get().build();
        hotCall = okHttpClient.newCall(hotReq);
        hotCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hotCityListener.failure(VarConfig.noNet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    hotCityListener.success(json);
                }
            }
        });
    }

    @Override
    public void loadComCity(String comUrl, final ComCityListener comCityListener) {
        Request comReq = new Request.Builder().url(comUrl).get().build();
        comCall = okHttpClient.newCall(comReq);
        comCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                comCityListener.failure(VarConfig.noNet);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    comCityListener.success(json);
                }
            }
        });
    }

    @Override
    public void getLocId(String[] letter, String locCity, String comJson, LocIdListener locIdListener) {
        try {
            JSONObject beanObj = new JSONObject(comJson);
            if (beanObj.optInt("code") == 200) {
                JSONObject dataObj = beanObj.optJSONObject("data");
                if (dataObj != null) {
                    for (int i = 0; i < letter.length; i++) {
                        JSONArray arr = dataObj.optJSONArray(letter[i]);
                        if (arr != null) {
                            for (int j = 0; j < arr.length(); j++) {
                                JSONObject o = arr.optJSONObject(j);
                                if (o != null) {
                                    if (o.optString("r_name").equals(locCity)) {
                                        locIdListener.success(o.optString("r_id"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changePosition(String url, final JsonListener jsonListener) {
        Request request = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        changePositionCall = okHttpClient.newCall(request);
        changePositionCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                jsonListener.success(e.getMessage());
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
    public void cancelTask() {
        if (hotCall != null) {
            hotCall.cancel();
            hotCall = null;
        }
        if (comCall != null) {
            comCall.cancel();
            comCall = null;
        }
        if (changePositionCall != null) {
            changePositionCall.cancel();
            changePositionCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
