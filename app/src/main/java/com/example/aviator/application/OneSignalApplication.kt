package com.example.aviator.application

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.onesignal.OneSignal

class OneSignalApplication:Application() {

    val ONESIGNAL_APP_ID = "fff15156-2fdf-4aeb-a8fb-877a5ac9ad13"

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

}