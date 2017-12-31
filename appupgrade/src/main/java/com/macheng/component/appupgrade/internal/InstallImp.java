package com.macheng.component.appupgrade.internal;

import android.content.Context;

import com.macheng.component.appupgrade.utils.ApkUtils;

import java.io.File;

/**
 * @author macheng
 * @name Component
 * @datetime 2017-12-27 16:47
 */
public class InstallImp implements Install {
    private Context context;

    public InstallImp(Context context) {
        this.context = context;
    }

    @Override
    public void install(String localpath) {
        //apk是否存在
        File file = new File(localpath);
        if (!file.exists()) {
            return;
        }
        //验证apk和当前apk的包名是否一致
        String currentPackageName = ApkUtils.getAppPackageName(context);
        String downloadPackageName = ApkUtils.getAppPackageName(context, localpath);
        if (!currentPackageName.equals(downloadPackageName)) {
            return;
        }
        //验证版本号是否大于当前应用
        int currentVersionCode = ApkUtils.getAPPVersionCode(context);
        int downloadVersionCode = ApkUtils.getApkVersionCode(context, localpath);
        if (currentVersionCode >= downloadVersionCode) {
            return;
        }
        //启动安装程序安装Apk
        ApkUtils.installApk(context, file);
    }

}
