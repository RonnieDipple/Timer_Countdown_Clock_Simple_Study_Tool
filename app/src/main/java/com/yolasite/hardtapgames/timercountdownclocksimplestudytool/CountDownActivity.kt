package com.yolasite.hardtapgames.timercountdownclocksimplestudytool

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar

class CountDownActivity : AppCompatActivity() {
    lateinit private var pBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_count_down)
        pBar = findViewById(R.id.progressBar)
    }
}
