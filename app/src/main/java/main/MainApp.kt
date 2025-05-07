package main

import android.app.Application
import models.ArtistJSONSore
import models.ArtistMemStore
import models.ArtistModel
import models.ArtistStore
import models.SongJSONStore
import models.SongMemStore
import models.SongModel
import models.SongStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var artists: ArtistStore
    lateinit var songs: SongStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
       // artists =  ArtistMemStore()
       // songs = SongMemStore()

        artists = ArtistJSONSore(applicationContext)
        songs = SongJSONStore(applicationContext)
        i(" Activity started")
    }

}
