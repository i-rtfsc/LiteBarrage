package com.journeyOS.core.api.daemon;

import android.content.Context;

import com.journeyOS.core.api.ICoreApi;

public interface IAliveApi extends ICoreApi {
    void keepAlive(Context context);
}
