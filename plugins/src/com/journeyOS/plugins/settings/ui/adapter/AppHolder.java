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

package com.journeyOS.plugins.settings.ui.adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.journeyOS.base.adapter.BaseRecyclerAdapter;
import com.journeyOS.base.adapter.BaseViewHolder;
import com.journeyOS.base.widget.SettingSwitch;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.api.appprovider.App;
import com.journeyOS.plugins.R;
import com.journeyOS.plugins.R2;
import com.journeyOS.plugins.settings.app.repository.IAppRepositoryApi;

import butterknife.BindView;
import butterknife.OnClick;

public class AppHolder extends BaseViewHolder<AppInfoData> {

    @BindView(R2.id.app_item)
    SettingSwitch mSwitch;

    AppInfoData mAppInfoData;

    public AppHolder(View itemView, BaseRecyclerAdapter baseRecyclerAdapter) {
        super(itemView, baseRecyclerAdapter);
    }

    @Override
    public void updateItem(AppInfoData data, int position) {
        mAppInfoData = data;
        mSwitch.setIcon(data.getDrawable());
        mSwitch.setTitle(data.getAppName());
        mSwitch.setCheck(data.getToogle());
    }

    @Override
    public int getContentViewId() {
        return R.layout.app_item;
    }


    @OnClick({R2.id.app_item})
    void listenerSwitch() {
        CoreManager.getImpl(IAppRepositoryApi.class).getAppWorkHandler().post(new Runnable() {
            @Override
            public void run() {
                String packageName = mAppInfoData.getPackageName();
                App app = CoreManager.getImpl(IAppRepositoryApi.class).getApp(packageName);
                boolean toggle = !app.toggle;
                setChecked(toggle);
                app.toggle = toggle;
                CoreManager.getImpl(IAppRepositoryApi.class).updateApp(app);
            }
        });
    }

    void setChecked(final boolean checked) {
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwitch.setCheck(checked);
            }
        });

    }
}
