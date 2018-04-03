package com.gjzg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gjzg.R;

import com.gjzg.utils.UserUtils;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (UserUtils.isFirstUse(LaunchActivity.this)) {
            startActivity(new Intent(LaunchActivity.this, GuideActivity.class));
        } else {
            startActivity(new Intent(LaunchActivity.this, WelcomeActivity.class));
        }
        finish();
    }
}
