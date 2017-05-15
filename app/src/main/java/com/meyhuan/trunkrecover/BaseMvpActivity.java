package com.meyhuan.trunkrecover;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.meyhuan.trunkrecover.presenter.BaseMvpPresenter;

/**
 * User  : guanhuan
 * Date  : 2017/5/15
 */

public class BaseMvpActivity<V extends MvpView, P extends BaseMvpPresenter<V>> extends MvpActivity<V, P>{
    @NonNull
    @Override
    public P createPresenter() {
        return null;
    }
}
