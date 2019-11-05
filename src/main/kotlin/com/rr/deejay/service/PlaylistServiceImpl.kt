package com.rr.deejay.service

import com.rr.deejay.ServiceRunner
import com.rr.deejay.logger
import com.rr.deejay.so.Playlist
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PlaylistServiceImpl() : PlaylistService {
    @Inject lateinit var playList: Playlist
    init {
        ServiceRunner.serviceComponent.inject(this)
    }

    override fun addToPlaylist(name: String, youtubeUrl: String): Boolean {
        playList.append(name, youtubeUrl)
        logger.info("$name added to playlist - $youtubeUrl")
        return true
    }

    override fun currentPlaylist(): List<Map<String, String>> {
        return playList.list()
    }
}