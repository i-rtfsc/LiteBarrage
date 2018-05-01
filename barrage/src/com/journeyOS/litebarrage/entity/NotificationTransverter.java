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

package com.journeyOS.litebarrage.entity;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

public class NotificationTransverter {

    public static MessageInfos convertFromNotification(StatusBarNotification sbn) {
        MessageInfos msg = new MessageInfos();
        msg.packageName = sbn.getPackageName();

        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;
        if (extras != null) {
            msg.title = extras.getString("android.title");
            msg.text = extras.getString("android.text");
        }

        return msg;
    }
}
