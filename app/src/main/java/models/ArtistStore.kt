package models

interface ArtistStore {
    fun findAll(): List<ArtistModel>
    fun create(artist: ArtistModel)
    fun update(artist: ArtistModel)
    fun delete(artist: ArtistModel)


}