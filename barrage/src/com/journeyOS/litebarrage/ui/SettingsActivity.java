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

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.journeyOS.base.utils.UIUtils;
import com.journeyOS.core.CoreManager;
import com.journeyOS.core.base.BaseActivity;
import com.journeyOS.core.base.BaseFragment;
import com.journeyOS.core.permission.IPermissionApi;
import com.journeyOS.litebarrage.R;
import com.journeyOS.litebarrage.services.BarrageService;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void initBeforeView() {
        super.initBeforeView();
        CoreManager.getImpl(IPermissionApi.class).initUrgentPermission(this);

        Intent intent = new Intent(SettingsActivity.this, BarrageService.class);
        startService(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.settings_activity;
    }

    @Override
    public void initViews() {
        UIUtils.setStatusBarColor(this, this.getResources().getColor(R.color.lightskyblue));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BaseFragment fragment = new SettingsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
