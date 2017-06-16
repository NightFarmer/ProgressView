package com.nightfarmer.progressview

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressView.color = ContextCompat.getColor(this, R.color.downloadButton)
        progressView.borderWidth = 5f
        progressView.radius = 20f
//        progressView.progress = 0.6f
        progressView.textSize = 50f

        progressView.onStart = {
            startLoadingThread()
        }
        progressView.onPause = {
            loadingThread?.state = ProgressView.ProgressState.Paused
        }
        progressView.onResume = {
            loadingThread?.state = ProgressView.ProgressState.Loading
        }
        progressView.onOpen = {
            progressView?.state = ProgressView.ProgressState.Loading
            startLoadingThread()
        }
    }

    var loadingThread: LoadingThread? = null

    private fun startLoadingThread() {
        loadingThread = LoadingThread()
        loadingThread?.onProgress = {
            progressView.progress = it
            if (it >= 1) {
                progressView?.state = ProgressView.ProgressState.Finished
            }
        }
        loadingThread?.start()
    }

    inner class LoadingThread : Thread() {
        var state = ProgressView.ProgressState.Loading
        var onProgress: ((Float) -> Unit)? = null
        var progress = 0f

        override fun run() {
            super.run()
            while (state == ProgressView.ProgressState.Loading || state == ProgressView.ProgressState.Paused) {
                if (progress > 1) {
                    break
                }
                progress += Math.random().toFloat() * 0.1f
                runOnUiThread {
                    onProgress?.invoke(Math.min(progress, 1f))
                }
                Thread.sleep(100)
            }
        }
    }

}
