package com.gjzg.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.gjzg.R;

import java.util.ArrayList;
import java.util.List;

import com.gjzg.adapter.ComplainPicAdapter;
import com.gjzg.bean.Pic;
import com.gjzg.config.IntentConfig;
import com.gjzg.utils.Utils;
import com.gjzg.custom.CProgressDialog;

public class PicActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private RelativeLayout returnRl, sureRl;
    private GridView gv;
    private CProgressDialog cpd;

    private List<Pic> list;
    private ComplainPicAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) {

            } else {
                switch (msg.what) {
                    case 1:
                        cpd.dismiss();
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_pic, null);
        setContentView(rootView);
        initView();
        initData();
        setData();
        setListener();
        loadData();
    }

    private void initView() {
        initRootView();
    }

    private void initRootView() {
        returnRl = (RelativeLayout) rootView.findViewById(R.id.rl_pic_return);
        sureRl = (RelativeLayout) rootView.findViewById(R.id.rl_pic_sure);
        gv = (GridView) rootView.findViewById(R.id.gv_pic);
        cpd = Utils.initProgressDialog(PicActivity.this, cpd);
    }

    private void initData() {
        list = new ArrayList<>();
        adapter = new ComplainPicAdapter(this, list);
    }

    private void setData() {
        gv.setAdapter(adapter);
    }

    private void setListener() {
        returnRl.setOnClickListener(this);
        sureRl.setOnClickListener(this);
        gv.setOnItemClickListener(this);
    }

    private void loadData() {
        cpd.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                if (mImageUri == null) {
                } else {
                    ContentResolver mContentResolver = PicActivity.this.getContentResolver();
                    if (mContentResolver == null) {
                    } else {
                        Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                        while (mCursor.moveToNext()) {
                            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            Pic pic = new Pic();
                            pic.setChoose(false);
                            pic.setPath(path);
                            list.add(pic);
                        }
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pic_return:
                finish();
                break;
            case R.id.rl_pic_sure:
                sure();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean b = list.get(position).isChoose();
        list.get(position).setChoose(!b);
        adapter.notifyDataSetChanged();
    }

    private int getPicAmount() {
        int picAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChoose()) {
                picAmount++;
            }
        }
        return picAmount;
    }

    private void sure() {
        int picAmount = getPicAmount();
        if (picAmount == 0) {
            Utils.toast(PicActivity.this, "你还没有选择图片");
        } else {
            List<String> strList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isChoose()) {
                    strList.add(list.get(i).getPath());
                }
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(IntentConfig.PIC, (ArrayList<String>) strList);
            setResult(IntentConfig.PIC_RESULT, intent);
            finish();
        }
    }
}
