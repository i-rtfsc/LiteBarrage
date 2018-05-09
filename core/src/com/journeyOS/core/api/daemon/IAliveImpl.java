package com.journeyOS.core.api.daemon;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.daemon.AliveActivity;
import com.journeyOS.literouter.annotation.ARouterInject;

@ARouterInject(api = IAliveApi.class)
public class IAliveImpl implements IAliveApi {
    private static final String TAG = IAliveImpl.class.getCanonicalName();
    private boolean DEBUG = true;
    private static final String KEEP_ALIVE = "com.journeyOS.core.daemon.keep_alive";

    @Override
    public void onCreate() {

    }

    @Override
    public void keepAlive(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(KEEP_ALIVE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtils.d(TAG, e);
        }
    }

}
