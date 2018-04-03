package com.gjzg.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gjzg.config.VarConfig;

public class CodeTimerService extends Service {

    public static final String IN_RUNNING = "in";
    public static final String END_RUNNING = "end";
    private CountDownTimer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new CountDownTimer(VarConfig.PWD_COUNT_DOWN * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                broadcastUpdate(IN_RUNNING, millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                broadcastUpdate(END_RUNNING);
                stopSelf();
            }
        };
        timer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, String time) {
        final Intent intent = new Intent(action);
        intent.putExtra("time", time);
        sendBroadcast(intent);
    }
}
