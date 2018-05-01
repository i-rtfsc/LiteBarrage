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

package com.journeyOS.base.widget.barrage;

import android.graphics.Color;

import com.journeyOS.base.R;

public class Barrage {
    private String content;
    private int color;
    private boolean showBorder;
    private int backGroundColor;

    public Barrage(String content) {
        this(content, R.color.violet, false, Color.WHITE);
    }

    public Barrage(String content, int color) {
        this(content, color, false, Color.WHITE);
    }

    public Barrage(String content, boolean showBorder) {
        this(content, R.color.black, showBorder, Color.WHITE);
    }

    public Barrage(String content, int color, int backGroundColor) {
        this(content, color, false, backGroundColor);
    }

    private Barrage(String content, int color, boolean showBorder, int backGroundColor) {
        this.content = content;
        this.color = color;
        this.showBorder = showBorder;
        this.backGroundColor = backGroundColor;
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }
}
