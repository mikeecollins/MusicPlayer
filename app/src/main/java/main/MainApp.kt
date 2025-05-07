package main

import android.app.Application
import models.ArtistMemStore
import models.ArtistModel
import models.SongMemStore
import models.SongModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {


    val artists = ArtistMemStore()
    val songs = SongMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i(" Activity started")
    }



}
