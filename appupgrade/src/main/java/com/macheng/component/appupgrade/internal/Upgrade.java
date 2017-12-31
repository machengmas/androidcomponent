package com.macheng.component.appupgrade.internal;

import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;

/**
 * app升级接口，升级包括检查更新，更新apk
 *
 * @author macheng
 * @name Component
 * @datetime 2017-12-26 16:22
 */
public interface Upgrade {
    /**
     * 检查是否app是否有更新
     *
     * @param checkUpgradeUrl
     * @param onUpgradeListener
     */
    void checkUpdate(String checkUpgradeUrl, OnUpgradeListener onUpgradeListener);

    /**
     * 更新app，下载+安装
     *
     * @param appUpgradeInfo    服务器中的app信息
     * @param onUpgradeListener
     */
    void update(AppUpgradeInfo appUpgradeInfo, OnUpgradeListener onUpgradeListener);

    /**
     * app升级回调
     */
    interface OnUpgradeListener {
        void afterCheckUpdate(boolean hasNewVersion, AppUpgradeInfo appUpgradeInfo);

        void afterUpgrade(boolean success);

        void onProgressUpgrade(int progressPercentage);
    }
}
