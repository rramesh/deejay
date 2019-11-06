package com.rr.deejay.test.server

import com.rr.deejay.logger
import com.rr.deejay.server.Configuration
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigurationTest {
    @BeforeAll
    fun setup() {
        logger = LoggerFactory.getLogger(ConfigurationTest::class.java)
    }

    @Test
    fun `it should pick up playlist file name from property file`() {
        val configuration = Configuration(
                env = mutableMapOf<String, String>(),
                resourceFile = "test.properties"
        )
        val playFile = configuration.getPlaylistFile()
        assertEquals("/tmp/deejay_test.txt", playFile.absolutePath)
        playFile.delete()
    }

    @Test
    fun `it should pick up playlist file name from Environment variable`() {
        val configuration = Configuration(
                env = mutableMapOf("DEEJAY_PLAYLIST_FILE" to "/tmp/Doojoo.properties"),
                resourceFile = "test.properties"
        )
        val playFile = configuration.getPlaylistFile()
        assertEquals("/tmp/Doojoo.properties", playFile.absolutePath)
        playFile.delete()
    }

    @Test
    fun `it should throw exception when playlist file name cannot be determined`() {
        assertThrows<ExceptionInInitializerError> {
            Configuration(
                    env = mutableMapOf(),
                    resourceFile = "nonexistent_file.properties"
            )
        }
    }

    @Test
    fun `it should return port number from property file`() {
        val configuration = Configuration(
                env = mutableMapOf<String, String>(),
                resourceFile = "test.properties"
        )
        val playFile = configuration.getPlaylistFile()
        val port = configuration.getServicePort()
        assertEquals(8764, port)
        playFile.delete()
    }

    @Test
    fun `it should return port number from environment variable`() {
        val configuration = Configuration(
                env = mutableMapOf<String, String>("DEEJAY_SERVICE_PORT" to "4455"),
                resourceFile = "test.properties"
        )
        val playFile = configuration.getPlaylistFile()
        val port = configuration.getServicePort()
        assertEquals(4455, port)
        playFile.delete()
    }

    @Test
    fun `it should default to 9092 if service port contains non-numeric port number`() {
        val configuration = Configuration(
                env = mutableMapOf<String, String>("DEEJAY_SERVICE_PORT" to "532GO"),
                resourceFile = "test.properties"
        )
        val playFile = configuration.getPlaylistFile()
        val port = configuration.getServicePort()
        assertEquals(9092, port)
        playFile.delete()
    }
}