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

package com.journeyOS.plugins.settings.app.provider;

import android.content.Context;
import android.os.Handler;

import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.appprovider.App;
import com.journeyOS.core.api.appprovider.IAppProvider;
import com.journeyOS.literouter.annotation.ARouterInject;
import com.journeyOS.plugins.settings.app.repository.IAppRepositoryApi;
import com.journeyOS.plugins.settings.ui.PluginsSettingsActivity;

import java.util.List;


@ARouterInject(api = IAppProvider.class)
public class AppProviderImpl implements IAppProvider {


    @Override
    public void initApps() {
        CoreManager.getImpl(IAppRepositoryApi.class).initApps();
    }

    @Override
    public Handler getAppWorkHandler() {
        return CoreManager.getImpl(IAppRepositoryApi.class).getAppWorkHandler();
    }

    @Override
    public App getApp(String packageName) {
        return CoreManager.getImpl(IAppRepositoryApi.class).getApp(packageName);
    }

    @Override
    public List<App> getAllApps() {
        return CoreManager.getImpl(IAppRepositoryApi.class).getAllApps();
    }

    @Override
    public void insertApp(App app) {
        CoreManager.getImpl(IAppRepositoryApi.class).insertApp(app);
    }

    @Override
    public void insertApp(List<App> apps) {
        CoreManager.getImpl(IAppRepositoryApi.class).insertApp(apps);
    }

    @Override
    public void updateApp(App app) {
        CoreManager.getImpl(IAppRepositoryApi.class).updateApp(app);
    }

    @Override
    public void updateApp(List<App> apps) {
        CoreManager.getImpl(IAppRepositoryApi.class).updateApp(apps);
    }

    @Override
    public void deleteApp(App app) {
        CoreManager.getImpl(IAppRepositoryApi.class).deleteApp(app);
    }

    @Override
    public void deleteApp(List<App> apps) {
        CoreManager.getImpl(IAppRepositoryApi.class).deleteApp(apps);
    }

    @Override
    public void navigationSettingsActivity(Context context) {
        PluginsSettingsActivity.navigationActivity(context);
    }

    @Override
    public void onCreate() {

    }
}
