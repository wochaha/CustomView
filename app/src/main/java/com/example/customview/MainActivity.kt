package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batman_image.isClickable = true

        //没有拦截点击事件，无法触发逻辑
        batman_image.setOnClickListener {
            Toast.makeText(this,"蝙蝠侠",Toast.LENGTH_SHORT).show()
        }

        batman_image.setOnLongClickListener {
            Toast.makeText(this,"长按",Toast.LENGTH_SHORT).show()
            true
        }
    }
}
