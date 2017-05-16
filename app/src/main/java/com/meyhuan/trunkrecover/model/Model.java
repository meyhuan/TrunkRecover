package com.meyhuan.trunkrecover.model;

import android.app.Application;
import android.os.Handler;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class Model {

    private Application application;
    private Handler ioHandler;
    protected Handler mainHandler;

    public void init(Application application, Handler ioHandler) {
        this.application = application;
        this.ioHandler = ioHandler;
        this.mainHandler = new Handler(application.getMainLooper());
    }

    public void runInIoThread(Runnable runnable){
        ioHandler.post(runnable);
    }

    protected void runInMianHandler(Runnable runnable) {
        mainHandler.post(runnable);
    }
}
