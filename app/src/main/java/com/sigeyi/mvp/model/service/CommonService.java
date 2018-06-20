package com.sigeyi.mvp.model.service;

import com.sigeyi.mvp.model.entity.FirmwareBean;
import com.sigeyi.mvp.model.entity.FirmwareJson;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CommonService {

    @GET("api/ad/third-source/get")
    Observable<FirmwareJson> getUpdateInfo(@QueryMap Map<String, String> map);
}
