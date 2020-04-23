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

        httpJsonEngine<ResponseEntity> {
            url = "https://api.apiopen.top/getSingleJoke?sid=28654780"
            method = "get"
            onStart {
            }
            onSuccess {
                tvDialog.text = it.toString()
            }
            onFail {

            }
        }
    }

}