package com.yolasite.hardtapgames.timercountdownclocksimplestudytool

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class myBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals("com.tester.alarmmanager")) {
            Notification.DEFAULT_SOUND
            Notification.DEFAULT_VIBRATE


        }

    }

}
