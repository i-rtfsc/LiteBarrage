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

package com.journeyOS.litebarrage.ui;

import android.content.Context;

import com.journeyOS.base.Constant;
import com.journeyOS.base.persistence.SpUtils;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.widget.SettingSwitch;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.permission.IPermissionApi;
import com.journeyOS.litebarrage.R;
import com.journeyOS.litebarrage.services.BarrageService;
import com.journeyOS.litebarrage.utils.DebugFeature;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private Context mContext;

    @BindView(R.id.daemon)
    SettingSwitch mDaemon;

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        mContext = CoreManager.getContext();
    }

    @Override
    public int getContentViewId() {
        return R.layout.settings_fragment;
    }

    @Override
    public void initViews() {
        initAutoStart();
    }

    void initAutoStart() {
        boolean daemon = SpUtils.getInstant().getBoolean(Constant.DAEMON, true);
        mDaemon.setCheck(daemon);
    }

    @OnClick({R.id.daemon})
    public void listenerAutoStart() {
        boolean daemon = SpUtils.getInstant().getBoolean(Constant.DAEMON, true);
        mDaemon.setCheck(!daemon);
        SpUtils.getInstant().put(Constant.DAEMON, !daemon);
    }

    @OnClick({R.id.overflow})
    public void overflowPermission() {
        boolean hasPermission = CoreManager.getImpl(IPermissionApi.class).canDrawOverlays(mContext);
        if (hasPermission)
            BaseUtils.Toast(mContext, mContext.getString(R.string.has_permission) + mContext.getString(R.string.overflow));
    }

    @OnClick({R.id.notification_permission})
    public void notificationPermission() {
        boolean hasPermission = CoreManager.getImpl(IPermissionApi.class).listenerNotification(mContext, BarrageService.class.getName());
        if (hasPermission)
            BaseUtils.Toast(mContext, mContext.getString(R.string.has_permission) + mContext.getString(R.string.notification_permission));
    }

    @OnClick({R.id.barrage_debug})
    public void barrageDebug() {
        DebugFeature.sendBarrage();
    }
}
