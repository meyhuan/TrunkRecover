package com.meyhuan.trunkrecover.model;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class InjectHelper {

    private static Application context;
    private static Handler ioHanler;

    private static Map<Class<? extends Model>, Model> modelMap = new ConcurrentHashMap<>();

    static {
        HandlerThread ioThread = new HandlerThread("IOThread");
        ioThread.start();
        ioHanler = new Handler(ioThread.getLooper());
    }

    private static void initModel(Class<? extends Model> clazz) {
        Model model = modelMap.get(clazz);
        if(model == null){
            try {
                model = clazz.newInstance();
                model.init(context, ioHanler);
                modelMap.put(clazz, model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T extends Model> T getModel(Class<T> clazz) {
        T model = (T) modelMap.get(clazz);
        if(model == null){
            initModel(clazz);
            model = (T) modelMap.get(clazz);
        }
        return model;
    }
}
