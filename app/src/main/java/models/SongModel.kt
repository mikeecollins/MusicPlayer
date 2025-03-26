package models

data class SongModel(var maxrating: Int = 10,
                     var isFavourite: Boolean = false,
                     var songname: String = "",
                     var genre: String = "",
                     var releasedate: String = "",
                     var songid: Int = 0,
                     var duration: Double = 800.0)
