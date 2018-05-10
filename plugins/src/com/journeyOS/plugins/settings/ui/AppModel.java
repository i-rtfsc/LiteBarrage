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

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.journeyOS.base.utils.AppUtils;
import com.journeyOS.base.utils.BaseUtils;
import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.appprovider.App;
import com.journeyOS.core.viewmodel.BaseViewModel;
import com.journeyOS.plugins.settings.app.repository.IAppRepositoryApi;
import com.journeyOS.plugins.settings.ui.adapter.AppInfoData;

import java.util.ArrayList;
import java.util.List;

public class AppModel extends BaseViewModel {
    private static final String TAG = AppModel.class.getSimpleName();
    private Context mContext;
    private MutableLiveData<List<AppInfoData>> mAppInfoData = new MutableLiveData<>();

    @Override
    protected void onCreate() {
        mContext = CoreManager.getContext();
    }

    void getAllApps() {
        CoreManager.getImpl(IAppRepositoryApi.class).getAppWorkHandler().post(new Runnable() {
            @Override
            public void run() {
                List<App> allApps = CoreManager.getImpl(IAppRepositoryApi.class).getAllApps();
                LogUtils.d(TAG, "search apps result = " + allApps.size());
                if (!BaseUtils.isNull(allApps)) {
                    List<AppInfoData> appInfoDatas = new ArrayList<>();
                    for (App app : allApps) {
                        String packageName = app.packageName;
                        Boolean toggle = app.toggle;
                        Drawable drawable = AppUtils.getAppIcon(mContext, packageName);
                        String label = app.appName;
                        AppInfoData appInfoData = new AppInfoData(drawable, label, packageName, toggle);
                        appInfoDatas.add(appInfoData);
                    }
                    mAppInfoData.postValue(appInfoDatas);
                }
            }
        });
    }

    MutableLiveData<List<AppInfoData>> getAllAppData() {
        return mAppInfoData;
    }
}
