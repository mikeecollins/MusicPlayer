package models

import timber.log.Timber.i

class ArtistMemStore : ArtistStore{

    val artists = ArrayList<ArtistModel>()

    override fun findAll(): List<ArtistModel> {
        return artists
    }

    override fun create(artist: ArtistModel) {
        artists.add(artist)
        logAll()
    }

    override fun update(artist: ArtistModel) {
        val foundArtist: ArtistModel? = artists.find {it  == artist}
        if (foundArtist != null) {
            foundArtist.artistTitle = artist.artistTitle
            foundArtist.artistname = artist.artistname
            foundArtist.age = artist.age
            foundArtist.dateofbirth = artist.dateofbirth
            foundArtist.image = artist.image

        }
    }
    fun logAll(){
        artists.forEach{ i("${it}")}
    }
}