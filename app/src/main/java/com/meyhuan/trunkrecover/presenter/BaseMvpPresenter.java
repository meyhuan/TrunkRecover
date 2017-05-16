package com.meyhuan.trunkrecover.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.meyhuan.trunkrecover.rxbus.RxBus;
import com.meyhuan.trunkrecover.rxbus.RxInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * User  : guanhuan
 * Date  : 2017/5/15
 */

public class BaseMvpPresenter <V extends MvpView> extends MvpBasePresenter<V> {

    private Disposable subscription;

    @Override
    public void attachView(V view) {
        super.attachView(view);
        subscription = RxBus.getDefaule().register(RxInterface.RxEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RxInterface.RxEvent>() {
                    @Override
                    public void accept(RxInterface.RxEvent rxEvent) throws Exception {
                        onEventHandle(rxEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("BaseMvpPresenter", throwable.getMessage());
                    }
                });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if(subscription != null && !subscription.isDisposed()){
            subscription.dispose();
        }

    }

    public void onEventHandle(RxInterface.RxEvent enent){

    }

}
