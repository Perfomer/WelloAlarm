package com.volkovmedia.perfo.welloalarm.activities.mvp.impl;

public interface MvpPresenter {

    void attachView(MvpView view);
    void detachView();

    void viewIsReady();

}
