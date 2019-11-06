package com.rr.deejay.server

import com.google.common.io.Resources.getResource
import com.rr.deejay.logger
import org.apache.logging.log4j.Level.toLevel
import org.apache.logging.log4j.core.config.Configurator
import java.io.File
import java.lang.System.getenv
import java.util.*
import javax.inject.Singleton

@Singleton
class Configuration (
    val envPrefix: String = "DEEJAY",
    val env: Map<String, String> = getenv(),
    val resourceFile: String = "application.properties") {
    private var playlistFile: File
    private val props = Properties()
    init {
        try{
            getResource(resourceFile).openStream().use(props::load)
        } catch(ex: IllegalArgumentException) {
            // [getResource] throws this exception when resource is not found.
            // It can be ignored in this case, as config values may be provided from other places.
        }

        readEnv(env)
        playlistFile = openPlaylistFile()
        props.getProperty("log.level")?.let {
            Configurator.setLevel("com.rr.deejay", toLevel(it))
        }
    }

    fun getPlaylistFile() : File {
        return playlistFile
    }

    fun getServicePort() : Int {
        val port = props.getProperty("service.port")
        if (port.isNullOrBlank()) return 9092
        return try{
            port.trim().toInt()
        } catch(nfe: NumberFormatException) {
            logger.warn("Warning: Non-numeric service port in service.port property. Defaulting to port 9092")
            9092
        }
    }
    private fun openPlaylistFile() : File {
        val fileName = getPlaylistFileName()
        try {
            val file = File(fileName)
            file.createNewFile()
            return file
        } catch(ex: Exception) {
            logger.error("FATAL. Deejay failed to boot as it could not open playlist file")
            ex.printStackTrace()
            throw ExceptionInInitializerError("FATAL. Deejay failed to boot as it could not open playlist file")
        }
    }

    private fun getPlaylistFileName() : String? {
        val fileName = props.get("playlist.file") as String?
        if(fileName.isNullOrBlank()) {
            logger.error("FATAL. Deejay failed to boot as valid playlist file is not provided")
            throw ExceptionInInitializerError("Deejay failed to boot as Playlist file is not provided")
        }
        logger.info("Playlist file - ${fileName}")
        return fileName
    }

    private fun readEnv(env: Map<String, String>) {
        env.filterKeys { it.startsWith(envPrefix + "_") }
            .mapKeys { envToProp(it.key) }
            .forEach { (k, v) -> props.setProperty(k, v)}
    }

    private fun envToProp(env: String): String {
        return env.drop(envPrefix.length + 1).toLowerCase().replace("_", ".")
    }
}