package com.st.httpktx.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.st.httpktx.httpHtmlEngine
import kotlinx.android.synthetic.main.activity_html.*

/**
 * code bt St on 2020/4/13
 */
class HtmlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        initData()
//        initData2()
    }

    private fun initData2() {
        httpHtmlEngine {
            url = "https://api.rereapi.com/"
            method = "get"
//            enc = "gbk"
//            headers {
//                "Connection" - "Keep-Alive"
//                "Referer" - "http://aqdyen.com/"
//                "User-Agent" - "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Safari/537.36 Edg/80.0.361.109"
//            }
//            params {
//                "searchword" - "路西法"
//            }
            onStart {
                //加载的loading
            }
            onSuccess {
                //加载关闭loading
                tvHtml.text = it
            }
        }
    }

    private fun initData() {
        httpHtmlEngine {
            url = "https://s6.niuniu-baidu.com/2020/07/13/SseDPHpdcZQlGVRi/index.m3u8"
            method = "get"
//            enc = "gbk"
            headers {
                "sec-fetch-dest" - "document"
                "sec-fetch-mode" - "navigate"
                "sec-fetch-site" - "none"
                "sec-fetch-user" - "?1"
                "upgrade-insecure-requests" - "1"
                "User-Agent" - "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Safari/537.36 Edg/80.0.361.109"
            }
//            params {
//                "searchword" - "路西法"
//            }
            onStart {
                //加载的loading
            }
            onSuccess {
                //加载关闭loading
                tvHtml.text = it
            }
        }
    }
}
