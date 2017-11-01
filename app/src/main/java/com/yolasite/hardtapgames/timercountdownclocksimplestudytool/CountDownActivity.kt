package com.yolasite.hardtapgames.timercountdownclocksimplestudytool


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.kobakei.ratethisapp.RateThisApp


class CountDownActivity : AppCompatActivity() {
    lateinit private var pBar: ProgressBar
    lateinit private var editTCount: EditText
    lateinit private var txtViewCount: TextView
    lateinit private var imageViewPlayButton: ImageView
    lateinit private var imageViewReset: ImageView
    lateinit private var countDownTimer: CountDownTimer
    lateinit private var txtViewCountMinutes: TextView
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
        val config = RateThisApp.Config(3, 1)
        RateThisApp.showRateDialogIfNeeded(this)
        config.setUrl("http://www.google.com")// <-- before launch change to app store location of app
        RateThisApp.init(config)


        //^^^ this is the rate app pop up section ^^^

        //VVV adverts make sure the numbers are right before launch VVV


        MobileAds.initialize(applicationContext,
                "ca-app-pub-3940256099942544~3347511713") //<-- change before launch change

        b2adView = findViewById(R.id.b2adView)
        val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()


        b2adView.loadAd(adRequest)

        //^^^ adverts make sure the numbers are right before launch ^^^

        pBar = findViewById(R.id.progressBar) //<- initialises the circular progress bar
        editTCount = findViewById(R.id.editTCount) //<- initialises the field to input the to count down
        txtViewCount = findViewById(R.id.txtViewCount)//<- initialises the visual representation of the seconds in the count down
        txtViewCountMinutes = findViewById<EditText>(R.id.txtViewCountMinutes)//<- initialises the visual representation of the minutes in the count down
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
                    txtViewCount.text = "" + timeInMillis / 1000
                    txtViewCountMinutes.text = "" + timeInMillis / 1000
                    pBar.progress = timeInMillis.toInt() + 0
                    pBar.max = timeInMillis.toInt() / 1000
                }

            }
        })

        imagePauseView.setOnClickListener {
            //pauses the time
            count++ //ensures that on starting the timer back up, that it resumes from the same time
            isPaused = true
            stop()

        }

        imageViewPlayButton.setOnClickListener {

            //starts the show and prevents a null pointer exception


            if (!isRunning) {
                if (editTCount.text.toString().isEmpty()) {
                    Toast.makeText(this@CountDownActivity, R.string.Enter_number_of_minutes, Toast.LENGTH_LONG).show()

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
            stop()
            millisLeft = 0
            isRunning = false
            imageViewPlayButton.setImageResource(R.drawable.ic_start)
            countDownTimer.cancel()
            txtViewCount.text = "" + timeInMillis / 1000
            txtViewCountMinutes.text = "" + timeInMillis / 1000
            pBar.progress = timeInMillis.toInt() + 0
            pBar.max = timeInMillis.toInt() / 1000
            isReset = true


        }

    }

    private fun start() {
        //starts everything and resets isPaused and isReset to false so that the sound will play when onFinish is activated

        val textInput = editTCount.text.toString()
        val timeInput = textInput.toLong() * 60 * 1000
        timeInMillis = timeInput
        pBar.max = timeInMillis.toInt() / 1000
        isPaused = false
        isReset = false
        startCount++





        while (isPaused && count > 0) {
            //when paused and started again this ensures the timer starts from the time it was paused
            timeInMillis = millisLeft
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

                txtViewCountMinutes.text = minutes.toString()
                txtViewCount.text = seconds.toString()
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
                txtViewCount.text = "" + timeInMillis
                txtViewCountMinutes.text = "" + timeInMillis
                pBar.progress = timeInMillis.toInt() + 0
                pBar.max = timeInMillis.toInt() / 1000
                isOnFinish = true

                if (!isPaused || !isReset) {
                    playSound()


                    //prevents the end chime from playing when the time is changed or reset
                    // ensures it only plays once the timer reaches zero


                }


            }

            //VVV starts everything, very important VVV
        }.start()
        countDownTimer.start()

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


}

