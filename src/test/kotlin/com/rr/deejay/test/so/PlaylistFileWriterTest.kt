package com.rr.deejay.test.so

import com.rr.deejay.ServiceRunner
import com.rr.deejay.so.PlaylistFileWriter
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaylistFileWriterTest {
    lateinit var tempFile :File

    @InjectMockKs
    @MockK
    lateinit var mockPlaylistFileWriter: PlaylistFileWriter

    @BeforeAll
    fun setup() {
        tempFile = createTempFile()
        mockkObject(ServiceRunner)
        every{ ServiceRunner.serviceComponent.inject(any() as PlaylistFileWriter)} just runs
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { mockPlaylistFileWriter getProperty "playlistFile"} returns tempFile
    }

    @Test
    fun `it should write text to file`() {
        val playWriter = spyk(mockPlaylistFileWriter, recordPrivateCalls = true)
        every{ playWriter getProperty "playlistFile"} returns tempFile
        playWriter.write("URL to write")
        val text = tempFile.readText()
        assertEquals("URL to write\n", text)
        verifySequence { playWriter.write("URL to write")}
    }

    @AfterAll
    fun tearDown() {
        tempFile.delete()
        unmockkAll()
    }
}