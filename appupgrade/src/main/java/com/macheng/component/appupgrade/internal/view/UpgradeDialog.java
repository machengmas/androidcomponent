package com.macheng.component.appupgrade.internal.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.macheng.component.appupgrade.R;
import com.macheng.component.appupgrade.internal.model.AppUpgradeInfo;
import com.macheng.component.appupgrade.utils.ApkUtils;

/**
 * 自定义升级对话框
 * @author macheng
 * @name Component
 * @datetime 2017-12-26 16:53
 */
public class UpgradeDialog extends Dialog {
    private Activity context;
    private AppUpgradeInfo appUpgradeInfo;
    private OnUpgradeDialogClickListener onUpgradeDialogClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade);
        setDefaultDialogSize();
        showUpgradeInfo();
        setOnClickListener();
        setCancelable(false);
    }

    private void setDefaultDialogSize() {
        Window win = getWindow();
        win.setGravity(Gravity.CENTER);
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*8/9; // 设置dialog宽度为屏幕的4/5
        lp.height = display.getHeight()*5/13; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        win.setAttributes(lp);

    }

    private void showUpgradeInfo() {
        ((TextView) findViewById(R.id.tvcurrentversionname)).setText("当前版本：V"+ApkUtils.getAPPVersionName(context));
        ((TextView) findViewById(R.id.tvnewversionname)).setText("新版本：V"+appUpgradeInfo.getLatestVersionName());
        ((TextView) findViewById(R.id.tvapksize)).setText("安装包大小:"+ApkUtils.getApkSize(appUpgradeInfo.getSize()));
    }
    private void setOnClickListener() {
        findViewById(R.id.tvupgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onUpgradeDialogClickListener!=null){
                    onUpgradeDialogClickListener.clickUpgrade();
                }

            }
        });
        findViewById(R.id.tvcancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onUpgradeDialogClickListener!=null){
                    onUpgradeDialogClickListener.clickcancel();
                }

            }
        });
    }
    public UpgradeDialog(@NonNull Activity context,AppUpgradeInfo appUpgradeInfo) {
        super(context);
        this.context = context;
        this.appUpgradeInfo = appUpgradeInfo;
    }
    public void setOnUpgradeDialogClickListener(OnUpgradeDialogClickListener onUpgradeDialogClickListener) {
        this.onUpgradeDialogClickListener = onUpgradeDialogClickListener;
    }

    /**
     * 升级对话框按钮监听回调
     */
    public interface OnUpgradeDialogClickListener{
        void clickUpgrade();
        void clickcancel();
    }


}
