package complain.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.gjzg.adapter.ComplainImageAdapter;
import com.gjzg.adapter.ComplainIssueAdapter;
import com.gjzg.adapter.PhotoAdapter;
import com.gjzg.bean.ComplainIssueBean;
import com.gjzg.bean.PhotoBean;
import com.gjzg.bean.ToComplainBean;
import complain.presenter.ComplainPresenter;
import complain.presenter.IComplainPresenter;
import com.gjzg.config.AppConfig;
import com.gjzg.config.IntentConfig;
import com.gjzg.config.NetConfig;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.utils.DataUtils;
import com.gjzg.utils.UserUtils;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CImageView;
import com.gjzg.custom.CProgressDialog;

public class ComplainActivity extends AppCompatActivity implements IComplainActivity, View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout returnRl;
    private RelativeLayout addImageRl;
    private TextView submitTv;
    private View popView;
    private PopupWindow pop;
    private TextView cameraTv, mapTv, cancelTv;
    private CProgressDialog cpd;
    private GridView gv;
    private List<Bitmap> list;
    private ComplainImageAdapter adapter;
    private String picPath;
    private List<String> upLoadImageList;
    private IComplainPresenter complainPresenter;
    private TextView cplIsTv;
    private View cplIsPopView;
    private PopupWindow cplIsPop;
    private ListView cplIsLv;
    private List<ComplainIssueBean> complainIssueBeanList = new ArrayList<>();
    private ComplainIssueAdapter complainIssueAdapter;

    private ToComplainBean toComplainBean;
    private UserInfoBean userInfoBean;

    private final int USER_INFO_SUCCESS = 1;
    private final int USER_INFO_FAILURE = 2;
    private final int USER_ISSUE_SUCCESS = 5;
    private final int USER_ISSUE_FAILURE = 6;
    private final int SUBMIT_SUCCESS = 7;
    private final int SUBMIT_FAILURE = 8;
    private CImageView iconIv;
    private ImageView sexIv;
    private TextView nameTv, skillNameTv, evaluateTv;
    private EditText contentEt;

    private static final int REQUEST_WRITE = 0;//写
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final String PHOTO_FILE_NAME = "temp_photo";
    private File tempFile;
    private String path;
    private Uri uri;
    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";
    private List<PhotoBean> photoBeanList = new ArrayList<>();
    private PhotoAdapter photoAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        photoAdapter.notifyDataSetChanged();
                        Utils.setGridViewHeight(gv, 3);
                        break;
                    case USER_INFO_SUCCESS:
                        complainPresenter.userIssue(NetConfig.complainTypeUrl + "?ct_type=" + toComplainBean.getCtType());
                        break;
                    case USER_INFO_FAILURE:
                        break;
                    case USER_ISSUE_SUCCESS:
                        notifyData();
                        break;
                    case USER_ISSUE_FAILURE:
                        break;
                    case SUBMIT_SUCCESS:
                        cpd.dismiss();
                        Utils.toast(ComplainActivity.this, "投诉成功");
                        finish();
                        break;
                    case SUBMIT_FAILURE:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_complain, null);
        setContentView(rootView);
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
            }
        }
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (complainPresenter != null) {
            complainPresenter.destory();
            complainPresenter = null;
        }
        if (handler != null) {
            handler.removeMessages(USER_INFO_SUCCESS);
            handler.removeMessages(USER_INFO_FAILURE);
            handler.removeMessages(USER_ISSUE_SUCCESS);
            handler.removeMessages(USER_ISSUE_FAILURE);
            handler.removeMessages(SUBMIT_SUCCESS);
            handler.removeMessages(SUBMIT_FAILURE);
            handler = null;
        }
    }

    private void initView() {
        initRootView();
        initCplIsPopView();
        initPopView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_complain_return);
        addImageRl = (RelativeLayout) rootView.findViewById(R.id.rl_complain_add_image);
        submitTv = (TextView) rootView.findViewById(R.id.tv_complain_sumit);
        gv = (GridView) rootView.findViewById(R.id.gv_complain_image);
        cpd = Utils.initProgressDialog(this, cpd);

        iconIv = (CImageView) rootView.findViewById(R.id.iv_complain_icon);
        sexIv = (ImageView) rootView.findViewById(R.id.iv_complain_sex);
        nameTv = (TextView) rootView.findViewById(R.id.tv_complain_name);
        skillNameTv = (TextView) rootView.findViewById(R.id.tv_complain_skill_name);
        evaluateTv = (TextView) rootView.findViewById(R.id.tv_complain_evaluate);
        contentEt = (EditText) rootView.findViewById(R.id.et_complain_content);
    }

    private void initCplIsPopView() {
        cplIsTv = (TextView) rootView.findViewById(R.id.tv_complain_issue);
        cplIsPopView = LayoutInflater.from(ComplainActivity.this).inflate(R.layout.pop_lv, null);
        cplIsLv = (ListView) cplIsPopView.findViewById(R.id.lv_pop_lv);
        cplIsPop = new PopupWindow(cplIsPopView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        cplIsPop.setFocusable(true);
        cplIsPop.setTouchable(true);
        cplIsPop.setOutsideTouchable(true);
        complainIssueAdapter = new ComplainIssueAdapter(ComplainActivity.this, complainIssueBeanList);
        cplIsLv.setAdapter(complainIssueAdapter);
    }

    private void initPopView() {
        popView = LayoutInflater.from(this).inflate(R.layout.pop_add_image, null);
        pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        cameraTv = (TextView) popView.findViewById(R.id.tv_pop_add_image_camera);
        mapTv = (TextView) popView.findViewById(R.id.tv_pop_add_image_map);
        cancelTv = (TextView) popView.findViewById(R.id.tv_pop_add_image_cancel);
        cameraTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                if (Utils.hasSdcard()) {
                    requestTakePhoto();
                } else {
                    Utils.toast(ComplainActivity.this, "Sd卡不可用");
                }
            }
        });
        mapTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                requestGallery();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

    private void initData() {
        toComplainBean = (ToComplainBean) getIntent().getSerializableExtra(IntentConfig.toComplain);
        toComplainBean.setAuthorId(UserUtils.readUserData(ComplainActivity.this).getId());
        list = new ArrayList<>();
        adapter = new ComplainImageAdapter(this, list);
        upLoadImageList = new ArrayList<>();
        complainPresenter = new ComplainPresenter(this);
        photoAdapter = new PhotoAdapter(ComplainActivity.this, photoBeanList);
    }

    private void setData() {
        gv.setAdapter(photoAdapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        cplIsTv.setOnClickListener(this);
        addImageRl.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        cplIsLv.setOnItemClickListener(this);
        gv.setOnItemClickListener(this);
        contentEt.addTextChangedListener(contentTw);
    }

    private void loadData() {
        cpd.show();
        complainPresenter.userInfo(NetConfig.userInfoUrl + toComplainBean.getAgainstId());
    }

    @Override
    public void userInfoSuccess(String json) {
        userInfoBean = DataUtils.getUserInfoBean(json);
        handler.sendEmptyMessage(USER_INFO_SUCCESS);
    }

    @Override
    public void userInfoFailure(String failure) {

    }

    @Override
    public void userIssueSuccess(String json) {
        complainIssueBeanList.addAll(DataUtils.getComplainIssueBeanList(json));
        handler.sendEmptyMessage(USER_ISSUE_SUCCESS);
    }

    @Override
    public void userIssueFailure(String failure) {

    }

    @Override
    public void submitSuccess(String json) {
        handler.sendEmptyMessage(SUBMIT_SUCCESS);
    }

    @Override
    public void submitFailure(String failure) {

    }

    private void notifyData() {
        cpd.dismiss();
        Picasso.with(ComplainActivity.this).load(userInfoBean.getU_img()).placeholder(R.mipmap.person_face_default).error(R.mipmap.person_face_default).into(iconIv);
        String sex = userInfoBean.getU_sex();
        if (sex.equals("0")) {
            sexIv.setImageResource(R.mipmap.female);
        } else if (sex.equals("1")) {
            sexIv.setImageResource(R.mipmap.male);
        }
        String type = toComplainBean.getCtType();
        if (type == null || type.equals("null") || TextUtils.isEmpty(type)) {
        } else {
            if (type.equals("1")) {
                skillNameTv.setText("雇主");
            } else if (type.equals("2")) {
                skillNameTv.setText(toComplainBean.getSkill());
            }
        }
        nameTv.setText(userInfoBean.getU_true_name());
        evaluateTv.setText("好评" + userInfoBean.getU_high_opinions() + "次");
        complainIssueAdapter.notifyDataSetChanged();
    }

    private void submitData() {
        if (TextUtils.isEmpty(toComplainBean.getCtId())) {
            Utils.toast(ComplainActivity.this, "请选择投诉问题");
        } else if (TextUtils.isEmpty(toComplainBean.getContent())) {
            Utils.toast(ComplainActivity.this, "请详细描述您要投诉的问题");
        } else {
            cpd.show();
            complainPresenter.submit(NetConfig.complainSubmitUrl, toComplainBean);
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void requestTakePhoto() {
        if (hasSdcard()) {
            if (Build.VERSION.SDK_INT > 22) {
                int p = ContextCompat.checkSelfPermission(ComplainActivity.this, Manifest.permission.CAMERA);
                if (p != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ComplainActivity.this, new String[]{Manifest.permission.CAMERA}, PHOTO_REQUEST_CAREMA);
                } else {
                    takePhoto();
                }
            } else {
                takePhoto();
            }
        } else {
            Utils.toast(ComplainActivity.this, "没有Sd卡！");
        }
    }

    private void takePhoto() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + "temp", PHOTO_FILE_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, AppConfig.APPLICATION_ID + ".fileProvider", tempFile);
        } else {
            uri = Uri.fromFile(tempFile);
        }
        if (uri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);
            intent.putExtra("crop", true);
            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        }
    }

    private void requestGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PHOTO_REQUEST_GALLERY);
    }

    private boolean hasSdcard() {
        //判断ＳＤ卡手否是安装好的　　　media_mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_complain_return:
                finish();
                break;
            case R.id.tv_complain_issue:
                cplIsPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                cplIsPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0f);
                    }
                });
                break;
            case R.id.rl_complain_add_image:
                if (photoBeanList.size() < 5) {
                    if (pop.isShowing()) {
                        pop.dismiss();
                    } else {
                        pop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                        backgroundAlpha(0.5f);
                        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                backgroundAlpha(1.0f);
                            }
                        });
                    }
                } else {
                    Utils.toast(ComplainActivity.this, "最多上传五张!");
                }
                break;
            case R.id.tv_complain_sumit:
                submitData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
            case PHOTO_REQUEST_CAREMA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_CAREMA:
                switch (resultCode) {
                    case RESULT_OK:
                        FileOutputStream fileOutputStream = null;
                        String saveDir = Environment.getExternalStorageDirectory().toString() + "/meitian_photos";
                        File dir = new File(saveDir);
                        if (dir.exists()) {
                        } else {
                            dir.mkdir();
                        }
                        SimpleDateFormat t = new SimpleDateFormat("yyyyMmddssSSS");
                        String filename = "MT" + (t.format(new Date())) + ".jpg";
                        File file = new File(saveDir, filename);
                        try {
                            fileOutputStream = new FileOutputStream(file);
                            Bitmap bitmap = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                bitmap = MediaStore.Images.Media.getBitmap(ComplainActivity.this.getContentResolver(), FileProvider.getUriForFile(ComplainActivity.this, AppConfig.APPLICATION_ID + ".fileProvider", tempFile));
                            } else {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(tempFile));
                            }
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            path = file.getPath();
                            String path_ys = saveBitmap(ComplainActivity.this, getSmallBitmap(path));
                            PhotoBean photoBean = new PhotoBean();
                            photoBean.setCheck(false);
                            photoBean.setPath(path_ys);
                            photoBeanList.add(photoBean);
                            handler.sendEmptyMessage(0);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case PHOTO_REQUEST_GALLERY:
                switch (resultCode) {
                    case RESULT_OK:
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
                                    PhotoBean photoBean = new PhotoBean();
                                    photoBean.setCheck(false);
                                    photoBean.setPath(path);
                                    photoBeanList.add(photoBean);
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_pop_lv:
                cplIsTv.setText(complainIssueBeanList.get(position).getName());
                toComplainBean.setCtId(complainIssueBeanList.get(position).getId());
                cplIsPop.dismiss();
                break;
            case R.id.gv_complain_image:
                photoBeanList.remove(position);
                photoAdapter.notifyDataSetChanged();
                Utils.setGridViewHeight(gv, 3);
                break;
        }
    }

    TextWatcher contentTw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            toComplainBean.setContent(s.toString());
        }
    };

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }


    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public Bitmap convertToBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), width, height, true);
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
}
