package com.st.httpktx.test

import android.os.Bundle
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

        httpHtmlEngine {
            url = "http://aqdyen.com/search.asp"
            method = "post"
            enc="gbk"
            headers{
                "Connection"- "Keep-Alive"
                "Referer"-"http://aqdyen.com/"
                "User-Agent"-"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Safari/537.36 Edg/80.0.361.109"
            }
            params{
                "searchword"-"路西法"
            }
            onStart {
                //加载的loading
            }

            onSuccess {
                //加载关闭loading
                tvHtml.text=it
            }
        }
    }
}
