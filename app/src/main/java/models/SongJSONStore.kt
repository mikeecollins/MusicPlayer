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
import kotlin.random.Random




const val SONGS_JSON_FILE = "songs.json"

val songListType: Type = object : TypeToken<ArrayList<SongModel>>() {}.type

class SongJSONStore(private val context: Context) : SongStore {

    var songs = mutableListOf<SongModel>()

    init {
        if (exists(context, SONGS_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<SongModel> {
        logAll()
        return songs
    }

    override fun create(song: SongModel) {
        songs.add(song)
        serialize()
    }

    override fun update(song: SongModel) {
        TODO("Not yet implemented")
    }

    override fun delete(song: SongModel) {
        songs.remove(song)
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(songs, songListType)
        write(context, SONGS_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, SONGS_JSON_FILE)
        songs = gsonBuilder.fromJson(jsonString, songListType)
    }

    private fun logAll() {
        songs.forEach { Timber.i("$it") }
    }
}




