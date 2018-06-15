package com.sigeyi.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 跳转App的管理类,针对固定的几个推荐App
 *
 * @author huangchangguo
 * <p>
 * 2017.1.12
 */
public class OpenAppUtils {

    private static final String TAG = "huang-OpenAppManager";

    /**
     * type 1 Open App
     *
     * @param ctx
     * @param appPcgName detailUrl
     * @param appDetail  detail
     * @param appid
     */
    public static void openAppHome(Context ctx, String appPcgName,
                                   String appDetail, String appid) {
        Log.d(TAG, " |appPcgName:" + appPcgName + " |DetailUrl:" + " |appid:"
                + appid);
        if (ctx == null || appPcgName == null || appid == null)
            return;
        if (isPkgInstalled(appPcgName, ctx)) {
            Intent intent;
            try {
                intent = ctx.getPackageManager().getLaunchIntentForPackage(
                        appPcgName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            } catch (Exception e) {
                Log.d(TAG, "Launcher Special App Error!" + appPcgName);
            }

        } else {
            gotoAppCenter(ctx, appid);
        }
    }

    /**
     * open deeplink app
     *
     * @param ctx
     * @param appDetail
     */
    public static void openAppDetailThird(Context ctx,
                                          String appDetail) {

        if (ctx == null || appDetail == null)
            return;
        Intent intent = new Intent();
        intent.setData(Uri.parse(appDetail));
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            ctx.startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, "openAppDetailThird_Exception:" + e.toString());
            //gotoAppCenter(ctx, appid);
        }
    }

    /**
     * open 打开webview ,酷比浏览器
     *
     * @param ctx
     * @param title
     * @param url
     */
    public static void openWebview(Context ctx, String title, String url) {
        String intentUrl = "pb://prize:8888/browserDetail?website_url=" + url + "&website_title=" + title + "&website_stamp=" + "0";

        try {
            Intent intent = Intent.parseUri(intentUrl, Intent.URI_INTENT_SCHEME);
            intent.setPackage("com.prize.browser");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } catch (Exception e) {
            Log.d("hcg--", "e    =" + e.toString());
            e.printStackTrace();
            //openWithWebview(ctx, title, url);
            //openWithTestWebview(ctx);
        }
    }

    /**
     * 判断系统是否存在此应用
     *
     * @param pkgName
     * @param ctx
     * @return
     */
    public static boolean isPkgInstalled(String pkgName, Context ctx) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * openAppDetail for system installed
     *
     * @param ctx
     * @param appPcgName
     * @param appDetail
     * @param appid
     */
    public static void openAppDetail(Context ctx, String appPcgName,
                                     String appDetail, String appid) {
        if (ctx == null || appPcgName == null || appid == null)
            return;
        if (isPkgInstalled(appPcgName, ctx)) {
            swichApps(ctx, appPcgName, appDetail, appid);
        } else {
            Log.d(TAG, "openAppDetail-gotoAppCenter");
            gotoAppCenter(ctx, appid);
        }
    }

    private static void swichApps(Context ctx, String appPcgName,
                                  String appDetail, String appid) {

        if (appPcgName.contains("com.qiyi.video")) {
            wakeUpAQY(ctx, appDetail, appid);
        } else if (appPcgName.contains("com.ss.android.article.news")
                || appPcgName.contains("com.andreader.prein")) {
            // 头条
            Intent intent = new Intent();
            intent.setData(Uri.parse(appDetail));
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ctx.startActivity(intent);
            } catch (Exception e) {
            }

        } else if (appPcgName.contains("com.sankuai.meituan")
                || appPcgName.contains("com.tencent.news")
                || appPcgName.contains("com.tencent.reading")) {
            // 美团
            Intent intent = new Intent();
            intent.setData(Uri.parse(appDetail));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ctx.startActivity(intent);
            } catch (Exception e) {
            }
        } else if (appPcgName.contains("com.qihoo.browser")) {
            // 奇虎
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(appDetail));
            intent.putExtra("from", "kb");
            intent.setClassName("com.qihoo.browser",
                    "com.qihoo.browser.activity.SplashActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ctx.startActivity(intent);
            } catch (Exception e) {
            }
        }

    }

    /**
     * 特殊处理爱奇艺启动，兼容它的特定版本
     *
     * @param ctx
     * @param appDetail
     * @param appid
     */
    private static void wakeUpAQY(Context ctx, String appDetail, String appid) {
        int versionCode = 0;
        try {
            PackageManager pm = ctx.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(
                    "com.qiyi.video", 0);
            PackageInfo packageInfo = pm.getPackageArchiveInfo(
                    applicationInfo.publicSourceDir, 0);
            if (packageInfo != null) {
                versionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "versionCode:" + versionCode);
        if (versionCode == 80730) { // v7.3
            Intent intent = new Intent();
            intent.setData(Uri.parse(appDetail));
            intent.setAction("android.intent.action.qiyivideo.player");
            intent.setPackage("com.qiyi.video");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ctx.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.i(TAG, "Exception:" + e.toString());
                startSingleGame(ctx, "com.qiyi.video");
            }
        } else if (versionCode >= 80770) { // v7.7
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(appDetail));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                ctx.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.i(TAG, "Exception2:" + e.toString());
                startSingleGame(ctx, "com.qiyi.video");
            }
        }
    }

    private static void startSingleGame(Context ctx, String packageName) {
        Intent intent = ctx.getPackageManager().getLaunchIntentForPackage(
                packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            ctx.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public static void gotoAppCenter(Context ctx, String appid) {
        Toast.makeText(ctx, "未检测到应用，请先下载安装", Toast.LENGTH_SHORT).show();
        try {
            Intent intent = new Intent();
            intent.setClassName("com.prize.appcenter",
                    "com.prize.appcenter.activity.AppDetailActivity");
            Bundle bundle = new Bundle();
            bundle.putParcelable("AppsItemBean", null);
            bundle.putString("appid", appid);
            intent.putExtra("bundle", bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
