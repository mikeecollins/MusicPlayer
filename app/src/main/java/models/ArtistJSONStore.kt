package models

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import ie.setu.app.helpers.exists
import ie.setu.app.helpers.read
import ie.setu.app.helpers.write
import timber.log.Timber
import java.lang.reflect.Type


const val JSON_FILE = "artists.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()

val listType: Type = object : TypeToken<ArrayList<ArtistModel>>() {}.type
class ArtistJSONSore(private val context: Context) : ArtistStore {

    var artists = mutableListOf<ArtistModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ArtistModel> {
        logAll()
        return artists
    }

    override fun create(artist: ArtistModel) {
        artists.add(artist)
        serialize()
    }

    override fun update(artist: ArtistModel) {
        val foundArtist = artists.find { it.artistname == artist.artistname }
        if (foundArtist != null) {
            foundArtist.socialmedia = artist.socialmedia
            foundArtist.artistTitle = artist.artistTitle
            foundArtist.age = artist.age
            foundArtist.dateofbirth = artist.dateofbirth
            foundArtist.image = artist.image
            foundArtist.aboutartist = artist.aboutartist
            serialize()
        }
    }

    override fun delete(artist: ArtistModel) {
        artists.remove(artist)
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(artists, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        artists = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        artists.forEach { Timber.i("$it") }
    }
}
class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }
    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
