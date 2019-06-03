package com.example.customview.view

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.example.customview.R
import com.nineoldandroids.view.ViewHelper
import java.util.*

class CustomView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {
    private var mLastX : Int = 0
    private var mLastY : Int = 0
    private val longPress = 300

    private val mWidth = 220*2
    private val mHeight = 220*2

    private var radius = 200f

    //判断是否可以出发长按和点击事件
    private var isClick = true
    private var isLongClick = false

    private lateinit var timer:Timer
    private lateinit var timerTask:TimerTask

    //将资源编码为bitmap
    private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.batman)

    //线性shader
    private val linearShader = LinearGradient(50f,100f,1050f,1100f,
        Color.parseColor("#E91E63"),
        Color.parseColor("#2196F3"),
        Shader.TileMode.CLAMP)

    private val radialShader = RadialGradient(550f,600f,500f,
        Color.parseColor("#E91E63"),
        Color.parseColor("#2196F3"),
        Shader.TileMode.REPEAT)

    //bitmap shader
    private val bitmapShader = BitmapShader(bitmap,
        Shader.TileMode.CLAMP,
        Shader.TileMode.CLAMP)

    private val linearPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
        shader = linearShader
    }

    private val radialPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
        shader = radialShader
    }

    //bitmap画笔
    private val bitmapPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
        shader = bitmapShader
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        //宽高都为wrap时设置view的大小为默认值
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth,mHeight)
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth,heightSpecSize)
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,mHeight)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
//        super.onDraw(canvas)
//        canvas?.drawCircle(550f,600f,500f,linearPaint)
//        canvas?.drawCircle(550f,600f,500f,radialPaint)
        //简单处理padding
        val width = mWidth - paddingLeft - paddingRight
        val height = mHeight - paddingBottom - paddingTop
        canvas?.drawCircle((width/2).toFloat(),(height/2).toFloat(),radius,bitmapPaint)
    }


    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun performLongClick(): Boolean {
        return super.performLongClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.rawX.toInt()
        val y = event.rawY.toInt()

        when(event.action){
            MotionEvent.ACTION_MOVE -> {
                //移动时设置点击和长按事件不生效并取消计时任务
                isClick = false
                isLongClick = false
                timerTask.cancel()
                timer.cancel()

                //view内容滑动的逻辑
                val deltaX = x.minus(mLastX)
                val deltaY = y.minus(mLastY)
                Log.d("moveAction","move, deltaX: $deltaX deltaY:$deltaY")
                val translationX = ViewHelper.getTranslationX(this).toInt() + deltaX
                val translationY = ViewHelper.getTranslationY(this).toInt() + deltaY
                ViewHelper.setTranslationX(this, translationX.toFloat())
                ViewHelper.setTranslationY(this, translationY.toFloat())

            }
            MotionEvent.ACTION_UP ->{
                if (event.pointerCount == 1){
                    //performClick()调用已设置的clickListener的onClick()方法
                    //根据isClick和isLongClick判断触发长按还是点击事件
                    //之后取消计时任务
                    if (isClick && !isLongClick){
                        performClick()
                    }else if (isLongClick){
                        performLongClick()
                    }
                    timerTask.cancel()
                    timer.cancel()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                if (event.pointerCount == 1){
                    //按下时初始化计时任务
                    timer = Timer()
                    timerTask =object : TimerTask(){
                        override fun run() {
                            isClick = false
                            isLongClick = true
                        }
                    }

                    //重新设置为初始状态
                    isClick = true
                    isLongClick = false

                    //开始计时任务，延时之后异步更新isClick和isLongClick
                    timer.schedule(timerTask,longPress.toLong(),1000*60*60*24)
                }
            }
        }
        mLastX = x
        mLastY = y
        return true
    }
}