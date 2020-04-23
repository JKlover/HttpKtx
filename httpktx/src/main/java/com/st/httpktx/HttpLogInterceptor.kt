package com.st.httpktx

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 * code bt St on 2020/4/11
 */
class HttpLogInterceptor : Interceptor {
    private val TAG = "HttpKtx"
    private val Enc = Charset.forName("utf-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        var body: String? = null
        requestBody?.let {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = Enc
            val contentType = requestBody.contentType()
            contentType?.let {
                charset = contentType.charset(Enc)
            }
            body = buffer.readString(charset!!)
        }
        Log.e(TAG, "\n")
        Log.e(TAG, "----------Start----------------")
        Log.e(
            TAG,
            "\nmethod->: " + request.method
                    + "\nurl->: " + request.url
                    + "\nheaders->: {${request.headers}}"
                    + "\nrequestParams->: {$body}"
        )

        val startNs = System.nanoTime()
        val response = chain.proceed(request)


        val responseBody = response.body
        val rBody: String

        val source = responseBody!!.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()

        var charset: Charset? = Enc
        val contentType = responseBody.contentType()
        contentType?.let {
            try {
                charset = contentType.charset(Enc)
            } catch (e: UnsupportedCharsetException) {
                Log.e(TAG, e.message)
            }
        }
        rBody = buffer.clone().readString(charset!!)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        Log.e(
            TAG,
            "\ncode->: " + response.code
                    + "\nResponse->: " + rBody
        )
        Log.e(TAG, "----------End->" + tookMs + "毫秒----------")
        return response
    }
}