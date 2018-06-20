package com.sigeyi.utils;

import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.FileConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 文件下载器，给我一个url，还你一个文件
 * Created by huangchangguo on 2018/6/19.
 */

public class FileDownloader {

    public Observable<Response<File>> download(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        Observable<Response<File>> observable = OkGo.<File>get(url)
                .converter(new FileConvert())
                .adapt(new ObservableResponse<>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {

                    }
                });
        return observable;
    }

}
