package com.rr.deejay.service

interface PlaylistService {
    fun addToPlaylist(name: String, youtubeUrl: String): Boolean
    fun currentPlaylist() : List<Map<String, String>>
}
