package com.sigeyi.mvp.contract;

import com.sigeyi.mvp.model.entity.FirmwareBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * MV接口
 */
public interface Contract {

    interface FirmwareUpdateView extends IView {
        void refreshView(ArrayList<FirmwareBean> FirmwareBeans);
    }

    interface FirmwareUpdateModel extends IModel {
        Observable<ArrayList<FirmwareBean>> getUpdateInfo(HashMap<String, String> map);
    }
}
