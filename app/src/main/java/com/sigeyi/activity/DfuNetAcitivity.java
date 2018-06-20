package com.sigeyi.activity;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.sigeyi.R;
import com.sigeyi.dfu.DfuService;
import com.sigeyi.http.Api;
import com.sigeyi.mvp.contract.Contract;
import com.sigeyi.mvp.model.FirmwareUpdateModel;
import com.sigeyi.mvp.model.entity.FirmwareBean;
import com.sigeyi.mvp.presenter.FirmwareUpdatePresenter;
import com.sigeyi.utils.FileDownloader;
import com.sigeyi.utils.ScannerUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

public class DfuNetAcitivity extends AppCompatActivity implements Contract.FirmwareUpdateView {

    private static final String DATA_DEVICE = "device";
    private static final String DATA_FILE_TYPE = "file_type";
    private static final String DATA_FILE_TYPE_TMP = "file_type_tmp";
    private static final String DATA_FILE_PATH = "file_path";
    private static final String DATA_FILE_STREAM = "file_stream";
    private static final String DATA_INIT_FILE_PATH = "init_file_path";
    private static final String DATA_INIT_FILE_STREAM = "init_file_stream";
    private static final String DATA_STATUS = "status";
    private static final String DATA_SCOPE = "scope";
    private static final String DATA_DFU_COMPLETED = "dfu_completed";
    private static final String DATA_DFU_ERROR = "dfu_error";

    private FirmwareUpdatePresenter mPresenter;
    private static final int ENABLE_BT_REQ = 0;

    @BindView(R.id.dfu_device_btn)
    Button mDeviceBtn;
    @BindView(R.id.dfu_file_btn)
    Button mUploadBtn;
    @BindView(R.id.dfu_file_tv)
    TextView mFileTv;
    @BindView(R.id.dfu_device_tv)
    TextView mDeviceTv;
    @BindView(R.id.load)
    Button mTestLoad;
    @BindView(R.id.dfu_upload_pb)
    ProgressBar mUploadProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfu_net_acitivity);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        onInitView(savedInstanceState);
        test();
    }

    private void test() {
       View load = findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDownloading();
            }
        });
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        mPresenter = new FirmwareUpdatePresenter(this, new FirmwareUpdateModel(), this);
//        isBLESupported();
//        if (!isBLEEnabled()) {
//            showBLEDialog();
//        }

//        // restore saved state
//        mFileType = DfuService.TYPE_AUTO; // Default
//        restoreState(savedInstanceState);
//
//        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
//
//        ScannerUtils utils = new ScannerUtils();
//        BluetoothDevice device = utils.getBondedBLE(this);
//        if (device == null)
//            return;
//        String name = device.getName();
//        mSelectedDevice = device;
//        mUploadBtn.setEnabled(mStatusOk);
//        mDeviceNameView.setText(name != null ? name : getString(R.string.not_available));
    }

//    private void restoreState(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            mFileType = savedInstanceState.getInt(DATA_FILE_TYPE);
//            mFileTypeTmp = savedInstanceState.getInt(DATA_FILE_TYPE_TMP);
//            mFilePath = savedInstanceState.getString(DATA_FILE_PATH);
//            mFileStreamUri = savedInstanceState.getParcelable(DATA_FILE_STREAM);
//            mInitFilePath = savedInstanceState.getString(DATA_INIT_FILE_PATH);
//            mInitFileStreamUri = savedInstanceState.getParcelable(DATA_INIT_FILE_STREAM);
//            mSelectedDevice = savedInstanceState.getParcelable(DATA_DEVICE);
//            mStatusOk = mStatusOk || savedInstanceState.getBoolean(DATA_STATUS);
//            mScope = savedInstanceState.containsKey(DATA_SCOPE) ? savedInstanceState.getInt(DATA_SCOPE) : null;
//            mUploadButton.setEnabled(mSelectedDevice != null && mStatusOk);
//            mDfuCompleted = savedInstanceState.getBoolean(DATA_DFU_COMPLETED);
//            mDfuError = savedInstanceState.getString(DATA_DFU_ERROR);
//        }
//    }

    private void isBLESupported() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d("hcg", "Device doesn\\'t have BLE support!");
            finish();
        }
    }

    private boolean isBLEEnabled() {
        final BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = manager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    private void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, ENABLE_BT_REQ);
    }

    private void performDownloading() {
        mPresenter.downloadFirmware(Api.testFirmwareURL1, null);
    }

    @Override
    public void refreshView(ArrayList<FirmwareBean> FirmwareBeans) {

    }

    @Override
    public void onDestoryView() {

    }

//    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
//        @Override
//        public void onDeviceConnecting(final String deviceAddress) {
//            mProgressBar.setIndeterminate(true);
//            mTextPercentage.setText(R.string.dfu_status_connecting);
//        }
//
//        @Override
//        public void onDfuProcessStarting(final String deviceAddress) {
//            mProgressBar.setIndeterminate(true);
//            mTextPercentage.setText(R.string.dfu_status_starting);
//        }
//
//        @Override
//        public void onEnablingDfuMode(final String deviceAddress) {
//            mProgressBar.setIndeterminate(true);
//            mTextPercentage.setText(R.string.dfu_status_switching_to_dfu);
//        }
//
//        @Override
//        public void onFirmwareValidating(final String deviceAddress) {
//            mProgressBar.setIndeterminate(true);
//            mTextPercentage.setText(R.string.dfu_status_validating);
//        }
//
//        @Override
//        public void onDeviceDisconnecting(final String deviceAddress) {
//            mProgressBar.setIndeterminate(true);
//            mTextPercentage.setText(R.string.dfu_status_disconnecting);
//        }
//
//        @Override
//        public void onDfuCompleted(final String deviceAddress) {
//            mTextPercentage.setText(R.string.dfu_status_completed);
//            if (mResumed) {
//                // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
//                new Handler().postDelayed(() -> {
//                    onTransferCompleted();
//
//                    // if this activity is still open and upload process was completed, cancel the notification
//                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.cancel(DfuService.NOTIFICATION_ID);
//                }, 200);
//            } else {
//                // Save that the DFU process has finished
//                mDfuCompleted = true;
//            }
//        }
//
//        @Override
//        public void onDfuAborted(final String deviceAddress) {
//            mTextPercentage.setText(R.string.dfu_status_aborted);
//            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
//            new Handler().postDelayed(() -> {
//                onUploadCanceled();
//
//                // if this activity is still open and upload process was completed, cancel the notification
//                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.cancel(DfuService.NOTIFICATION_ID);
//            }, 200);
//        }
//
//        @Override
//        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
//            mProgressBar.setIndeterminate(false);
//            mProgressBar.setProgress(percent);
//            mTextPercentage.setText(getString(R.string.dfu_uploading_percentage, percent));
//            if (partsTotal > 1)
//                mTextUploading.setText(getString(R.string.dfu_status_uploading_part, currentPart, partsTotal));
//            else
//                mTextUploading.setText(R.string.dfu_status_uploading);
//        }
//
//        @Override
//        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
//            if (mResumed) {
//                showErrorMessage(message);
//
//                // We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
//                new Handler().postDelayed(() -> {
//                    // if this activity is still open and upload process was completed, cancel the notification
//                    final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.cancel(DfuService.NOTIFICATION_ID);
//                }, 200);
//            } else {
//                mDfuError = message;
//            }
//        }
//    };
}
