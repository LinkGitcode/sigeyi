package com.sigeyi.mvp.model;

import com.sigeyi.http.RetrofitCreater;
import com.sigeyi.mvp.contract.Contract;
import com.sigeyi.mvp.model.entity.FirmwareBean;
import com.sigeyi.mvp.model.entity.FirmwareJson;
import com.sigeyi.mvp.model.service.CommonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class FirmwareUpdateModel implements Contract.FirmwareUpdateModel {

    @Override
    public Observable<ArrayList<FirmwareBean>> getUpdateInfo(HashMap<String, String> map) {
        Retrofit retrofit = RetrofitCreater.getInstance().getRetrofit();
        return retrofit
                .create(CommonService.class)
                .getUpdateInfo(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<FirmwareJson, ArrayList<FirmwareBean>>() {
                    @Override
                    public ArrayList<FirmwareBean> apply(FirmwareJson firmwareJson) throws Exception {
                        ArrayList<FirmwareBean> data = firmwareJson.getData();
                        return data;
                    }
                });
    }

    @Override
    public void onDestroy() {

    }


}
