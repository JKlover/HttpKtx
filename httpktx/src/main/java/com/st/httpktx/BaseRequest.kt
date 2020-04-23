package com.st.httpktx

import com.st.httpktx.partial.partially1
import okhttp3.RequestBody

/**
 * code bt St on 2020/4/13
 */
open class BaseRequest<T> {
    var url: String? = null
    var method: String? = null
    var body: RequestBody? = null
    var tag: Any? = null
    var postRequest: Any? = null
    var mParams: MutableMap<String, String> = mutableMapOf()
    var mHeaders: MutableMap<String, String> = mutableMapOf()
    var mStart: (() -> Unit) = {}
    var mSuccess: (T) -> Unit = {}
    var mFail: (String) -> Unit = {}

    var pairs = fun(map: MutableMap<String, String>, makePairs: RequestPairs.() -> Unit) {
        val requestPair = RequestPairs()
        requestPair.makePairs()
        map.putAll(requestPair.pairs)
    }
    var params = pairs.partially1(mParams)
    var headers = pairs.partially1(mHeaders)
    fun onStart(onStart: () -> Unit) {
        mStart = onStart
    }

    fun onSuccess(onSuccess: (T) -> Unit) {
        mSuccess = onSuccess
    }

    fun onFail(onError: (String) -> Unit) {
        mFail = onError
    }
}