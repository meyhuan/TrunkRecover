package com.meyhuan.trunkrecover.presenter;

import android.widget.Toast;

import com.example.annotation.apt.PresenterFactory;
import com.example.annotation.javassist.BusEvent;
import com.example.annotation.javassist.ProviderModel;
import com.meyhuan.trunkrecover.Event.MainEvent;
import com.meyhuan.trunkrecover.MyApp;
import com.meyhuan.trunkrecover.model.LoginModel;
import com.meyhuan.trunkrecover.view.MainView;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */
@PresenterFactory
public class MainPresenter extends BaseMvpPresenter<MainView>{

    @ProviderModel
    private LoginModel loginModel;

    public void getUserInfo(){
        loginModel.getUserInfo();
    }

    @BusEvent
    public void onLogin(MainEvent.LoginEvent event){

        Toast.makeText(MyApp.CONTEXT, "login success ..", Toast.LENGTH_LONG).show();
        getView().setUserInfo(event.param1);
    }
}
