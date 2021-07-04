package com.dktrips.traveltales

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.location.DefaultLocationProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        MapboxSearchSdk.initialize(
            this,
            getString(R.string.mapbox_access_token),
            DefaultLocationProvider(this)
        )
    }
}