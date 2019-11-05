package com.rr.deejay.so

import com.rr.deejay.ServiceRunner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Playlist() {
    @Inject lateinit var playlistWriter: PlaylistWriter
    private val list: MutableList<MutableMap<String, String>> = mutableListOf()

    init{
        ServiceRunner.serviceComponent.inject(this)
    }

    fun append(name: String, item: String) {
        list.add(mutableMapOf(name to item))
        playlistWriter.write(item)
    }

    fun list() : List<Map<String, String>> {
        return list
    }

    fun truncateAll() {
        list.clear()
    }
}