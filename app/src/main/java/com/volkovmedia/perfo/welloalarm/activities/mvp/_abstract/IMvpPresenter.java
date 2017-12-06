package com.volkovmedia.perfo.welloalarm.activities.mvp._abstract;

public interface IMvpPresenter<V extends IMvpView, M extends IMvpModel> {

    void attachView(V view);
    void detachView();

    void viewIsReady();
    void destroy();

}
