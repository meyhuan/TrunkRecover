package com.meyhuan.trunkrecover.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class RxBus {

    private final Subject<Object> bus;

    private RxBus (){
        //PublishSubject 之后的注册的事件全部发射
        bus = PublishSubject.create().toSerialized();
    }

    private static class RxBusCreator {
        private static RxBus instance = new RxBus();
    }

    public static RxBus getDefaule(){
        return RxBusCreator.instance;
    }


    //向注册的观察者分发事件
    public void post(RxInterface.RxEvent event){
        bus.onNext(event);
    }

    //注册事件，返回被观察者用于注销注册
    public <T extends RxInterface> Observable<T> register(Class<T> clazz){
        return bus.ofType(clazz);
    }

}
