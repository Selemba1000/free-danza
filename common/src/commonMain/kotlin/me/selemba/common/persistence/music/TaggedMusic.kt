package me.selemba.common.persistence.music

import androidx.compose.ui.graphics.ImageBitmap

class TaggedMusic(
    var filetitle:String,
    var title: String,
    var artist: String,
    var album: String,
    var bpm: Int,
    var date: String,
    var genre: String,
    val length: Long,
    val artwork: ImageBitmap?,
) {
}