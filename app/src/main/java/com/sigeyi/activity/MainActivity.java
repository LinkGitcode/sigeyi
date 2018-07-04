package com.sigeyi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sigeyi.R;
import com.sigeyi.activity.base.BaseActivity;
import com.sigeyi.csc.CSCActivity;
import com.sigeyi.dfu.DfuActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    @BindView(R.id.home)
    View mHome;
    @BindView(R.id.home_dfu)
    TextView mDfu;
    @BindView(R.id.home_data)
    TextView mDatas;

    // 保存用户按返回键的时间
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏透明和间距处理
        //StatusBarUtil.immersive(this, 0x00e3e3e3, 0f);
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

    @OnClick({R.id.home_dfu, R.id.home_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_dfu:
                startActivity(this, DfuActivity.class);
                break;
            case R.id.home_data:
                startActivity(this, CSCActivity.class);
                break;
            default:
                break;
        }
    }
}
