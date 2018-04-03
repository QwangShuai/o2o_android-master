package usermanage.module;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import com.gjzg.listener.JsonListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import usermanage.listener.UpLoadingIconListener;

import com.gjzg.utils.Utils;

public class UserManageModule implements IUserManageModule {

    private OkHttpClient okHttpClient;
    private Call infoCall, upCall;

    public UserManageModule() {
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void info(String url, final JsonListener jsonListener) {
        Request infoRequest = new Request.Builder().url(url).get().build();
        infoCall = okHttpClient.newCall(infoRequest);
        infoCall.enqueue(new Callback() {
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
    public void up(Context context, String id, Uri uri, final UpLoadingIconListener upLoadingIconListener) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        String picPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
        cursor.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不申请内存，重新计算比例
        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        options.inJustDecodeBounds = false;//申请内存
        //计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / 200;
        int beHeight = h / 200;
        int be = 4;
        if (beWidth < beHeight && beHeight >= 1) {
            be = beHeight;
        }
        if (beHeight < beWidth && beWidth >= 1) {
            be = beWidth;
        }
        if (be <= 0) {
            be = 1;
        } else if (be > 3) {
            be = 3;
        }
        options.inSampleSize = be;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;
        options.inInputShareable = true;
        try {
            //重新读入图片，读取缩放后的bitmap
            bitmap = BitmapFactory.decodeFile(picPath, options);
            //利用ThumbnailUtils来创建缩略图
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        }
        String base64 = Utils.Bitmap2StrByBase64(bitmap);
        if (base64 == null || base64.equals("null") || TextUtils.isEmpty(base64)) {
        } else {
            RequestBody baseBody = new FormBody.Builder().add("base64", base64).build();
            File file = new File(picPath);
            String url = Utils.getIconUpdateUrl(id, file.getName());
            Request request = new Request.Builder().url(url).post(baseBody).build();
            upCall = okHttpClient.newCall(request);
            final Bitmap finalBitmap = bitmap;
            upCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        if (!TextUtils.isEmpty(json)) {
                            try {
                                JSONObject objBean = new JSONObject(json);
                                int code = objBean.optInt("code");
                                JSONObject objData = objBean.optJSONObject("data");
                                if (objData != null) {
                                    String msg = objData.optString("msg");
                                    if (!TextUtils.isEmpty(msg)) {
                                        switch (code) {
                                            case 0:
                                                upLoadingIconListener.upLoadingIconFailure(msg);
                                                break;
                                            case 1:
                                                upLoadingIconListener.upLoadingIconSuccess(msg, finalBitmap);
                                                break;
                                            default:
                                                break;
                                        }
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
    }

    @Override
    public void cancelTask() {
        if (upCall != null) {
            upCall.cancel();
            upCall = null;
        }
        if (infoCall != null) {
            infoCall.cancel();
            infoCall = null;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
    }
}
