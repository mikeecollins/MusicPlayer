package models

import timber.log.Timber.i

var lastId = 0L
internal fun getId() = lastId++
class SongMemStore : SongStore {


    val songs = ArrayList<SongModel>()

    override fun findAll(): List<SongModel> {
        return songs
    }


    override fun create(song: SongModel) {
        song.id = getId()
        songs.add(song)
        logAll()
    }


    override fun update(song: SongModel) {
        val foundSong: SongModel? = songs.find { p -> p.id == song.id }
        if (foundSong != null) {
            foundSong.songname = song.songname
            foundSong.genre = song.genre
            foundSong.duration = song.duration
            foundSong.maxrating = song.maxrating
            foundSong.releasedate = song.releasedate
            foundSong.isFavourite = song.isFavourite
            logAll()
        }
    }
    private fun logAll(){
        songs.forEach { i("${it}")}
    }
}