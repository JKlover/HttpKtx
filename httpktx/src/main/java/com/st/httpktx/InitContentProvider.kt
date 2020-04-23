package com.st.httpktx

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity

/**
 * code bt St on 2020/4/8
 */
internal class InitContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        val application=context!!.applicationContext as Application
        NetAppWatcher.init(application)
        Log.e("ST--->当前初始化","初始化开始")
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}

/**
 * 初始化Application存下当前Activity的弱引用
 * Activity必须是ComponentActivity的子类才可以回调LifecycleOwner
 */
internal object NetAppWatcher {
    fun init(application: Application) {
        Http.init(
            RequestConfig.newBuilder(application)
                .build()
        )
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {

            }
            override fun onActivityDestroyed(activity: Activity) {
                //取消在该Activity所有的OkHttp内部的线程请求
                Http.cancelHttpJop(activity.javaClass.name)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is FragmentActivity){
                    MyAcManager.getInstance().setCurrentActivity(activity)
                }else{
                }
            }

            override fun onActivityResumed(activity: Activity) {
                if (activity is FragmentActivity){
                    MyAcManager.getInstance().setCurrentActivity(activity)
                }else{
                }
            }
        })

    }

}