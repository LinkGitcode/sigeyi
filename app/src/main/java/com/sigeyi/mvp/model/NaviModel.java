package com.sigeyi.mvp.model;

import com.sigeyi.http.RetrofitCreater;
import com.sigeyi.mvp.contract.Contract;
import com.sigeyi.mvp.model.service.CommonService;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;


public class NaviModel implements Contract.NaviModel {

    @Override
    public Observable<List<String>> getNavigations() {
        Retrofit retrofit = RetrofitCreater.getInstance().getRetrofit();
        Observable<List<String>> listObservable = retrofit.create(CommonService.class)
                    .map();
        return listObservable;
    }

    @Override
    public void onDestroy() {

    }
}
