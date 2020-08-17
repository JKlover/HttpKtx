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
        tvTest.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        httpJsonEngine<ResponseEntity> {
            url = "http://192.168.0.122:8181/data/dataByTime"
            headers{
                "token"-"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjMsIm5hbWUiOiIxMzc5MDczODMxMSIsInBob25lIjoiMTM3OTA3MzgzMTEiLCJleHAiOjE2MjM1MTE0MDUsImlzcyI6IlN0In0.sQSgiwhSjXR2FRtiFg8Y0I630CA0fKgJnooJ8YgWy9Q"
            }
//            method="get"
            onStart {
            }
            onSuccess {
                tvJson.text = it.toString()
            }
            onFail {

            }
        }
    }

    private fun initData() {
        httpJsonEngine<ResponseEntity> {
            url = "http://192.168.0.122:8181/login"
            params {
                "mobile" - "13790738311"
                "pwd"-"jokerw620302"
            }
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
