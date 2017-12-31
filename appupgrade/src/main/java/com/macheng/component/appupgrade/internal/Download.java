package com.macheng.component.appupgrade.internal;

import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;

/**
 * apk下载接口
 * @author macheng
 * @name Component
 * @datetime 2017-12-27 16:24
 */
public interface Download {
    /**
     * 下载
     * @param appUpgradeInfo 服务器中的app的信息
     * @param onDownloadListener
     */
    public void download(AppUpgradeInfo appUpgradeInfo, OnDownloadListener onDownloadListener);
    /**
     * apk下载监听回调
     */
    public interface OnDownloadListener{
        void afterDownload(boolean isSuccess,String locaSavePath);
        void onProgressDownload(int progressPercentage);
    }

}
