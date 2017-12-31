package com.macheng.component.appupgrade.internal.view;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * 创建系统通知栏的通知，apk下载时，显示系统通知，并显示下载进度
 * @author macheng
 * @name Component
 * @datetime 2017-12-27 13:49
 */
public class DownloadNotification {
    private Context context;
    private NotificationManager mNotificationManager;
    public DownloadNotification(Context context) {
        this.context = context;
    }

    /**
     * 创建下载通知
     * @param iconId
     * @param contentTitle
     * @param contentText
     */
    public void CreateNotification(int iconId, String contentTitle, String contentText) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis());
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
