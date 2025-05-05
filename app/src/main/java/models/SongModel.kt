package models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongModel(var id: Long = 0,
                     var maxrating: Int = 10,
                     var isFavourite: Boolean = false,
                     var songname: String = "",
                     var genre: String = "",
                     var releasedate: String = "",
                     var duration: Double = 800.0) : Parcelable
