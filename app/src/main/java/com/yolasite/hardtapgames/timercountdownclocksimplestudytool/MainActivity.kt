package com.yolasite.hardtapgames.timercountdownclocksimplestudytool

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var b1adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        //VVV adverts make sure the numbers are right before launch VVV

//        MobileAds.initialize(applicationContext,
//                "ca-app-pub-6679533492072913~3923978625") //<-- change before launch change//now changed
//
//        b1adView = findViewById(R.id.b1adView)
//        val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
//
//
//        b1adView.loadAd(adRequest)

        //^^^ adverts make sure the numbers are right before launch ^^^


        pressToStartBtn.setOnClickListener {

            val cIntent = Intent(this, CountDownActivity::class.java)
            startActivity(cIntent)
        }
    }
}
