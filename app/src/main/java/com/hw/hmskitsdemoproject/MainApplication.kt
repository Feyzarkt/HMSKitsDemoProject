package com.hw.hmskitsdemoproject

import android.app.Application
import com.huawei.hms.maps.MapsInitializer

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
    }
}