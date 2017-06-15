package com.nightfarmer.progressview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * 进度button
 * Created by zhangfan on 17-6-15.
 */
class ProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var radius = 10f
        set(value) {
            field = value
            invalidate()
        }

    var borderWidth = 1f
        set(value) {
            field = value
            paint_border.strokeWidth = value
            invalidate()
        }

    var color = Color.RED
        set(value) {
            field = value
            paint_border.color = value
            paint_bg.color = value
            paint_front.color = value
            paint_txt.color = value
            invalidate()
        }

    var progress = 0.3f
        set(value) {
            field = value
            invalidate()
        }

    var state = ProgressState.IDLE
        set(value) {
            field = value
            invalidate()
        }

    var textSize = DefaultFontSize
        set(value) {
            field = value
            paint_txt.textSize = value
            invalidate()
        }

    enum class ProgressState {
        Loading,
        Paused,
        Finished,
        IDLE,
    }

    var onStart: (() -> Unit)? = null
    var onPause: (() -> Unit)? = null
    var onResume: (() -> Unit)? = null
    var onOpen: (() -> Unit)? = null

    var idleText = "下载"
    var loadingTextRender = { progress: Float -> "${(progress * 100).toInt()}%" }
    var pausedText = "暂停"
    var finishedText = "完成"

    val rectF = RectF(0f, 0f, 0f, 0f)
    val maskRectF = RectF(0f, 0f, 0f, 0f)
    val txt_rect = Rect()

    val paint_border = Paint()
    var paint_bg = Paint()
    var paint_front = Paint()
    var paint_txt = Paint()


    companion object {
        private val DefaultFontSize = 30f
    }

    init {
        paint_bg.color = color
        paint_bg.isAntiAlias = true

        paint_front.color = color
        paint_front.isAntiAlias = true
        paint_front.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        paint_border.color = color
        paint_border.isAntiAlias = true
        paint_border.strokeWidth = borderWidth
        paint_border.style = Paint.Style.STROKE

        paint_txt.color = color
        paint_txt.textSize = textSize
        paint_txt.isAntiAlias = true
        paint_txt.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        paint_txt.textAlign = Paint.Align.LEFT

        setOnClickListener {
            when (state) {
                ProgressState.IDLE -> {
                    state = ProgressState.Loading
                    onStart?.invoke()
                }
                ProgressState.Loading -> {
                    state = ProgressState.Paused
                    onPause?.invoke()
                }
                ProgressState.Paused -> {
                    state = ProgressState.Loading
                    onResume?.invoke()
                }
                ProgressState.Finished -> {
                    onOpen?.invoke()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawRoundRect(rectF, radius, radius, paint_bg)

        maskRectF.left = rectF.left + progress * (rectF.right - rectF.left)
        canvas.drawRect(maskRectF, paint_front)
        canvas.drawRoundRect(rectF, radius, radius, paint_border)

        val text = parseText()
        paint_txt.getTextBounds(text, 0, text.length, txt_rect)
        val textX = (width / 2) - txt_rect.centerX()
        val textY = (height / 2) - txt_rect.centerY()
        canvas.drawText(text, textX.toFloat(), textY.toFloat(), paint_txt)

        canvas.restoreToCount(layerId)
    }

    private fun parseText() = when (state) {
        ProgressState.IDLE -> idleText
        ProgressState.Finished -> finishedText
        ProgressState.Paused -> pausedText
        else -> loadingTextRender(progress)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rectF.left = borderWidth / 2
        rectF.top = borderWidth / 2
        rectF.right = width - borderWidth / 2
        rectF.bottom = height - borderWidth / 2

        maskRectF.left = rectF.left
        maskRectF.top = rectF.top
        maskRectF.right = rectF.right
        maskRectF.bottom = rectF.bottom
    }
}
