package com.st.httpktx.test

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.st.httpktx.httpHtmlEngine
import com.st.httpktx.httpJsonEngine
import kotlinx.android.synthetic.main.activity_html.*
import kotlinx.android.synthetic.main.activity_json.*
import kotlinx.android.synthetic.main.dialog_http_test.*


/**
 * code bt St on 2020/3/27
 */
class HttpDialog constructor(context: Context) : Dialog(context, R.style.push_animation_dialog_style) {
    init {
        val view = View.inflate(context, R.layout.dialog_http_test, null)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(view)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT //设置宽度
        window!!.attributes = lp
        setCanceledOnTouchOutside(false) //点击dialog背景部分不消失
        initView()
    }

    private fun initView() {
        httpHtmlEngine {
            url = "https://www.jjmj.tv/mjplay/6424-1-1.html"
            method="get"
            headers{
                "user-agent"-"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36 Edg/81.0.416.68"
            }
            onStart {
            }
            onSuccess {
                tvDialog.text = it.toString()
                println(it)
            }
            onFail {

            }
        }
    }

}