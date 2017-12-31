package com.macheng.component.appupgrade.internal.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.macheng.component.appupgrade.UpgradeCallback;
import com.macheng.component.appupgrade.internal.Upgrade;
import com.macheng.component.appupgrade.internal.UpgradeImp;
import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;
import com.macheng.component.appupgrade.internal.view.UpgradeDialog;

/**
 * app升级服务，检查app是否有更新，有更新弹出升级对话框，让用户选择升级/取消
 * @author macheng
 * @name Component
 * @datetime 2017-12-28 15:57
 */
public class UpgradeService extends Service {
    private final UpgradeAppBinder iBinder = new UpgradeAppBinder();
    private Upgrade upgrade;
    private UpgradeCallback upgradeCallback;
    private Activity activity;

    private void showUpgradeDialog(final AppUpgradeInfo appUpgradeInfo) {
        final UpgradeDialog upgradeDialog = new UpgradeDialog(activity, appUpgradeInfo);
        upgradeDialog.show();
        upgradeDialog.setOnUpgradeDialogClickListener(new UpgradeDialog.OnUpgradeDialogClickListener() {
            @Override
            public void clickUpgrade() {
                upgradeDialog.dismiss();
                upgrade.update(appUpgradeInfo, new Upgrade.OnUpgradeListener() {
                    @Override
                    public void afterCheckUpdate(boolean hasNewVersion, AppUpgradeInfo appUpgradeInfo) {
                    }

                    @Override
                    public void afterUpgrade(boolean success) {
                        upgradeCallback.afterUpgrade(success);
                    }

                    @Override
                    public void onProgressUpgrade(int progressPercentage) {
                        upgradeCallback.onProgressUpgrade(progressPercentage);
                    }
                });
            }

            @Override
            public void clickcancel() {
                upgradeDialog.dismiss();
                upgradeCallback.cancel();
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        upgrade = new UpgradeImp(this);
        super.onCreate();
        this.getBaseContext();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String checkUpgradeUrl = intent.getStringExtra("checkUpgradeUrl");
        upgrade.checkUpdate(checkUpgradeUrl, new Upgrade.OnUpgradeListener() {
            @Override
            public void afterCheckUpdate(boolean hasNewVersion, final AppUpgradeInfo appUpgradeInfo) {
                if (hasNewVersion) {
                    showUpgradeDialog(appUpgradeInfo);
                } else {
                    upgradeCallback.afterUpgrade(true);
                }
            }

            @Override
            public void afterUpgrade(boolean success) {
                upgradeCallback.afterUpgrade(success);
            }

            @Override
            public void onProgressUpgrade(int progressPercentage) {
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    public void setUpgradeCallback(UpgradeCallback upgradeCallback) {
        this.upgradeCallback = upgradeCallback;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public class UpgradeAppBinder extends Binder {
        public UpgradeService getService() {
            return UpgradeService.this;
        }
    }


}
