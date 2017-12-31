package com.macheng.component.appupgrade;

/**
 * App升级客户端回调接口
 * @author macheng
 * @name Component
 * @datetime 2017-12-29 13:45
 */
public interface UpgradeCallback {
    /**
     * 主线程中执行
     * @param success 没有新版本时success为true，检查是否有更新、下载apk等步骤中任何一处失败返回false
     */
    void afterUpgrade(boolean success);

    /**
     * 下载apk的回调，子线程中执行
     * @param progressPercentage 下载apk的进度
     */
    void onProgressUpgrade(int progressPercentage);

    /**
     * 点击升级对话框中取消按钮后的回调，主线程中执行
     */
    void cancel();
}
