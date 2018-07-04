package com.sigeyi.mvp.contract;

public interface IPresenter {

    /**
     * 绑定presenter的时候调用
     */
    void onAttach();

    /**
     * 解绑presenter的时候调用
     */
    void onDetach();
}
