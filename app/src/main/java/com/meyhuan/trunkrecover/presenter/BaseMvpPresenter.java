package com.meyhuan.trunkrecover.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * User  : guanhuan
 * Date  : 2017/5/15
 */

public class BaseMvpPresenter <V extends MvpView> extends MvpBasePresenter<V> {


    @Override
    public void attachView(V view) {
        super.attachView(view);
    }


    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

    }


}
