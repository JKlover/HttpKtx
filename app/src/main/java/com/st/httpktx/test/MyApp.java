package com.st.httpktx.test;

import android.app.Application;
import android.util.Log;

/**
 * author ST 2020/5/1
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("ST--->MyApp","初始化");
    }
}
