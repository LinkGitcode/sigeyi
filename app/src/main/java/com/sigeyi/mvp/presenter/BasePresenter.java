package com.sigeyi.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import com.sigeyi.mvp.contract.IModel;
import com.sigeyi.mvp.contract.IView;
import com.sigeyi.http.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    protected final String TAG = this.getClass().getSimpleName();
    protected CompositeDisposable mCompositeDisposable;

    protected M mModel;
    protected V mRootView;
    protected Context mCtx;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     *
     * @param model
     * @param rootView
     */
    public BasePresenter(Context ctx, M model, V rootView) {
        Preconditions.checkNotNull(model, "%s cannot be null", IModel.class.getName());
        Preconditions.checkNotNull(rootView, "%s cannot be null", IView.class.getName());
        this.mCtx = ctx;
        this.mModel = model;
        this.mRootView = rootView;
        onCreate();
    }

    /**
     * 如果当前页面不需要操作数据,只需要 View 层,则使用此构造函数
     *
     * @param rootView
     */
    public BasePresenter(V rootView) {
        Preconditions.checkNotNull(rootView, "%s cannot be null", IView.class.getName());
        this.mRootView = rootView;
        onCreate();
    }


    @Override
    public void onCreate() {

    }


    /**
     * 在框架中 {@link Activity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    @Override
    public void onDestroy() {
        unDispose();//解除订阅
        if (mModel != null)
            mModel.onDestroy();
        this.mModel = null;
        this.mRootView = null;
        this.mCompositeDisposable = null;
    }

    /**
     * 将 {@link Disposable} 添加到 {@link CompositeDisposable} 中统一管理
     *
     * @param disposable
     */
    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入集中处理
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }


}
