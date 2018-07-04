package com.sigeyi.mvp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.model.Response;
import com.sigeyi.mvp.contract.Contract;
import com.sigeyi.mvp.model.entity.FirmwareBean;
import com.sigeyi.utils.FileDownloader;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;


public class FirmwareUpdatePresenter extends BasePresenter<Contract.FirmwareUpdateModel, Contract.FirmwareUpdateView> {

    private FileDownloader mDownloader;

    public FirmwareUpdatePresenter(Context ctx, Contract.FirmwareUpdateModel model, Contract.FirmwareUpdateView rootView) {
        super(ctx, model, rootView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onAttach() {
        super.onAttach();
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
                        mRootView.loadFile(response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.d("hcg", "onError" + e.toString());
                        Toast.makeText(mCtx,"下载失败，链接失效!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("hcg", "onComplete");
                    }
                });
    }
}
