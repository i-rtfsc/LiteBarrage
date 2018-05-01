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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderTextView extends TextView {

    public BorderTextView(Context context, int border_color) {
        this(context, null, border_color);
    }

    public BorderTextView(Context context, AttributeSet attrs, int border_color) {
        super(context, attrs);
        this.border_color = border_color;
    }

    private int border_color;

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(border_color);
        paint.setStrokeWidth(5);
        canvas.drawLine(0, 0, this.getWidth(), 0, paint);
        canvas.drawLine(0, 0, 0, this.getHeight(), paint);
        canvas.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight(), paint);
        canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), paint);
        super.onDraw(canvas);
    }
}
