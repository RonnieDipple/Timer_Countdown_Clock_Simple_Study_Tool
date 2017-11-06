package com.yolasite.hardtapgames.timercountdownclocksimplestudytool


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.kobakei.ratethisapp.RateThisApp
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast


class CountDownActivity : AppCompatActivity() {
    lateinit private var pBar: ProgressBar
    lateinit private var editTCount: EditText
    lateinit private var txtViewCount: TextView
    lateinit private var imageViewPlayButton: ImageView
    lateinit private var imageViewReset: ImageView
    lateinit private var countDownTimer: CountDownTimer
    lateinit private var imagePauseView: ImageView


    internal var timeInMillis: Long = 0
    internal var millisLeft: Long = 0


    private var isRunning = false
    private var isReset = false
    private var isPaused = false
    private var isOnFinish = false
    private var count = 0
    private var startCount = 0


    private lateinit var b2adView: AdView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_count_down) //<-the main countdown view

        //VVV this is the rate app pop up section VVVV

        // Monitor launch times and interval from installation
        RateThisApp.onStart(this)
        // If the condition is satisfied, "Rate this app" dialog will be shown
        val config = RateThisApp.Config(3, 2)
        RateThisApp.showRateDialogIfNeeded(this)
        config.setUrl("http://www.google.com")// <-- before launch change to app store location of app
        RateThisApp.init(config)


        //^^^ this is the rate app pop up section ^^^

        //VVV adverts make sure the numbers are right before launch VVV


