package com.sigeyi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sigeyi.R;
import com.sigeyi.activity.base.BaseActivity;
import com.sigeyi.csc.CSCActivity;
import com.sigeyi.dfu.DfuActivity;
import com.sigeyi.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.home)
    private View mHome;
    @BindView(R.id.home_dfu)
    private Button mDfu;
    @BindView(R.id.home_data)
    private Button mDatas;

    // 保存用户按返回键的时间
    private long mExitTime = 0;
    private Button mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        //状态栏透明和间距处理
        //StatusBarUtil.immersive(this, 0xff000000, 0f);

        initData();
    }

    private void initData() {
        mDatas = findViewById(R.id.home_data);
        mDfu = findViewById(R.id.home_dfu);
        mTest = findViewById(R.id.test);

        mDatas.setOnClickListener(this);
        mDfu.setOnClickListener(this);
        mTest.setOnClickListener(this);

    }

    private void startActivity(Context ctx, Class clazz) {
        Intent intent = new Intent(ctx, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Snackbar.make(mHome, R.string.exit_toast, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mDfu)
            startActivity(this, DfuActivity.class);
        else if (view == mDatas)
            startActivity(this, CSCActivity.class);
        else if (view == mTest)
            startActivity(this, DfuNetAcitivity.class);
    }
}
