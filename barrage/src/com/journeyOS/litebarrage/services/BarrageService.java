/*
 * Copyright (c) 2018 anqi.huang@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyOS.litebarrage.services;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.journeyOS.base.Constant;
import com.journeyOS.base.persistence.SpUtils;
import com.journeyOS.base.receiver.ScreenObserver;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.Messages;
import com.journeyOS.core.api.daemon.IAliveApi;
import com.journeyOS.litebarrage.entity.MessageInfos;
import com.journeyOS.litebarrage.entity.NotificationTransverter;
import com.journeyOS.litebarrage.wm.BarrageManager;
import com.journeyOS.literouter.Router;
import com.journeyOS.literouter.RouterListener;


public class BarrageService extends NotificationListenerService implements ScreenObserver.ScreenStateListener, RouterListener {
    private static final String TAG = BarrageService.class.getSimpleName();
    private Context mContext;
    BarrageManager mBarrageManager;
    private ScreenObserver mScreenObserver;

    public static final long MSG_DELAY_TIME = 1000L * 10;
    private final H mHandler = new H();

    @Override
    public void onCreate() {
        super.onCreate();
        Router.getDefault().register(this);
        prepareJob();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        MessageInfos infos = NotificationTransverter.convertFromNotification(sbn);
        LogUtils.d(TAG, infos.toString());

        Message msg = new Message();
        msg.what = H.MSG_BARRAGE_SHOW;
        msg.obj = infos;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mBarrageManager.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Router.getDefault().unregister(this);
        destroy();
    }

    void prepareJob() {
        mContext = CoreManager.getContext();

        mBarrageManager = new BarrageManager(mContext);

        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(this);
    }

    void destroy() {
        mScreenObserver.stopScreenStateUpdate();

        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onScreenChanged(boolean isScreenOn) {
        if (Constant.DEBUG) LogUtils.d(TAG, "screen on = " + isScreenOn);
        if (!isScreenOn) {
            boolean daemon = SpUtils.getInstant().getBoolean(Constant.DAEMON, true);
            if (daemon) CoreManager.getImpl(IAliveApi.class).keepAlive(mContext);
        }
    }

    @Override
    public void onShowMessage(com.journeyOS.literouter.Message message) {
        Messages msg = (Messages) message;
        switch (msg.what) {
            case Messages.MSG_DEBUG_BARRAGE:
                String infos = (String) msg.obj;
                mBarrageManager.addDanmaku(infos);
                break;
        }
    }

    public final class H extends Handler {
        public static final int MSG_BARRAGE_SHOW = 1 << 0;
        public static final int MSG_BARRAGE_HIDE = 1 << 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_BARRAGE_SHOW:
                    MessageInfos infos = (MessageInfos) msg.obj;
                    mBarrageManager.addDanmaku(infos.title + " : " + infos.text);
                    mHandler.sendEmptyMessageDelayed(H.MSG_BARRAGE_HIDE, MSG_DELAY_TIME);
                    break;
                case MSG_BARRAGE_HIDE:
//                    mBarrageManager.hideBarrage();
                    break;
            }
        }
    }
}
