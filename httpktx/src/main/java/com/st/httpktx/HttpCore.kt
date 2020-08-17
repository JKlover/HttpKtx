package com.st.httpktx

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.*
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * <p/>
 * Code by ST on 2020-02-22
 */
/**
 * 用于网页请求用的
 */
class HttpString : BaseRequest<String>() {
    var enc: String = "utf-8"//默认是utf-8
}

object Http {
    var client: OkHttpClient? = null
    var postRequestBase: Any? = null
    var context: Context? = null
    fun init(requestConfig: RequestConfig) {
        context = requestConfig.context
        client = OkHttpClient.Builder()
            .addInterceptor(HttpLogInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .sslSocketFactory(NetUtils.createSSLSocketFactory(), NetUtils.TrustAllManager())
            .hostnameVerifier(NetUtils.TrustAllHostnameVerifier())
            .build()
        postRequestBase = requestConfig.postRequestBase
    }

    fun getOkHttpclient(): OkHttpClient {
        return client!!
    }

    fun cancelHttpJop(any: Any) {
        for (call in getOkHttpclient()!!.dispatcher.queuedCalls()) {
            if (any == call.request().tag()) {
                call.cancel()
            }

        }
        for (call in getOkHttpclient()!!.dispatcher.runningCalls()) {
            if (any == call.request().tag()) {
                call.cancel()
            }
        }
    }

}

/**
 * 挂起函数
 */
suspend fun responseHandler(http: BaseRequest<*>) = withContext(Dispatchers.IO) {
    Log.e("ST线程名字--->", Thread.currentThread().name)
    onJsonAsyncExecute(http)//异步OkHttp请求,非阻塞感觉速度慢
//    onJsonExecute(http)!!//同步阻塞感觉速度快很多
}

/**
 * 同步的方式
 */
fun onJsonExecute(wrap: BaseRequest<*>): Response? {
    var resp = Http.getOkHttpclient().newCall(request(wrap)!!).execute()
    return resp
}

/**
 * 异步的方式
 */
suspend fun onJsonAsyncExecute(wrap: BaseRequest<*>): Response {
    var resp = Http.getOkHttpclient().newCall(request(wrap)!!).await()
    return resp
}

private fun request(wrap: BaseRequest<*>): Request? {
    var req: Request? = null
    Log.e("ST--->post",wrap.method)
    when (wrap.method) {
        "get", "Get", "GET" -> req =
            Request.Builder().url(wrap.url.toString()).headers(setRequestHeaders(wrap.mHeaders))
                .tag(MyAcManager.getInstance().currentActivity.javaClass.name).build()
        "post", "Post", "POST" -> req =
            Request.Builder().url(wrap.url.toString()).headers(setRequestHeaders(wrap.mHeaders))
        .post(wrap.body!!).tag(MyAcManager.getInstance().currentActivity.javaClass.name).build()
        "put", "Put", "PUT" -> req = Request.Builder().url(wrap.url.toString()).put(wrap.body!!)
            .headers(setRequestHeaders(wrap.mHeaders)).tag(MyAcManager.getInstance().currentActivity.javaClass.name).build()
        "delete", "Delete", "DELETE" -> req =
            Request.Builder().url(wrap.url.toString()).headers(setRequestHeaders(wrap.mHeaders))
                .delete(wrap.body).tag(MyAcManager.getInstance().currentActivity.javaClass.name).build()
        else->Request.Builder().url(wrap.url.toString()).headers(setRequestHeaders(wrap.mHeaders))
            .tag(MyAcManager.getInstance().currentActivity.javaClass.name).build()
    }
    return req
}

suspend fun Call.await(): Response = suspendCoroutine { block ->
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: java.io.IOException) {
            Log.e("ST--->底部请求异常", e.message)
            block.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            Log.e("ST--->底部请求成功", response.code.toString())
            if (response.isSuccessful) {
                block.resume(response)
            }
        }
    })
}

class RequestPairs {
    var pairs: MutableMap<String, String> = HashMap()
    operator fun String.minus(value: String) {
        pairs[this] = value
    }
}

/**
 * post的请求参数，构造RequestHeaders
 * @param headParams
 * @return
 */
fun setRequestHeaders(headParams: Map<String, String>): Headers {
    var headers = Headers.Builder()
    if (headParams != null) {
        val iterator = headParams.keys.iterator()
        var key = ""
        while (iterator.hasNext()) {
            key = iterator.next()
            headers.add(key, headParams.getValue(key))
//            Log.e("post http", "post_Params===" + key + "====" + headParams[key])
        }
    }
    return headers.build()
}

/**
 * post的请求参数，构造RequestBody
 * @param BodyParams
 * @return
 */
fun setRequestBody(bodyParams: Map<String, String>,enc:String): RequestBody {
    var body: RequestBody
    var formEncodingBuilder = FormBody.Builder()
    if (bodyParams != null) {
        val iterator = bodyParams.keys.iterator()
        var key = ""
        while (iterator.hasNext()) {
            key = iterator.next()
            formEncodingBuilder.addEncoded(key,URLEncoder.encode(bodyParams.getValue(key),enc) )
        }
    }
    body = formEncodingBuilder.build()
    return body
}
