package com.st.httpktx.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

/**
 * code bt St on 2020/4/13
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn1.setOnClickListener {
            startActivity<HtmlActivity>()
        }

        btn2.setOnClickListener {
            startActivity<JsonActivity>()
        }
    }
}