//        MobileAds.initialize(applicationContext,
//                "ca-app-pub-6679533492072913~3923978625") //<-- change before launch change//changed
//
//        b2adView = findViewById(R.id.b2adView)
//        val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
//
//
//        b2adView.loadAd(adRequest)

        //^^^ adverts make sure the numbers are right before launch ^^^

        pBar = findViewById(R.id.progressBar) //<- initialises the circular progress bar
        editTCount = findViewById(R.id.editTCount) //<- initialises the field to input the to count down
        txtViewCount = findViewById(R.id.txtViewCount)//<- initialises the visual representation of the seconds in the count down

        imageViewPlayButton = findViewById(R.id.imageViewPlayButton) //<- initialises the button that starts the count down
        imageViewReset = findViewById(R.id.imageViewReset)//<- initialises the button that resets the count down
        imagePauseView = findViewById(R.id.imagePauseView)//<- initialises the button that pauses the count down


        editTCount.addTextChangedListener(object : TextWatcher {
            // addTextChangedListener "listens" to see if text has been changed in the editTcount text field
            //TextWatcher then acts based on the text before it was change, during the change and after the change
            //afterTextChanged is the only field where you can  edit the text and actions of the editTcount view

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                //performs the actions below after the field to add the count down number has been changed, including deleting a previous number
                //on deleting a number the field is changed to null so the actions below are still performed

                if (!isRunning) {
                    millisLeft = 0
                    isRunning = false
                    txtViewCount.text = "0" + timeInMillis / 1000
                    pBar.progress = timeInMillis.toInt()
                    pBar.max = timeInMillis.toInt() / 1000

                }

            }
        })


        imagePauseView.setOnClickListener {

            if (editTCount.text.isEmpty() && isRunning) {
                //pauses the time
                count++ //ensures that on starting the timer back up, that it resumes from the same time
                isPaused = true

                stop()


            } else if (editTCount.text.isEmpty() || editTCount.text.isNotEmpty() && startCount <= 0) {
                mainToast()
            } else {

                //pauses the time
                count++ //ensures that on starting the timer back up, that it resumes from the same time
                isPaused = true

                stop()
            }


        }

        imageViewPlayButton.setOnClickListener {


            //starts the show and prevents a null pointer exception






            if (!isRunning) {

                if (editTCount.text.toString().isEmpty()) {
                    mainToast()

                } else {

                    start()
                }
            } else {

                stop()


            }
        }

        imageViewReset.setOnClickListener {
            //resets everything and activates reset boolean to true, to be checked in onFinish to prevent
            // the end sound from chiming when a new number is placed in the editTcount text field


            //resets everything and activates reset boolean to true, to be checked in onFinish to prevent
            // the end sound from chiming when a new number is placed in the editTcount text field
            if (editTCount.text.isEmpty() && isRunning||editTCount.text.isEmpty() && !isRunning) {


                stop()
                millisLeft = 0
                txtViewCount.text = "" + timeInMillis / 1000
                pBar.progress = timeInMillis.toInt()
                pBar.max = timeInMillis.toInt() / 1000
                isReset = true
                editTCount.text = null


            } else if (editTCount.text.isEmpty() && !isRunning && startCount <= 0 || editTCount.text.isEmpty() || editTCount.text.isNotEmpty() && startCount <= 0) {
                mainToast()
            } else {
                stop()
                millisLeft = 0
                txtViewCount.text = "" + timeInMillis / 1000

                pBar.progress = timeInMillis.toInt()
                pBar.max = timeInMillis.toInt() / 1000
                isReset = true
                editTCount.text = null

            }


        }

    }

    private fun start() {

        //starts everything and resets isPaused and isReset to false so that the sound will play when onFinish is activated

        val textInput = editTCount.text.toString()
        val timeInput = textInput.toLong() * 60 * 1000
        timeInMillis = timeInput
        pBar.max = timeInMillis.toInt() / 1000
        startCount++
        isPaused = false

        while (!isPaused && count > 0) {
            //when paused and started again this ensures the timer starts from the time it was paused
            timeInMillis = millisLeft - 1000
            count--


        }

        imageViewPlayButton.setImageResource(R.drawable.ic_stop)
        isRunning = true


        countDownTimer = object : CountDownTimer(timeInMillis + 1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                //^^^this is the engine of the app, up top timeInMillis starts the time 1 second ahead^^^
                // ^^^so 5:00 would be 4:59, 1000 is added to timeInMillis to compensate making it 5:00^^^

                //VVV converts milliseconds in to seconds and minutes VVVV

                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60


                //^converts milliseconds in to seconds and minutes^

                //VVV shows the progress of time in circular progress bar and visual representation of the time VVV


                txtViewCount.text = String.format("%02d:%02d", minutes, seconds)
                pBar.progress = Math.round(millisUntilFinished * 0.001f)
                millisLeft = millisUntilFinished

                //^^^ shows the progress of time in circular progress bar and visual representation of the time ^^^


            }


            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                //finishes everything and changes the stop button view back into the play button view
                imageViewPlayButton.setImageResource(R.drawable.ic_start)
                isRunning = false
                timeInMillis = 0
                txtViewCount.text = "0" + timeInMillis

                pBar.progress = timeInMillis.toInt()
                pBar.max = timeInMillis.toInt() / 1000
                isOnFinish = true
                stop()
                if (!isReset) {
                    playSound()


                }

                if (isReset) {
                    toasty()
                }


            }


            //VVV starts everything, very important VVV
        }.start()
        countDownTimer.start()
        isReset = false


        //^^^ starts everything, very important ^^^

    }

    private fun stop() {

        //stops everything and ensures isRunning is false

        imageViewPlayButton.setImageResource(R.drawable.ic_start)
        isRunning = false
        timeInMillis = 0
        countDownTimer.cancel()


    }

    private fun playSound() {
        // ensures a sound is played at the end of the count down

        val mp = MediaPlayer.create(this, R.raw.tone)

        mp.start()


    }

    private fun toasty() {
//        val myToast = Toast.makeText(this, getString(R.string.Press_The_Play_button_again_to_confirm_a_new_time), Toast.LENGTH_LONG)
//        myToast.setGravity(Gravity.CENTER, 0, 0)
//        myToast.show()
        val toast = Toast.makeText(this, getString(R.string.Press_The_Play_button_again_to_confirm_a_new_time), Toast.LENGTH_SHORT)
        val layout = toast.view as LinearLayout
        if (layout.childCount > 0) {
            val tv = layout.getChildAt(0) as TextView
            tv.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        }
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()

    }

    private fun mainToast() {


        val toast = Toast.makeText(this, getString(R.string.Enter_number_and_press_the_play_button), Toast.LENGTH_SHORT)
        val layout = toast.view as LinearLayout
        if (layout.childCount > 0) {
            val tv = layout.getChildAt(0) as TextView
            tv.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        }
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()

    }


}

