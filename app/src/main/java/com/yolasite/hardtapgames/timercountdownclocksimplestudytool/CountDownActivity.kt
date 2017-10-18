package com.yolasite.hardtapgames.timercountdownclocksimplestudytool

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import android.widget.*

class CountDownActivity : AppCompatActivity() {
    lateinit private var pBar: ProgressBar
    lateinit private var editTCount: EditText
    lateinit private var txtViewCount: TextView
    lateinit private var imageViewSwitch: ImageView
    lateinit private var imageViewReset: ImageView
    lateinit private var countDownTimer: CountDownTimer
    internal var isRunning = false
    internal var timeInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_count_down)
        pBar = findViewById(R.id.progressBar)

        pBar = findViewById<ProgressBar>(R.id.progressBar)
        editTCount = findViewById<EditText>(R.id.editTCount)
        txtViewCount = findViewById<TextView>(R.id.txtViewCount)
        imageViewSwitch = findViewById<ImageView>(R.id.imageViewSwitch)
        imageViewReset = findViewById<ImageView>(R.id.imageViewReset)



        imageViewSwitch.setOnClickListener {
            if (!isRunning) {
                if (editTCount.text.toString().isEmpty()) {
                    Toast.makeText(this@CountDownActivity, "Enter number of seconds", Toast.LENGTH_LONG).show()
                } else {
                    start()
                }
            } else {
                stop()
            }
        }

        imageViewReset.setOnClickListener {
            stop()
            isRunning = false
            imageViewSwitch.setImageResource(R.drawable.ic_start)
            txtViewCount.text = "" + timeInMillis / 1000
            pBar.progress = timeInMillis.toInt() / 1000
            pBar.max = timeInMillis.toInt() / 1000
        }
    }
    private fun start() {
        val textInput = editTCount.text.toString()
        val timeInput = textInput.toLong() * 1000
        timeInMillis = timeInput

        pBar.max = timeInMillis.toInt() / 1000
        imageViewSwitch.setImageResource(R.drawable.ic_stop)
        isRunning = true

        countDownTimer = object : CountDownTimer(timeInMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                txtViewCount.text = Math.round(millisUntilFinished * 0.001f).toString()
                pBar.progress = Math.round(millisUntilFinished * 0.001f)
            }

            override fun onFinish() {

            }
        }.start()
        countDownTimer.start()
    }

    private fun stop() {
        imageViewSwitch.setImageResource(R.drawable.ic_start)
        isRunning = false
        countDownTimer.cancel()
    }
}

