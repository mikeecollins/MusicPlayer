package main

import android.app.Application
import models.ArtistModel
import models.SongModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    val songs = ArrayList<SongModel>()
    val artists = ArrayList<ArtistModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i(" Activity started")
    }

}
