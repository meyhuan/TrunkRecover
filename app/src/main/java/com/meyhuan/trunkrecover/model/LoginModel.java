package com.meyhuan.trunkrecover.model;

import com.meyhuan.trunkrecover.Event.MainEvent;
import com.meyhuan.trunkrecover.rxbus.RxBus;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class LoginModel extends Model {

    public void getUserInfo(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RxBus.getDefaule().post(new MainEvent.LoginEvent("i an kk..."));
    }

}
