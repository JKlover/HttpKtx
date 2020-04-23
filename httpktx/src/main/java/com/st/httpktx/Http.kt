package com.st.httpktx

import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.st.httpktx.utils.StreamTools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * code bt St on 2020/4/13
 * 该请求类的设计Java无法调用只提供给kotlin调用
 * 网络请求使用okHttp和RxJava进行简单的封装这里的可以换更加成熟的方案
 * 为了解决内存泄漏只能给OkHttp设置tag，ViewModel销毁时候取消相应的tag让RxJava的线程制自动释放
 */
inline fun <reified T> httpJsonEngine(http: BaseRequest<T>.() -> Unit) {
    var httpCore = BaseRequest<T>()
    http.invoke(httpCore)
    val jsonRequestBase = Http.postRequestBase?.run {
        JSONObject.toJSON(this) as JSONObject
    }
    httpCore.mParams?.also {
        if (it.isNotEmpty()) {
            val jsonRequest = JSONObject.toJSON(it) as JSONObject
            jsonRequestBase!!.putAll(jsonRequest)
            httpCore.method = "post"
        }
    }

    httpCore.postRequest?.also {
        val jsonRequest = JSONObject.toJSON(it) as JSONObject
        jsonRequestBase!!.putAll(jsonRequest)
        httpCore.method = "post"
    }
    jsonRequestBase?.also {
        httpCore.body =
            it.toJSONString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
    GlobalScope.asyncWithLifecycleAsync(MyAcManager.getInstance().currentActivity, Dispatchers.Main) {
        httpCore.mStart()
        try {
            var response = responseHandler(httpCore)
            if (response.code == 200) {
                var body = response.body!!.string()
                //https的请求的话是正常的,http的请求直接报要在主线程的异常,wf,所以在io解析Json
                var jsonDataBody = withContext(Dispatchers.IO) {
                    try {
                        JSON.parseObject(body, T::class.java)
                    } catch (e: Exception) {
                        return@withContext  "解析异常" as T
                    }
                }
                httpCore.mSuccess(jsonDataBody)
            } else {//抓取异常的接口
                httpCore.mFail("接口异常")
            }
        } catch (e: Exception) {
            httpCore.mFail("异常")
        }
    }
}

/**
 * 用于请求网页的
 */
fun httpHtmlEngine(http: HttpString.() -> Unit) {
    var httpCore = HttpString()
    http.invoke(httpCore)
    httpCore.mParams?.let {
        httpCore.method="post"
    }
    httpCore.body= setRequestBody(httpCore.mParams,httpCore.enc)
    Log.e("ST--->编码",httpCore.enc)
    GlobalScope.asyncWithLifecycleAsync(MyAcManager.getInstance().currentActivity, Dispatchers.Main) {
        httpCore.mStart()
        try {
            var response = responseHandler(httpCore)
            if (response.code == 200) {
                //https的请求的话是正常的,http的请求直接报要在主线程的异常,wf,所以在io解析Json
                var body = withContext(Dispatchers.IO) {
                    var b=response.body!!.byteStream()
                    return@withContext  StreamTools.getInstance().getInputStreamToStringWithEnc(b,httpCore.enc)
                }
//                Log.e("ST--->","我还存在")
//                delay(8000)
                httpCore.mSuccess(body)
            } else {//抓取异常的接口
                httpCore.mFail("接口异常")
            }

        } catch (e: Exception) {
            httpCore.mFail("异常")
        }
    }
}
