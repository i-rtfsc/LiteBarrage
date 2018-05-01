package com.journeyOS.base.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

public class ScreenObserver {
    private static String TAG = ScreenObserver.class.getSimpleName();
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenObserver(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                mScreenStateListener.onScreenChanged(true);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mScreenStateListener.onScreenChanged(false);
            }
        }
    }

    public void requestScreenStateUpdate(ScreenStateListener listener) {
        mScreenStateListener = listener;
        startScreenBroadcastReceiver();

        firstGetScreenState();
    }

    private void firstGetScreenState() {
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Activity.POWER_SERVICE);
        if (isScreenOn()) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenChanged(true);
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenChanged(false);
            }
        }
    }

    public void stopScreenStateUpdate() {
        mContext.unregisterReceiver(mScreenReceiver);
    }

    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenReceiver, filter);
    }


    private boolean isScreenOn() {
        boolean screenState;
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public interface ScreenStateListener {
        void onScreenChanged(boolean isScreenOn);
    }
}