package com.macheng.component.appupgrade.internal;

/**
 * apk安装接口
 *
 * @author macheng
 * @name Component
 * @datetime 2017-12-27 16:47
 */
public interface Install {
    /**
     * apk安装
     *
     * @param localSavePath 需要安装的apk文件的本地路径
     */
    public void install(String localSavePath);
}
