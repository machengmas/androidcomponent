package com.macheng.component.appupgrade.internal;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;
import com.macheng.component.appupgrade.utils.ApkUtils;
import com.macheng.component.appupgrade.internal.view.DownloadNotification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author macheng
 * @name Component
 * @datetime 2017-12-28 14:00
 */
public class DownloadImp implements Download{
    private static String LOCALSAVEDIR=Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOWNLOADS;
    private static int DOWNLOAD_SUCCESS=1;
    private static int DOWNLOAD_FAILED=2;
    private Context context;
    private DownloadNotification downloadNotification;
    private String localSavePath;
    private boolean showNotification;
    private OnDownloadListener onDownloadListener;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==DOWNLOAD_SUCCESS){
                if(onDownloadListener!=null){
                    onDownloadListener.afterDownload(true,localSavePath);
                }
            }else{
                if(onDownloadListener!=null) {
                    onDownloadListener.afterDownload(false,localSavePath);
                }
            }
        }
    };

    public DownloadImp(Context context) {
        this.context = context;
        this.downloadNotification = new DownloadNotification(context);
        this.localSavePath = LOCALSAVEDIR+"/"+ApkUtils.getAppLabel(context)+".apk";
        this.showNotification=false;
    }

    public DownloadImp(Context context, boolean showNotification) {
        this.context = context;
        this.downloadNotification = new DownloadNotification(context);
        this.localSavePath =  LOCALSAVEDIR+"/"+ApkUtils.getAppLabel(context)+".apk";;
        this.showNotification=showNotification;
    }


    @Override
    public void download(AppUpgradeInfo appUpgradeInfo,OnDownloadListener onDownloadListener) {
        setOnDownloadListener(onDownloadListener);
        File dir=new File(LOCALSAVEDIR);
        if(!dir.exists()){
            dir.mkdir();
        }
        File file=new File(localSavePath);
        if(file.exists()){
            int localApkVersionCode=ApkUtils.getApkVersionCode(context,localSavePath);
            int serverApkVersionCode=appUpgradeInfo.getLatestVersionCode();
            if(serverApkVersionCode>localApkVersionCode){
                downloadApk(appUpgradeInfo.getDownloadUrl(),onDownloadListener);
            }else{
                if(onDownloadListener!=null){
                    onDownloadListener.afterDownload(true,localSavePath);
                }
            }
        }else{
            downloadApk(appUpgradeInfo.getDownloadUrl(),onDownloadListener);
        }
    }

    /**
     * 设置下载时是否显示系统通知栏通知
     * @param showNotification
     */
    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    private void downloadApk(String downloadUrl, final OnDownloadListener onDownloadListener){
        Request request=new Request.Builder().url(downloadUrl).build();
        final Message message=Message.obtain();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                message.what=DOWNLOAD_FAILED;
                mHandler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int downPercent = 0;
                InputStream is =null;
                byte[] buf=new byte[1024];
                int len=0;
                FileOutputStream fos=null;
                try{
                    is=response.body().byteStream();
                    long total=response.body().contentLength();
                    File file=new File(localSavePath);
                    fos=new FileOutputStream(file);
                    long sum=0;
                    while((len= is.read(buf))!=-1){
                        fos.write(buf,0,len);
                        sum+=len;
                        int progress= (int) (sum*1.0f/total*100);
                        if(onDownloadListener!=null){
                            onDownloadListener.onProgressDownload(progress);
                        }
                        if(showNotification){
                            if ((downPercent == 0) || progress - 10 > downPercent||progress==100) {
                                downPercent += 10;
                                downloadNotification.CreateNotification(android.R.mipmap.sym_def_app_icon, ApkUtils.getAppLabel(context)+"正在下载",progress+"%");
                            }
                        }

                    }
                    fos.flush();
                    message.what=DOWNLOAD_SUCCESS;
                    mHandler.sendMessage(message);
                }catch (Exception e){
                    message.what=DOWNLOAD_FAILED;
                    mHandler.sendMessage(message);
                }finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {

                    }
                }

            }
        });
    }
    private void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }
}
