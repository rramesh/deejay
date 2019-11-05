package com.rr.deejay.so

import com.google.common.base.Charsets
import com.google.common.io.FileWriteMode
import com.google.common.io.Files
import com.rr.deejay.ServiceRunner
import com.rr.deejay.server.Configuration
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistFileWriter() : PlaylistWriter {
    @Inject lateinit var configuration: Configuration
    private val playlistFile: File by lazy {openPlaylistFile()}

    init {
        ServiceRunner.serviceComponent.inject(this)
    }

    private fun openPlaylistFile() : File {
        val fileName = configuration.getPlaylistFileName()
        return File(fileName)
    }

    override fun write(text: String) {
        val chs = Files.asCharSink(playlistFile, Charsets.UTF_8, FileWriteMode.APPEND)
        chs.write(text)
        chs.write("\n")
    }
}