package com.meyhuan.trunkrecover.Event;

import com.meyhuan.trunkrecover.rxbus.RxInterface;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public interface MainEvent {

    class LoginEvent extends RxInterface.SingleEvent<String>{

        public LoginEvent(String param1) {
            super(param1);
        }
    }

}
