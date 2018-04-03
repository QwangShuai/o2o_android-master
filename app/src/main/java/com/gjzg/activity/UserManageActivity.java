package com.gjzg.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gjzg.config.NetConfig;
import com.gjzg.config.PermissionConfig;
import com.gjzg.fragment.EditInfoFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.gjzg.fragment.UserInfoFragment;
import com.gjzg.bean.UserInfoBean;
import usermanage.presenter.IUserManagePresenter;
import usermanage.presenter.UserManagePresenter;
import usermanage.view.IUserManageActivity;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CImageView;
import com.gjzg.custom.CProgressDialog;

public class UserManageActivity extends AppCompatActivity implements IUserManageActivity, View.OnClickListener {

    private View rootView;
    private RelativeLayout returnRl, previewRl, editRl;
    private TextView previewTv, editTv;
    private CImageView iconIv;
    private CProgressDialog cpd;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private int curPosition = 0, tarPosition = -1;
    private View editPopView;
    private PopupWindow editPop;
    private IUserManagePresenter userManagePresenter = new UserManagePresenter(this);
    public UserInfoBean userInfoBean;

    private final int INFO_SUCCESS = 2;
    private final int INFO_FAILURE = 3;
    private Handler userInfoHandler;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        tarPosition = 0;
                        changeFragment();
                        loadData();
                        userInfoHandler.sendEmptyMessage(5);
                        break;
                    case INFO_SUCCESS:
                        Picasso.with(UserManageActivity.this).load(userInfoBean.getU_img()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
                        break;
                    case INFO_FAILURE:
                        break;
                    case 4:
                        loadData();
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_user_manage, null);
        setContentView(rootView);
        initView();
        initData();
        setListener();
        loadData();
    }

    private void initView() {
        initRootView();
        initDialogView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_user_manage_return);
        previewRl = (RelativeLayout) rootView.findViewById(R.id.rl_user_manage_info);
        previewTv = (TextView) rootView.findViewById(R.id.tv_person_manage_info);
        editRl = (RelativeLayout) rootView.findViewById(R.id.rl_user_manage_edit);
        editTv = (TextView) rootView.findViewById(R.id.tv_user_manage_edit);
        iconIv = (CImageView) rootView.findViewById(R.id.iv_user_manage_icon);
        cpd = Utils.initProgressDialog(UserManageActivity.this, cpd);
    }

    private void initDialogView() {
        editPopView = View.inflate(this, R.layout.pop_dialog_0, null);
        ((TextView) editPopView.findViewById(R.id.tv_pop_dialog_0_content)).setText("是否要修改您的个人信息？");
        ((TextView) editPopView.findViewById(R.id.tv_pop_dialog_0_cancel)).setText("取消");
        ((TextView) editPopView.findViewById(R.id.tv_pop_dialog_0_sure)).setText("确认");
        (editPopView.findViewById(R.id.rl_pop_dialog_0_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPop.dismiss();
            }
        });
        (editPopView.findViewById(R.id.rl_pop_dialog_0_sure)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPop.dismiss();
                changeFragment();
            }
        });
        editPop = new PopupWindow(editPopView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        editPop.setTouchable(true);
        editPop.setFocusable(true);
        editPop.setOutsideTouchable(true);
        editPop.setBackgroundDrawable(new BitmapDrawable());
        editPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
    }

    private void initData() {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        userInfoHandler = userInfoFragment.handler;
        EditInfoFragment editInfoFragment = new EditInfoFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(userInfoFragment);
        fragmentList.add(editInfoFragment);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ll_user_manage_sit, fragmentList.get(curPosition));
        transaction.commit();
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        previewRl.setOnClickListener(this);
        editRl.setOnClickListener(this);
        iconIv.setOnClickListener(this);
    }

    private void loadData() {
        userManagePresenter.info(NetConfig.userInfoUrl + UserUtils.readUserData(UserManageActivity.this).getId());
    }

    private void changeFragment() {
        if (tarPosition != curPosition) {
            switch (tarPosition) {
                case 0:
                    previewTv.setTextColor(Color.parseColor("#ff3e50"));
                    editTv.setTextColor(Color.parseColor("#a0a0a0"));
                    break;
                case 1:
                    previewTv.setTextColor(Color.parseColor("#a0a0a0"));
                    editTv.setTextColor(Color.parseColor("#ff3e50"));
                    break;
                default:
                    break;
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment curFragment = fragmentList.get(curPosition);
            Fragment tarFragment = fragmentList.get(tarPosition);
            transaction.hide(curFragment);
            if (tarFragment.isAdded()) {
                transaction.show(tarFragment);
            } else {
                transaction.add(R.id.ll_user_manage_sit, tarFragment);
            }
            transaction.commit();
            curPosition = tarPosition;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_manage_return:
                finish();
                break;
            case R.id.rl_user_manage_info:
                tarPosition = 0;
                changeFragment();
                break;
            case R.id.rl_user_manage_edit:
                tarPosition = 1;
                if (curPosition != tarPosition) {
                    backgroundAlpha(0.5f);
                    editPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.iv_user_manage_icon:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PermissionConfig.GALLERY);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionConfig.GALLERY && resultCode == RESULT_OK) {
            //从相册返回的数据
            if (data != null) {
                Bitmap bitmap = null;
                Uri uri = null;
                String path = null;
                Bundle bundle = data.getExtras();
                if (data.getData() != null) {
                    uri = data.getData();
                } else {
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        if (bitmap != null) {
                            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                        }
                    }
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (uri != null) {
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = null;
                    if (uri.getScheme().equals("content")) {
                        cursor = cr.query(uri, null, null, null, null);
                    } else {
                        cursor = cr.query(getFileUri(uri), null, null, null, null);
                    }
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columnIndex);
                        cursor.close();
                        try {
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            if (bmp != null) {
                                Bitmap bm = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                                if (bm != null) {
                                    String base64 = Utils.Bitmap2StrByBase64(bm);
                                    if (base64 == null || base64.equals("null") || TextUtils.isEmpty(base64)) {
                                    } else {
                                        String url = Utils.getIconUpdateUrl(UserUtils.readUserData(UserManageActivity.this).getId(), new File(path).getName());
                                        if (url == null || url.equals("null") || TextUtils.isEmpty(url)) {
                                        } else {
                                            RequestBody baseBody = new FormBody.Builder().add("base64", base64).build();
                                            Request request = new Request.Builder().url(url).post(baseBody).build();
                                            OkHttpClient okHttpClient = new OkHttpClient();
                                            okHttpClient.newCall(request).enqueue(new Callback() {
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
                                                                                break;
                                                                            case 1:
                                                                                handler.sendEmptyMessage(4);
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
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //下面是红色字体的方法内容
    public Uri getFileUri(Uri uri) {
        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Images.ImageColumns.DATA)
                        .append("=")
                        .append("'" + path + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur
                        .moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    //do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    @Override
    public void infoSuccess(String json) {
        userInfoBean = DataUtils.getUserInfoBean(json);
        handler.sendEmptyMessage(INFO_SUCCESS);
    }

    @Override
    public void infoFailure(String failure) {

    }
}
