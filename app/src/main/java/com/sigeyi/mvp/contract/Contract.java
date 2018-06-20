package com.sigeyi.mvp.contract;

import java.util.List;

import io.reactivex.Observable;

/**
 * MV接口
 */
public interface Contract {

    interface NaviView extends IView {
        void refreshView(List<String> naviTables);
    }

    interface NaviModel extends IModel {
        Observable<List<String>> getNavigations();
    }
}
