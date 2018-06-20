package com.sigeyi.mvp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.prize.feedstream.mvp.contract.Contract;
import com.prize.feedstream.mvp.model.entity.navibean.NaviTable;
import com.prize.feedstream.utils.OpenAppManager;
import com.prize.inforstream.R;
import com.prize.inforstream.bean.PushNaviTable;
import com.prize.inforstream.publicutils.UmengUtils;
import com.prize.inforstream.view.InforStreamView;
import com.prize.leftsearch.searchmvp.view.AnimFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chungo on 2018/2/19.
 */

public class NaviView implements Contract.NaviView, View.OnClickListener {

    private LayoutInflater mInflater = null;
    private LinearLayout mParentView = null;
    private LinearLayout.LayoutParams params;
    private int indexOfChild;
    private Context mCtx;

    public NaviView(LinearLayout view, Context ctx) {
        this.mParentView = view;
        this.mCtx = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public void refreshView(List<NaviTable> naviTables) {
        Log.i("hcg", "  naviTables=" + naviTables);
        addViews(naviTables);
    }
    
    @Override
    public void showTips(String msg) {

    }

    @Override
    public void showDefView() {

    }

    @Override
    public void onDestory() {

    }


    int page = 0;
    private void addViews(List<NaviTable> naviDatas) {
        int childSz = mParentView.getChildCount();
        int row = (int) mParentView.getWeightSum();
        if (row == 0)
            row = 5;
        int start = page * row;
        List<NaviTable> temp = new ArrayList<>();
        int count = Math.min(row + start, naviDatas.size());
        for (int i = start; i < count; i++) {
            temp.add(naviDatas.get(i));
        }
        int dataSize = temp.size();
        page++;
        if (page >= naviDatas.size() / row) {
            page = 0;
        }
        if (dataSize < childSz) { // 先删除多余的VIEW
            for (int i = dataSize; i < childSz; i++) {
                mParentView.removeViewAt(dataSize);
            }
        }
        int rightPx = 0;
        int startIndex = 0;
        // 若有需要 替换VIEW
        int min = Math.min(dataSize, childSz);
        if (min > 0) {
            for (; startIndex < min; startIndex++) {
                NaviTable item = temp.get(startIndex);
                AnimFrameLayout frame = (AnimFrameLayout) mParentView.getChildAt(startIndex);
                initItemView(frame.getChildAt(0), item, startIndex);
            }
        }
        for (; startIndex < dataSize; startIndex++) {
            AnimFrameLayout frame = (AnimFrameLayout) mInflater.inflate(R.layout.push_view_item_base, null);
            NaviTable item = naviDatas.get(startIndex);
            View itemView = mInflater.inflate(R.layout.push_item, null);

            initItemView(itemView, item, startIndex);

            frame.replaceView(itemView, startIndex);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            if (startIndex == 0) {
                params.leftMargin = 0;
                params.rightMargin = rightPx;
            } else if (startIndex == dataSize - 1) {
                params.leftMargin = rightPx;
                params.rightMargin = 0;
            } else {
                params.leftMargin = rightPx;
                params.rightMargin = rightPx;
            }
            mParentView.addView(frame, params);
        }
    }

    /***
     * 绑定指定的VIEW
     *
     * @param v
     * @param item
     */
    private void initItemView(final View v, final NaviTable item, final int startIndex) {
        v.setTag(item);
        v.setOnClickListener(this);
        v.post(new Runnable() {

            @Override
            public void run() {

                // 标题或名称
                TextView txtName = (TextView) v.findViewById(R.id.txt_title);
                txtName.setText(item.name);
                String imgUrl = item.icon_url;
                ImageView imgView = (ImageView) v.findViewById(R.id.img_left);
                imgView.setTag(item);
                imgView.setOnClickListener(NaviView.this);
                if (startIndex == 0) {
                    imgView.setImageResource(R.drawable.youxi);

                } else if (startIndex == 1) {
                    imgView.setImageResource(R.drawable.yule);
                } else if (startIndex == 2) {
                    imgView.setImageResource(R.drawable.shiping);
                } else if (startIndex == 3) {
                    imgView.setImageResource(R.drawable.junshi);
                } else if (startIndex == 4) {
                    imgView.setImageResource(R.drawable.meitu);
                }
                if (InforStreamView.image) {
                    ImageLoader.getInstance().displayImage(imgUrl, imgView);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        PushNaviTable t = (PushNaviTable) v.getTag();
        OpenAppManager.openWebview(mCtx,"", t.link_url);
//        Intent it = new Intent(mCtx, WVLWebViewActivity.class);
//        it.putExtra(WVLWebViewActivity.P_URL, t.link_url);
//        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mCtx.startActivity(it);
//        it = null;
        try {
            View frame = (View) v.getParent().getParent().getParent();
            indexOfChild = mParentView.indexOfChild(frame);
            if (indexOfChild == 0)
                UmengUtils.onMilitary(UmengUtils.getInfoStreamView(mParentView).mActivity);
            else if (indexOfChild == 1)
                UmengUtils.onFunny(UmengUtils.getInfoStreamView(mParentView).mActivity);
            else if (indexOfChild == 2)
                UmengUtils.onBeauty(UmengUtils.getInfoStreamView(mParentView).mActivity);
            else if (indexOfChild == 3)
                UmengUtils.onVideo(UmengUtils.getInfoStreamView(mParentView).mActivity);
            else if (indexOfChild == 4)
                UmengUtils.onEntertainment(UmengUtils.getInfoStreamView(mParentView).mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
