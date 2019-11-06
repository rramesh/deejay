package com.rr.deejay.so

import com.google.common.base.Charsets
import com.google.common.io.FileWriteMode
import com.google.common.io.Files
import com.rr.deejay.ServiceRunner
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistFileWriter() : PlaylistWriter {
    @Inject lateinit var playlistFile: File

    init {
        ServiceRunner.serviceComponent.inject(this)
    }

    override fun write(text: String) {
        val chs = Files.asCharSink(playlistFile, Charsets.UTF_8, FileWriteMode.APPEND)
        chs.write(text)
        chs.write("\n")
    }
}