package com.meyhuan.trunkrecover;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.meyhuan.trunkrecover.presenter.MainPresenter;
import com.meyhuan.trunkrecover.view.MainView;

public class MainActivity extends BaseMvpActivity<MainView, MainPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
