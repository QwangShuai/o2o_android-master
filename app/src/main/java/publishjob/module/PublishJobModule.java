package publishjob.module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import publishjob.listener.GetSkillListener;
import publishjob.listener.TaskTypeListener;


public class PublishJobModule implements IPublishJobModule {

    private OkHttpClient okHttpClient;
    private Call taskTypeCall, getSkillCall;

    public PublishJobModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void getTaskType(String url, final TaskTypeListener taskTypeListener) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        taskTypeCall = okHttpClient.newCall(request);
        taskTypeCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    List<String> taskTypeList = new ArrayList<>();
                    try {
                        JSONObject beanObj = new JSONObject(json);
                        if (beanObj.optInt("code") == 200) {
                            JSONArray dataArr = beanObj.optJSONArray("data");
                            if (dataArr != null) {
                                for (int i = 0; i < dataArr.length(); i++) {
                                    taskTypeList.add(dataArr.optString(i));
                                }
                                taskTypeListener.success(taskTypeList);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void getSkill(String url, final GetSkillListener getSkillListener) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        getSkillCall = okHttpClient.newCall(request);
        getSkillCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    getSkillListener.success(response.body().string());
                }
            }
        });
    }

    @Override
    public void cancelTask() {
        if (taskTypeCall != null) {
            taskTypeCall.cancel();
            taskTypeCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
