package com.volkovmedia.perfo.welloalarm.activities.mvp._abstract;

public interface MvpPresenter {

    void attachView(MvpView view);
    void detachView();

    void viewIsReady();

}
