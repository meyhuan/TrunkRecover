package com.meyhuan.trunkrecover.presenter;

import com.apt.presenter.PresenterFactory;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class PresenterUtil {

    public static <T> T getInstance(Class<?> clazz) {

        try {
            return (T) PresenterFactory.create(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
