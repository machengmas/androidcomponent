package com.macheng.component.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.macheng.component.appupgrade.AppUpgradeExecutor;
import com.macheng.component.appupgrade.UpgradeCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appupgradeDemo();
    }

    private void appupgradeDemo(){
        String checkUpgradeUrl="http://172.16.33.241:18080/app/appUpgrade.do?action=getLatestAppVersion";
        new AppUpgradeExecutor(this, new UpgradeCallback() {
            @Override
            public void afterUpgrade(boolean success) {
                if(success){
                    ((TextView)MainActivity.this.findViewById(R.id.tvappupgrade)).setText("afterUpgrade+success");
//                    MainActivity.this.finish();
                }else {
                    ((TextView)MainActivity.this.findViewById(R.id.tvappupgrade)).setText("afterUpgrade+failed");
                }
            }

            @Override
            public void onProgressUpgrade(int progressPercentage) {

            }

            @Override
            public void cancel() {
                ((TextView)MainActivity.this.findViewById(R.id.tvappupgrade)).setText("cancel");
//                MainActivity.this.finish();
            }
        }).executor(checkUpgradeUrl);
    }
}
