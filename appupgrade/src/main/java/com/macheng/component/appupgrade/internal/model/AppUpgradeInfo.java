package com.macheng.component.appupgrade.internal.model;

/**
 * app升级实体，储存app升级所需要的信息
 *
 * @author macheng
 * @name Component
 * @datetime 2017-12-26 16:28
 */
public class AppUpgradeInfo {

    /**
     * 最新版本号
     */
    private int latestVersionCode;

    /**
     * 最新版本名称
     */
    private String latestVersionName;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 系统最低版本要求
     */
    private String minOSVersion;

    /**
     * 应用大小
     */
    private int size;

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public void setLatestVersionCode(int latestVersionCode) {
        this.latestVersionCode = latestVersionCode;
    }

    public String getLatestVersionName() {
        return latestVersionName;
    }

    public void setLatestVersionName(String latestVersionName) {
        this.latestVersionName = latestVersionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }


    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


    public String getMinOSVersion() {
        return minOSVersion;
    }


    public void setMinOSVersion(String minOSVersion) {
        this.minOSVersion = minOSVersion;
    }


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }

}
