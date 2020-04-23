package com.st.httpktx.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.st.httpktx.httpJsonEngine
import kotlinx.android.synthetic.main.activity_json.*

/**
 * code bt St on 2020/4/13
 */
class JsonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
        initData()
    }

    private fun initData() {
        httpJsonEngine<ResponseEntity> {
            url = "https://api.apiopen.top/getSingleJoke?sid=28654780"
            method = "get"
            onStart {
            }
            onSuccess {
                tvJson.text = it.toString()
            }
            onFail {

            }
        }
    }
}
