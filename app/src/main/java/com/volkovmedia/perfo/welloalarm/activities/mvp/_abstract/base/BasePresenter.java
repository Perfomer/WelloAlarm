package com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.base;

import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.IMvpModel;
import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.IMvpPresenter;
import com.volkovmedia.perfo.welloalarm.activities.mvp._abstract.IMvpView;

public abstract class BasePresenter<V extends IMvpView, M extends IMvpModel> implements IMvpPresenter<V, M> {

    private V mView;
    private M mModel;

    protected BasePresenter(M model) {
        this.mModel = model;
    }

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }


    @Override
    public void destroy() {
        mModel.destroy();
    }

    protected M getModel() {
        return mModel;
    }

    protected V getView() {
        return mView;
    }
}
