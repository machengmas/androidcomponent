package com.macheng.component.appupgrade.internal.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.macheng.component.appupgrade.internal.DownloadImp;
import com.macheng.component.appupgrade.internal.Upgrade;
import com.macheng.component.appupgrade.internal.UpgradeImp;
import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;

/**
 * 定时任务服务，定时去检查app是否有更新，有更新的话将新版本的apk预先下载到本地
 * @author macheng
 * @name Component
 * @datetime 2017-12-28 17:24
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobServiceImp extends JobService {
    private Upgrade upgrade;

    @Override
    public void onCreate() {
        upgrade = new UpgradeImp(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        upgrade.checkUpdate(params.getExtras().getString("checkUpgradeUrl"), new Upgrade.OnUpgradeListener() {
            @Override
            public void afterCheckUpdate(boolean hasNewVersion, AppUpgradeInfo appUpgradeInfo) {
                if (hasNewVersion) {
                    new DownloadImp(JobServiceImp.this).download(appUpgradeInfo, null);
                }
            }

            @Override
            public void afterUpgrade(boolean success) {

            }

            @Override
            public void onProgressUpgrade(int progressPercentage) {

            }

        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


}
