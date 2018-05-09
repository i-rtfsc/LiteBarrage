/*
 * Copyright (c) 2018 anqi.huang@outlook.com.
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

package com.journeyOS.plugins.settings.ui;

import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.viewmodel.ModelProvider;
import com.journeyOS.plugins.R;
import com.journeyOS.plugins.R2;
import com.journeyOS.plugins.settings.ui.adapter.AppHolder;
import com.journeyOS.plugins.settings.ui.adapter.AppInfoData;

import java.util.List;

import butterknife.BindView;

public class PluginsSettingsActivity extends BaseActivity {
    private static final String TAG = PluginsSettingsActivity.class.getSimpleName();

    @BindView(R2.id.apps_recyclerView)
    RecyclerView mAllAppsView;

    private BaseRecyclerAdapter mAllAppsAdapter;

    private AppModel mAppModel;

    public static void navigationActivity(Context from) {
        try {
            Intent intent = new Intent(from, PluginsSettingsActivity.class);
            from.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtils.d(TAG, e);
        }

    }

    public static void navigationFromApplication(Context from) {
        Intent intent = new Intent(from, PluginsSettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        from.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.plugins_settings_activity;
    }

    @Override
    public void initViews() {
        UIUtils.setStatusBarColor(this, this.getResources().getColor(R.color.lightskyblue));
    }

    @Override
    protected void initDataObserver() {
        super.initDataObserver();
        LogUtils.d(TAG, "data observer has been called!");
        mAppModel = ModelProvider.getModel(this, AppModel.class);
        mAppModel.getAllApps();
        mAppModel.getAllAppData().observe(this, new Observer<List<AppInfoData>>() {
            @Override
            public void onChanged(@Nullable List<AppInfoData> appInfoData) {
                onAllApps(appInfoData);
            }
        });
    }

    void onAllApps(final List<AppInfoData> appInfoData) {
        LogUtils.d(TAG, "observer app info data = " + appInfoData);
        LinearLayoutManager appLayoutManager = new LinearLayoutManager(this);
        mAllAppsView.setLayoutManager(appLayoutManager);
        mAllAppsAdapter = new BaseRecyclerAdapter(this);
        mAllAppsAdapter.setData(appInfoData);
        mAllAppsAdapter.registerHolder(AppHolder.class, R.layout.app_item);
        mAllAppsView.setAdapter(mAllAppsAdapter);
    }

}
