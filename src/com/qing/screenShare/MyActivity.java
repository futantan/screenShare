package com.qing.screenShare;

import android.app.Activity;
import android.os.Bundle;
import util.SystemManager;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String apkRoot = "chmod 777 " + getPackageCodePath();

    }

    public void getRoot() {
        String apkRoot = "chmod 777 " + getPackageCodePath();
        SystemManager.RootCommand(apkRoot);
    }
}
