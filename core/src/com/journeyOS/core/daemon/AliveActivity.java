package com.journeyOS.core.daemon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.journeyOS.base.Constant;
import com.journeyOS.base.receiver.ScreenObserver;
import com.journeyOS.base.utils.LogUtils;

public class AliveActivity extends Activity implements ScreenObserver.ScreenStateListener {
    private static final String TAG = AliveActivity.class.getSimpleName();
    private ScreenObserver mScreenObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.DEBUG) LogUtils.d(TAG, "onCreate() called");
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        //checkScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.DEBUG) LogUtils.d(TAG, "onResume() called");
        prepareJob();
        checkScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constant.DEBUG) LogUtils.d(TAG, "onDestroy() called");
        destroy();
    }

    void prepareJob() {
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(this);
    }

    void destroy() {
        mScreenObserver.stopScreenStateUpdate();
    }

    void checkScreen() {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            finish();
        }
    }

    @Override
    public void onScreenChanged(boolean isScreenOn) {
        if (Constant.DEBUG) LogUtils.d(TAG, "screen on = " + isScreenOn);
        if (isScreenOn) {
            this.finish();
        }
    }
}
