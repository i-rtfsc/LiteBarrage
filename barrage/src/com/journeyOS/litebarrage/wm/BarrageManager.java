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

package com.journeyOS.litebarrage.wm;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.journeyOS.base.Constant;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.litebarrage.R;
import com.journeyOS.litebarrage.services.BarrageService;

import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.AbsDisplayer;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

import static android.view.WindowManager.LayoutParams;

//https://blog.csdn.net/benhuo931115/article/details/51056646
public class BarrageManager {
    private static final String TAG = BarrageManager.class.getSimpleName();

    private Context mContext;
    private WindowManager mWm;
    private Handler mHandler;

    private DanmakuView mDanmakuView;
    private BaseDanmakuParser mParser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    public BarrageManager(Context context, Handler handler) {
        mContext = context;
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mHandler = handler;
    }

    DanmakuView initDanmakuView(DanmakuContext danmakuContext) {
        final DanmakuView danmakuView = (DanmakuView) View.inflate(mContext, R.layout.barrage_layout, null);
        if (danmakuView != null) {
            danmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {
                    if (Constant.DEBUG) LogUtils.d(TAG, "on barrage drawing finished");
                    //hideBarrage();
                    mHandler.sendEmptyMessageDelayed(BarrageService.H.MSG_BARRAGE_HIDE, BarrageService.MSG_DELAY_TIME);
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                    if (Constant.DEBUG) LogUtils.d(TAG, "on barrage shown");
                    if (mHandler.hasMessages(BarrageService.H.MSG_BARRAGE_HIDE)) {
                        mHandler.removeMessages(BarrageService.H.MSG_BARRAGE_HIDE);
                    }
                }

                @Override
                public void prepared() {
                    danmakuView.start();
                }
            });
            danmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    if (Constant.DEBUG) LogUtils.d(TAG, "on barrage click.");
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    if (Constant.DEBUG) LogUtils.d(TAG, "on barrage long click.");
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    if (Constant.DEBUG) LogUtils.d(TAG, "on barrage view click.");
                    return false;
                }
            });
            danmakuView.prepare(mParser, danmakuContext);
//            danmakuView.showFPS(true);
            danmakuView.enableDanmakuDrawingCache(true);
            danmakuView.canResolveTextAlignment();
        }
        return danmakuView;
    }

    DanmakuContext initDanmakuContext(int length) {
        //不好要改掉。
        float speed = (length < 20 ? 1.9f : length/8);
        LogUtils.d(TAG, "barrage speed = "+speed);
        DanmakuContext danmakuContext = DanmakuContext.create();

        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5);

        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        AbsDisplayer absDisplayer = danmakuContext.getDisplayer();
        absDisplayer.setHardwareAccelerated(true);

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 2)
                .setDuplicateMergingEnabled(true)
                .setScrollSpeedFactor(speed)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair)
                .setDanmakuMargin(40);
        return danmakuContext;
    }

    @MainThread
    public void addDanmaku(String msg) {
        DanmakuContext mDanmakuContext = initDanmakuContext(msg.length());
        if (BaseUtils.isNull(mDanmakuView)) {
            mDanmakuView = initDanmakuView(mDanmakuContext);
        }
        if (!mDanmakuView.isAttachedToWindow()) {
            mWm.addView(mDanmakuView, getLayoutParams());
        }

        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL, mDanmakuContext);

        mDanmakuView.pause();
        mDanmakuView.resume();

        danmaku.text = msg == null ? "null" : msg;
        danmaku.padding = 5;
        danmaku.priority = 0;  // 0 可能会被各种过滤器过滤并隐藏显示, 1 表示一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = true;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
//        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textSize = 50f;
        danmaku.textColor = mContext.getResources().getColor(R.color.hotpink);
        mDanmakuView.addDanmaku(danmaku);
    }

    @MainThread
    public void hideBarrage() {
        if (!BaseUtils.isNull(mDanmakuView) && mDanmakuView.isAttachedToWindow()) {
            mWm.removeView(mDanmakuView);
        }
    }

    LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.format = PixelFormat.TRANSPARENT;
        params.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.gravity = Gravity.TOP;
        params.y = UIUtils.getStatusBarHeight(mContext);
        params.width = mContext.getResources().getDisplayMetrics().widthPixels;
        params.height = UIUtils.getStatusBarHeight(mContext);
        return params;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (mDanmakuView != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mDanmakuView.getConfig().setDanmakuMargin(200);
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mDanmakuView.getConfig().setDanmakuMargin(400);
            }
        }
    }
}
