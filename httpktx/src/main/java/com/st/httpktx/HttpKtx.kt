package com.st.httpktx

import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.st.httpktx.utils.StreamTools
import com.st.httpktx.utils.trueLet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * code bt St on 2020/4/13
 * 该请求类的设计Java无法调用只提供给kotlin调用
 * 网络请求使用okHttp和RxJava进行简单的封装这里的可以换更加成熟的方案
 * 为了解决内存泄漏只能给OkHttp设置tag，Activity销毁时候取消相应的tag让RxJava的线程或者协程自动释放
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
        (it.size>0)trueLet{
            Log.e("Json的大小", it.size.toString())
            httpCore.body =
                it.toJSONString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }elseLet {
            httpCore.method = "get"
        }

    }
    GlobalScope.asyncWithLifecycleAsync(
        MyAcManager.getInstance().currentActivity,
        Dispatchers.Main
    ) {
        httpCore.mStart()
        try {
            var response = responseHandler(httpCore)
            (response.code==200)trueLet {
                var body = response.body!!.string()
                //https的请求的话是正常的,http的请求直接报要在主线程的异常,wf,所以在io解析Json
                var jsonDataBody = withContext(Dispatchers.IO) {
                    try {
                        JSON.parseObject(body, T::class.java)
                    } catch (e: Exception) {
                        return@withContext "解析异常" as T
                    }
                }
                httpCore.mSuccess(jsonDataBody)
            }elseLet {
                httpCore.mFail("接口异常")
            }
        } catch (e: Exception) {
            Log.e("ST--->请求发生异常退出",e.toString())
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
    (httpCore.mParams.isEmpty())trueLet {
        (httpCore.method.isNullOrEmpty())trueLet {
            httpCore.method="post"
        }
    }
    httpCore.body = setRequestBody(httpCore.mParams, httpCore.enc)
    GlobalScope.asyncWithLifecycleAsync(
        MyAcManager.getInstance().currentActivity,
        Dispatchers.Main
    ) {
        httpCore.mStart()
        try {
            var response = responseHandler(httpCore)
            (response.code==200)trueLet {
                var body = withContext(Dispatchers.IO) {
                    var b = response.body!!.byteStream()
                    return@withContext StreamTools.getInstance()
                        .getInputStreamToStringWithEnc(b, httpCore.enc)
                }
                httpCore.mSuccess(body)
            }elseLet {
                httpCore.mFail("接口异常")
            }
        } catch (e: Exception) {
            httpCore.mFail("异常")
        }
    }
}

suspend fun httpX(http: HttpString.() -> Unit)= withContext(Dispatchers.IO){
    var httpCore = HttpString()
    http.invoke(httpCore)
    var content = StringBuilder()
    var response = onJsonAsyncExecute(httpCore)
    (response.code==200)trueLet {
        content = withContext(Dispatchers.IO) {
            var b = response.body!!.byteStream()
            return@withContext content.append(StreamTools.getInstance().getInputStreamToStringWithEnc(b, httpCore.enc)).append("\n")
        }
//        httpCore.mSuccess(body)
    }elseLet {
        httpCore.mFail("接口异常")
    }
    return@withContext content
}
