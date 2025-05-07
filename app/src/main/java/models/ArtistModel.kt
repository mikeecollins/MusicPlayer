package models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistModel(
    var artistname: String ="",
    var socialmedia: String ="",
    var artistTitle: String = "",
    var age: Int = 0,
    var dateofbirth: String = "",
    var image: Uri = Uri.EMPTY,
    var aboutartist: String = "") : Parcelable