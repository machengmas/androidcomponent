package com.macheng.component.appupgrade;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;

import com.macheng.component.appupgrade.internal.service.JobServiceImp;
import com.macheng.component.appupgrade.internal.service.UpgradeService;

/**
 * App升级处理器
 * @author macheng
 * @name Component
 * @datetime 2017-12-26 16:47
 */
public class AppUpgradeExecutor {
    private Activity activity;
    private Context context;
    private WrappedUpgradeCallback wrappedUpgradeCallback;

    private UpgradeServiceConnection conn;
    private boolean connected = false;
    private Intent upgradIntent;

    public AppUpgradeExecutor(Activity activity, UpgradeCallback upgradeCallback) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.wrappedUpgradeCallback = new WrappedUpgradeCallback(upgradeCallback);
    }
    
    public void executor(String checkUpgradeUrl) {
        startUpgradeService(checkUpgradeUrl);
        //Android5.0以上支持后台下载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startDownloadJobService(checkUpgradeUrl);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startDownloadJobService(String checkUpgradeUrl) {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString("checkUpgradeUrl", checkUpgradeUrl);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), JobServiceImp.class.getName()))
                .setPeriodic(1000 * 60 * 60 * 6)//每6个小时检查一次更新
                .setExtras(persistableBundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    private void startUpgradeService(String checkUpgradeUrl) {
        upgradIntent = new Intent(context, UpgradeService.class);
        upgradIntent.putExtra("checkUpgradeUrl", checkUpgradeUrl);
        context.startService(upgradIntent);
        conn = new UpgradeServiceConnection();
        context.bindService(upgradIntent, conn,
                Context.BIND_AUTO_CREATE);
    }

    private class UpgradeServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpgradeService updateService = ((UpgradeService.UpgradeAppBinder) service)
                    .getService();
            updateService.setUpgradeCallback(wrappedUpgradeCallback);
            updateService.setActivity(activity);
            connected = true;
        }
    }

    /**
     * 采用装饰设计模式对客户端回调接口做一层封装
     * 采用装饰设计模式而不采用继承的原因：保证客户端程序员回调时不用调用UpgradeCallback父类对象的super方法，
     * 因为你不能保证客户端程序员调用super方法的顺序，这样做客户端程序员可以不用调用父类对象的super方法，减少了客户端程序员的出错可能性
     */
    private class WrappedUpgradeCallback implements UpgradeCallback {
        UpgradeCallback upgradeCallback;

        public WrappedUpgradeCallback(UpgradeCallback upgradeCallback) {
            this.upgradeCallback = upgradeCallback;
        }

        @Override
        public void afterUpgrade(boolean success) {
            if (null != context && null != conn && connected) {
                context.stopService(upgradIntent);
                context.unbindService(conn);
            }
            if (null != upgradeCallback) {
                upgradeCallback.afterUpgrade(success);
            }
        }

        @Override
        public void onProgressUpgrade(int progressPercentage) {
            if (null != upgradeCallback) {
                upgradeCallback.onProgressUpgrade(progressPercentage);
            }
        }

        @Override
        public void cancel() {
            upgradeCallback.cancel();
        }

    }
}
