package com.gjzg.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.gjzg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageTipActivity extends AppCompatActivity {

    @BindView(R.id.rl_message_tip_return)
    RelativeLayout rlMessageTipReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_tip);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_message_tip_return)
    public void onClick() {
        finish();
    }
}
