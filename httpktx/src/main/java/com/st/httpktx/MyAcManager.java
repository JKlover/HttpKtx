package com.st.httpktx;


import android.app.Activity;
import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;

/**
 * code bt St on 2020/3/12
 */

public class MyAcManager {
    private static MyAcManager sInstance = new MyAcManager();
    private WeakReference<FragmentActivity> sCurrentActivityWeakRef;
    private MyAcManager() {
    }
    public static MyAcManager getInstance() {
        return sInstance;
    }
    public FragmentActivity getCurrentActivity() {
        FragmentActivity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<>((FragmentActivity) activity);
    }

}
