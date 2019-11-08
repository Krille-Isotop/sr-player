package com.example.playah

data class Episodes(val episodes: Array<Episode>)

data class Episode(
    val title: String,
    val description: String,
    val imageurl: String,
    val downloadpodfile: downloadpodfile,
    val id: Int
)

data class downloadpodfile(val url: String)