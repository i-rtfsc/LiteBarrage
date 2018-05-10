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

package com.journeyOS.plugins.settings.app.repository;

import android.os.Handler;

import com.journeyOS.base.Constant;
import com.journeyOS.base.persistence.SpUtils;
import com.journeyOS.base.utils.AppUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.appprovider.App;
import com.journeyOS.core.repository.DBHelper;
import com.journeyOS.literouter.annotation.ARouterInject;
import com.journeyOS.litetask.TaskScheduler;
import com.journeyOS.plugins.settings.app.repository.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

@ARouterInject(api = IAppRepositoryApi.class)
public class AppRepositoryImpl implements IAppRepositoryApi {
    private static final String TAG = AppRepositoryImpl.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private Handler mAppHandler;

    @Override
    public void onCreate() {
        mAppDatabase = DBHelper.provider(AppDatabase.class, "app.db");
        mAppHandler = TaskScheduler.provideHandler(TAG);
    }


    @Override
    public List<App> getAllApps() {
        return mAppDatabase.appDao().getAll();
    }

    @Override
    public void insertApp(App app) {
        mAppDatabase.appDao().insert(app);
    }

    @Override
    public void insertApp(List<App> apps) {
        mAppDatabase.appDao().insert(apps);
    }

    @Override
    public void updateApp(App app) {
        mAppDatabase.appDao().update(app);
    }

    @Override
    public void updateApp(List<App> apps) {
        mAppDatabase.appDao().update(apps);
    }

    @Override
    public void deleteApp(App app) {
        mAppDatabase.appDao().delete(app);
    }

    @Override
    public void deleteApp(List<App> apps) {
        mAppDatabase.appDao().delete(apps);
    }

    @Override
    public void initApps() {
        boolean appInited = SpUtils.getInstant().getBoolean(Constant.APP_INITED, false);
        LogUtils.d(TAG, "device has been init database = " + appInited);
        if (appInited) {
            return;
        }

        mAppHandler.post(new Runnable() {
            @Override
            public void run() {
                List<String> apps = AppUtils.getLauncherApp(CoreManager.getContext());
                LogUtils.d(TAG, "device install apps size = " + apps.size());
                List<App> allApps = new ArrayList<>();
                for (String packageName : apps) {
                    App app = new App();
                    app.appName = AppUtils.getAppName(CoreManager.getContext(), packageName);
                    app.packageName = packageName;
                    app.toggle = true;
                    allApps.add(app);
                }
                mAppDatabase.appDao().insert(allApps);
                SpUtils.getInstant().put(Constant.APP_INITED, true);
            }
        });
    }

    @Override
    public Handler getAppWorkHandler() {
        return mAppHandler;
    }

    @Override
    public App getApp(String packageName) {
        return mAppDatabase.appDao().getApp(packageName);
    }

}
