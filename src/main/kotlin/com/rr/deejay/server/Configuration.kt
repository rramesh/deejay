package com.rr.deejay.server

import com.google.common.io.Resources.getResource
import com.rr.deejay.logger
import org.apache.logging.log4j.Level.toLevel
import org.apache.logging.log4j.core.config.Configurator
import java.lang.System.getenv
import java.util.*
import javax.inject.Singleton

@Singleton
class Configuration(
    resourceFile: String = "application.properties",
    private val envPrefix: String = "DEEJAY",
    env: Map<String, String> = getenv()
) {
    private val props = Properties()
    init {
        try{
            getResource(resourceFile).openStream().use(props::load)
        } catch(ex: IllegalArgumentException) {
            // [getResource] throws this exception when resource is not found.
            // It can be ignored in this case, as config values may be provided from other places.
        }

        readEnv(env)
        props.getProperty("log.level")?.let {
            Configurator.setLevel("com.rr", toLevel(it))
        }
    }
    fun getProperties() : Properties {
        return props
    }

    fun getPlaylistFileName() : String? {
        var fileName = System.getProperty("deejay.playlist.file")
        logger.info("Parameter Property passed - ${fileName}")
        if (!fileName.isNullOrBlank()) return fileName
        fileName = props.get("deejay.playlist.file") as String?
        logger.info("Parameter Property picked from Environment variable - ${fileName}")
        if (!fileName.isNullOrBlank()) return fileName
        fileName = props.get("playlist.file") as String?
        logger.info("Parameter Property picked from application.properties file - ${fileName}")
        if(fileName.isNullOrBlank()) {
            logger.error("Deejay failed to create playlist as valid playlist file is not provided")
            throw IllegalArgumentException("Deejay could not work as Playlist file is not provided")
        }
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