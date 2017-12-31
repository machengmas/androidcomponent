package com.macheng.component.appupgrade.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.macheng.component.appupgrade.internal.common.OkHttpManager;
import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;
import com.macheng.component.appupgrade.utils.ApkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author macheng
 * @name Component
 * @datetime 2017-12-26 16:40
 */
public class UpgradeImp implements Upgrade {
    private static final int CODE_SUCCESS = 1;
    private static final int CODE_FAILURE = 2;
    private Context context;
    private Download download;
    private Install install;
    private AppUpgradeInfo appUpgradeInfo;
    private OnUpgradeListener onUpgradeListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == OkHttpManager.REQUEST_SUCCESS) {
                checkUpdate((String) msg.obj);
            } else {
                if (onUpgradeListener != null) {
                    onUpgradeListener.afterUpgrade(false);
                }
            }
        }
    };

    public UpgradeImp(Context context) {
        this.context = context;
        this.download = new DownloadImp(context, true);
        this.install = new InstallImp(context);
    }

    @Override
    public void checkUpdate(String checkUpgradUrl, final OnUpgradeListener onUpgradeListener) {
        setOnUpgradeListener(onUpgradeListener);
        Map<String, String> checkUpdateParams = new HashMap<String, String>();
        checkUpdateParams.put("versionCode", String.valueOf(ApkUtils.getAPPVersionCode(context)));
        checkUpdateParams.put("appKey", ApkUtils.getAppPackageName(context));
        new OkHttpManager().request(checkUpgradUrl, checkUpdateParams, mHandler);
    }

    @Override
    public void update(AppUpgradeInfo appUpgradeInfo, final OnUpgradeListener onUpgradeListener) {
        download.download(appUpgradeInfo, new Download.OnDownloadListener() {
            @Override
            public void afterDownload(boolean isSuccess, String locaSavePath) {
                if (isSuccess) {
                    install.install(locaSavePath);
                } else {
                    onUpgradeListener.afterUpgrade(false);
                }
            }

            @Override
            public void onProgressDownload(int progressPercentage) {
                onUpgradeListener.onProgressUpgrade(progressPercentage);
            }
        });
    }

    private void checkUpdate(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        if (String.valueOf(CODE_SUCCESS).equals(jsonObject.getString("code"))) {
            appUpgradeInfo = getAppUpgradeInfo(jsonObject.getString("data"));
            if (appUpgradeInfo.getLatestVersionCode() > ApkUtils.getAPPVersionCode(context)) {
                if (onUpgradeListener != null) {
                    onUpgradeListener.afterCheckUpdate(true, appUpgradeInfo);
                }
            } else {
                if (onUpgradeListener != null) {
                    onUpgradeListener.afterCheckUpdate(false, appUpgradeInfo);
                }
            }
        } else {
            if (onUpgradeListener != null) {
                onUpgradeListener.afterUpgrade(false);
            }

        }
    }

    private AppUpgradeInfo getAppUpgradeInfo(String data) {
        JSONObject jsonObject = JSON.parseObject(data);
        AppUpgradeInfo appUpgradeInfo = JSON.toJavaObject(jsonObject,
                AppUpgradeInfo.class);
        return appUpgradeInfo;
    }

    private void setOnUpgradeListener(OnUpgradeListener onUpgradeListener) {
        this.onUpgradeListener = onUpgradeListener;
    }


}
