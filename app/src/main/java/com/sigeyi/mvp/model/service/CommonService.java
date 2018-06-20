package com.sigeyi.mvp.model.service;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CommonService {

    @GET("api/ad/third-source/get")
    Observable<String> getAds(@QueryMap Map<String, String> map);

    @GET("api/news/v0/app/get")
    Observable<String> getFeeds(@QueryMap Map<String, String> requestMap);

}
