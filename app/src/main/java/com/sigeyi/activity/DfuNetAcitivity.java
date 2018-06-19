package com.sigeyi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lzy.okgo.model.Response;
import com.sigeyi.R;
import com.sigeyi.utils.FileDownloader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class DfuNetAcitivity extends AppCompatActivity {

    @BindView(R.id.load)
    private Button mLoad;
    String downloadUrl = "http://183.2.192.171/appdl.hicloud.com/dl/appdl/application/apk/56/5615f9e8fc1c4575962bbe71500be994/com.ifeng.newvideo.1806111622.apk?mkey=5b29675c12bb058c&f=2664&c=0&sign=portal@portal1529429208757&source=portalsite&p=.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfu_net_acitivity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View load = findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDownloading();
            }
        });
    }

    private void performDownloading() {
        FileDownloader downloader = new FileDownloader();
        Observable<Response<File>> download = downloader.download(downloadUrl);
        download.subscribe(new Observer<Response<File>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Response<File> response) {
                File file = response.body();
                file.getAbsolutePath();
                Log.d("hcg", "onNext" + file.getAbsolutePath());
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

}
