package com.nightfarmer.progressview

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressView.color = ContextCompat.getColor(this, R.color.downloadButton)
        progressView.borderWidth = 5f
        progressView.radius = 20f
        progressView.progress = 0.6f
        progressView.textSize = 50f
        progressView.onStart = {

        }
        progressView.onPause = {

        }
        progressView.onResume = {

        }
        progressView.onOpen = {

        }
    }
}
