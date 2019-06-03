package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batman_image.isClickable = true

        batman_image.setOnClickListener {
            Toast.makeText(this,"蝙蝠侠",Toast.LENGTH_SHORT).show()
        }

        batman_image.setOnLongClickListener {
            Toast.makeText(this,"长按",Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onStart() {
        super.onStart()
//        batman_image.post {
//            val width = batman_image.measuredWidth
//            val height = batman_image.measuredHeight
//            Log.d("BATMAN POST","宽是：$width，高是：$height")
//        }

        val observe = batman_image.viewTreeObserver
        observe.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                batman_image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = batman_image.measuredWidth
                val height = batman_image.measuredHeight
                Log.d("BATMAN TREE","宽是：$width，高是：$height")
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            val width = batman_image.measuredWidth
            val height = batman_image.measuredHeight
            Log.d("BATMAN WINDOW","宽是：$width，高是：$height")
        }
    }
}
