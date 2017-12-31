package com.macheng.component.appupgrade.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * @author macheng
 * @name Component
 * @datetime 2017-12-26 18:01
 */
public class ApkUtils {
    /**
     * 获取当前app的版本号 currentVersionCode
     * @param context
     * @return
     */
    public static int getAPPVersionCode(Context context) {
        int currentVersionCode = 0;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }

    /**
     * 获取当前app的版本名称
     *
     * @param context
     * @return
     */
    public static String getAPPVersionName(Context context) {
        String appVersionName = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }

    /**
     * 获取当前app的包名
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        String packageName = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            packageName = info.packageName; 
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取指定apk文件的包名
     * @param context
     * @param path
     * @return
     */
    public static String getAppPackageName(Context context, String path) {
        String packageName = "";
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            packageName = applicationInfo.packageName;
        }
        return packageName;
    }

    /**
     * 获取指定apk文件的版本号
     *
     * @param context
     * @param path
     * @return
     */
    public static int getApkVersionCode(Context context, String path) {
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            versionCode = packageInfo.versionCode;
        }
        return versionCode;
    }


    /**
     * 获取当前app的应用名称
     * @param context
     * @return
     */
    public static String getAppLabel(Context context) {
        PackageManager manager = context.getPackageManager();
        String appLabel = (String) manager.getApplicationLabel(context.getApplicationInfo());
        return appLabel;
    }

    /**
     * 安装指定的apk
     * @param context
     * @param file
     */
    public static void installApk(Context context,File file){
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    /**
     * * 获取apk大小
     * @return 单位M
     */
    public static String getApkSize(int size){
        int apkSize=size/(1024*1024);
        return apkSize+"M";
    }
}
