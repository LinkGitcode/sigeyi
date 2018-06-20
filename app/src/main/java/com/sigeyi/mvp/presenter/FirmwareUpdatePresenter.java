package com.sigeyi.mvp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;


import com.lzy.okgo.model.Response;
import com.sigeyi.mvp.contract.Contract;
import com.sigeyi.mvp.model.entity.FirmwareBean;
import com.sigeyi.utils.FileDownloader;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class FirmwareUpdatePresenter extends BasePresenter<Contract.FirmwareUpdateModel, Contract.FirmwareUpdateView> {

    private FileDownloader mDownloader;

    public FirmwareUpdatePresenter(Context ctx, Contract.FirmwareUpdateModel model, Contract.FirmwareUpdateView rootView) {
        super(ctx, model, rootView);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @SuppressLint("CheckResult")
    @Override
    public void onLoadList() {
        super.onLoadList();

        mModel.getUpdateInfo(null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<FirmwareBean>>() {
                    @Override
                    public void accept(ArrayList<FirmwareBean> firmwareBeans) throws Exception {
                        mRootView.refreshView(firmwareBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.d(throwable);
                    }
                });
    }

    public void downloadFirmware(String url, String md5) {
        if (mDownloader == null)
            mDownloader = new FileDownloader();
        mDownloader.download(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<File>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<File> response) {
                        File file = response.body();
                        file.getAbsolutePath();
                        URI uri = file.toURI();
                        Log.d("hcg", "onNext" + file.getAbsolutePath()+"  uri="+uri);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d("hcg", "onError" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("hcg", "onComplete");
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
