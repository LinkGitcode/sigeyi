/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sigeyi.mvp.presenter;

import android.content.Context;

import com.prize.feedstream.mvp.contract.Contract;
import com.prize.feedstream.mvp.model.entity.feedsbean.FeedsDataBean;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PicRecomPresenter extends BasePresenter<Contract.PicRecomModel, Contract.PicRecomView> {

    public PicRecomPresenter(Context ctx, Contract.PicRecomModel model, Contract.PicRecomView rootView) {
        super(ctx, model, rootView);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void loadDatas(String url) {
        super.loadDatas();
        mModel.getPicRecom(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ArrayList<FeedsDataBean>>() {
            @Override
            public void accept(ArrayList<FeedsDataBean> dataBeans) throws Exception {
                FeedsDataBean feedsDataBean = new FeedsDataBean();
                feedsDataBean.setPicDeatilsType(true);
                dataBeans.add(1,feedsDataBean);
                mRootView.getPicSuccess(dataBeans);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mRootView.getPicError(throwable);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
