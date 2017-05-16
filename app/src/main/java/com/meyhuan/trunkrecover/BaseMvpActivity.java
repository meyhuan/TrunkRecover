package com.meyhuan.trunkrecover;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.meyhuan.trunkrecover.presenter.BaseMvpPresenter;
import com.meyhuan.trunkrecover.presenter.PresenterUtil;

import java.lang.reflect.ParameterizedType;

/**
 * User  : guanhuan
 * Date  : 2017/5/15
 */

public class BaseMvpActivity<V extends MvpView, P extends BaseMvpPresenter<V>> extends MvpActivity<V, P>{
    @NonNull
    @Override
    public P createPresenter() {

        if(presenter == null){
            //检查父类是否是泛型类，并且泛型类的个数大于 0
            if(getClass().getGenericSuperclass() instanceof ParameterizedType &&
                    ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0){
               //获取真正泛型类数组的第二个
                Class mPresenterClass = (Class) ((ParameterizedType) (this.getClass()
                        .getGenericSuperclass())).getActualTypeArguments()[1];
                presenter = PresenterUtil.getInstance(mPresenterClass);
            }else {
                try{
                    presenter = (P) new BaseMvpPresenter();
                }catch(Exception e){
                    Log.e("BaseMvpActivity", "createPresenter fail, must confirm MvpBasePresenter ParameterizedType");
                }
            }
        }

        return presenter;
    }
}
