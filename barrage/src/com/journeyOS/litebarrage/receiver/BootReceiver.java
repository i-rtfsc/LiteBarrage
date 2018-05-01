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

package com.journeyOS.litebarrage.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.journeyOS.base.utils.LogUtils;
import com.journeyOS.litebarrage.services.BarrageService;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();
    private static final String ACTION_BARRAGE_SERVICE = "com.journeyOS.barrage.BService";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "onReceive() called with: intent action = [" + intent.getAction() + "]");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent service = new Intent(context, BarrageService.class);
            service.setAction(ACTION_BARRAGE_SERVICE);
            context.startService(service);
        }
    }

}
