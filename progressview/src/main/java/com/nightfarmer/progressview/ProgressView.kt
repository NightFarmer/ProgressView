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
            paintBorder.strokeWidth = value
            invalidate()
        }

    var color = Color.RED
        set(value) {
            field = value
            paintBorder.color = value
            paintBg.color = value
            paintFront.color = value
            paintTxt.color = value
            invalidate()
        }

    var progress = 0f
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
            paintTxt.textSize = value
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
    val txtRect = Rect()

    val paintBorder = Paint()
    var paintBg = Paint()
    var paintFront = Paint()
    var paintTxt = Paint()

    private var viewWidth = 0
    private var viewHeight = 0

    companion object {
        private val DefaultFontSize = 30f
    }

    init {
        paintBg.color = color
        paintBg.isAntiAlias = true

        paintFront.color = color
        paintFront.isAntiAlias = true
        paintFront.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        paintBorder.color = color
        paintBorder.isAntiAlias = true
        paintBorder.strokeWidth = borderWidth
        paintBorder.style = Paint.Style.STROKE

        paintTxt.color = color
        paintTxt.textSize = textSize
        paintTxt.isAntiAlias = true
        paintTxt.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        paintTxt.textAlign = Paint.Align.LEFT

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

        val layerId = canvas.saveLayer(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawRoundRect(rectF, radius, radius, paintBg)

        maskRectF.left = rectF.left + progress * (rectF.right - rectF.left)
        canvas.drawRect(maskRectF, paintFront)
        canvas.drawRoundRect(rectF, radius, radius, paintBorder)

        val text = parseText()
        paintTxt.getTextBounds(text, 0, text.length, txtRect)
        val textX = (viewWidth / 2) - txtRect.centerX()
        val textY = (viewHeight / 2) - txtRect.centerY()
        canvas.drawText(text, textX.toFloat(), textY.toFloat(), paintTxt)

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
        viewWidth = measuredWidth
        viewHeight = measuredHeight

        rectF.left = borderWidth / 2
        rectF.top = borderWidth / 2
        rectF.right = viewWidth - borderWidth / 2
        rectF.bottom = viewHeight - borderWidth / 2

        maskRectF.left = 0f
        maskRectF.top = 0f
        maskRectF.right = viewWidth.toFloat()
        maskRectF.bottom = viewHeight.toFloat()

    }
}
