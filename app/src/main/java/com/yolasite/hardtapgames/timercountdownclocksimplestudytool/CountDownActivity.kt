package com.yolasite.hardtapgames.timercountdownclocksimplestudytool


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.*


class CountDownActivity : AppCompatActivity() {
    lateinit private var pBar: ProgressBar
    lateinit private var editTCount: EditText
    lateinit private var txtViewCount: TextView
    lateinit private var imageViewPlayButton: ImageView
    lateinit private var imageViewReset: ImageView
    lateinit private var countDownTimer: CountDownTimer
    lateinit private var txtViewCountMinutes: TextView
    lateinit private var imagePauseView: ImageView



    private var isRunning = false
    internal var timeInMillis: Long = 0
    internal var millisLeft: Long = 0
    private var isPaused = false
    private var count = 0
    internal var resetCount = 0
    internal var pauseCount = 0
    private var startCount = 0





    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_count_down)
        pBar = findViewById(R.id.progressBar)

        pBar = findViewById(R.id.progressBar)
        editTCount = findViewById(R.id.editTCount)
        txtViewCount = findViewById(R.id.txtViewCount)
        imageViewPlayButton = findViewById(R.id.imageViewPlayButton)
        imageViewReset = findViewById(R.id.imageViewReset)
        txtViewCountMinutes = findViewById<EditText>(R.id.txtViewCountMinutes)
        imagePauseView = findViewById(R.id.imagePauseView)


        editTCount.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if(!isRunning){
                millisLeft = 0
                isRunning = false
                txtViewCount.text = "" + timeInMillis / 1000
                txtViewCountMinutes.text = "" + timeInMillis / 1000
                pBar.progress = timeInMillis.toInt() + 0
                pBar.max = timeInMillis.toInt() / 1000}

            }
        })

        imagePauseView.setOnClickListener {
            count++
            pauseCount++
            isPaused = true
            stop()

        }

        imageViewPlayButton.setOnClickListener {

            resetCount = 0
            pauseCount = 0
            startCount = 0
            if (!isRunning) {
                if (editTCount.text.toString().isEmpty()) {
                    Toast.makeText(this@CountDownActivity, "Enter number of minutes", Toast.LENGTH_LONG).show()

                }else{

                    start()
                }
            } else {

                stop()

            }
        }

        imageViewReset.setOnClickListener {
            stop()
            millisLeft =0
            isRunning = false
            imageViewPlayButton.setImageResource(R.drawable.ic_start)
            countDownTimer.cancel()
            txtViewCount.text = "" + timeInMillis / 1000
            txtViewCountMinutes.text = "" + timeInMillis / 1000
            pBar.progress = timeInMillis.toInt() + 0
            pBar.max = timeInMillis.toInt() / 1000
            resetCount++


        }
    }

    private fun start() {
        val textInput = editTCount.text.toString()
        val timeInput = textInput.toLong() * 60 * 1000
        timeInMillis = timeInput
        pBar.max = timeInMillis.toInt() / 1000
        pauseCount--
        startCount++




        while(isPaused && count > 0){
            timeInMillis = millisLeft
            count--



        }



        imageViewPlayButton.setImageResource(R.drawable.ic_stop)
        isRunning = true


        countDownTimer = object : CountDownTimer(timeInMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {



                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60

                txtViewCountMinutes.text = minutes.toString()
                txtViewCount.text = seconds.toString()
                pBar.progress = Math.round(millisUntilFinished * 0.001f)
                millisLeft = millisUntilFinished


            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                imageViewPlayButton.setImageResource(R.drawable.ic_start)
                isRunning = false
                timeInMillis = 0
                txtViewCount.text = "" + timeInMillis
                txtViewCountMinutes.text = "" + timeInMillis
                pBar.progress = timeInMillis.toInt() + 0
                pBar.max = timeInMillis.toInt() / 1000

                if (resetCount <= 0 && pauseCount <= 0){
                    playSound()
                    resetCount--


                }








            }
        }.start()
        countDownTimer.start()


    }

    private fun stop() {
        imageViewPlayButton.setImageResource(R.drawable.ic_start)
        isRunning = false
        timeInMillis = 0
        countDownTimer.cancel()


    }

    private fun playSound() {

        val mp = MediaPlayer.create(this, R.raw.tone)

        mp.start()

    }



}



