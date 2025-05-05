package models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistModel(
                       var artistname: String ="",
                       var socialmedia: String ="",
                       var artistTitle: String = "",
                       var age: Int = 0,
                       var dateofbirth: String = "",
                       var aboutartist: String = "") : Parcelable