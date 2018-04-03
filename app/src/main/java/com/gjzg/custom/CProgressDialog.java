package com.gjzg.custom;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.gjzg.R;


public class CProgressDialog extends Dialog {

    private View dialogView;
    private Context mContext;
    private ImageView imageIv;
    private ObjectAnimator animator;

    public CProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initDialog();
    }

    private void initDialog() {
        dialogView = View.inflate(mContext, R.layout.dialog_cprogress, null);
        imageIv = (ImageView) dialogView.findViewById(R.id.iv_dialog_cprogress);
        animator = ObjectAnimator.ofFloat(imageIv, "rotation", 0f, 359f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(800);
        animator.setRepeatCount(-1);
        this.setContentView(dialogView);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        super.show();
        animator.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animator.cancel();
    }
}
