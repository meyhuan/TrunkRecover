package com.meyhuan.trunkrecover;

import android.app.Application;
import android.content.Context;

import com.meyhuan.trunkrecover.model.InjectHelper;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class MyApp extends Application{

    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
        InjectHelper.init(this);
    }
}
