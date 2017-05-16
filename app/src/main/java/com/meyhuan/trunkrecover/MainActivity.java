package com.meyhuan.trunkrecover;

import android.os.Bundle;
import android.widget.TextView;

import com.meyhuan.trunkrecover.presenter.MainPresenter;
import com.meyhuan.trunkrecover.view.MainView;

public class MainActivity extends BaseMvpActivity<MainView, MainPresenter> implements MainView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPresenter().getUserInfo();
    }

    @Override
    public void setUserInfo(String userInfo) {
        ((TextView)(findViewById(R.id.tv_user_info))).setText(userInfo);
    }
}
