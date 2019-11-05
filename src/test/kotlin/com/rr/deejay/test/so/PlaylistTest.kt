package com.rr.deejay.test.so

import com.rr.deejay.ServiceRunner
import com.rr.deejay.so.Playlist
import com.rr.deejay.so.PlaylistWriter
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaylistTest {
    @MockK
    lateinit var mockPlaylistFileWriter: PlaylistWriter

    @InjectMockKs
    lateinit var mockPlaylist: Playlist

    @BeforeAll
    fun setup() {
        mockkObject(ServiceRunner)
        every{ServiceRunner.serviceComponent.inject(any() as Playlist)} just runs
        MockKAnnotations.init(this, relaxUnitFun = true)
        every{mockPlaylistFileWriter.write(any() as String)} just runs
    }

    @Test
    fun `it should add to the playlist`() {
        val playlist = spyk(mockPlaylist, recordPrivateCalls = true)
        playlist.truncateAll()
        playlist.append("A Guy","Audio URL")
        val list: List<Map<String, String>> = playlist.list()
        assertEquals(list.last(), mutableMapOf("A Guy" to "Audio URL"))
        verifySequence {
            playlist.truncateAll()
            playlist.append("A Guy", "Audio URL")
            playlist.list()
        }
    }

    @Test
    fun `it should append to existing playlist`() {
        val playlist = spyk(mockPlaylist, recordPrivateCalls = true)
        playlist.truncateAll()
        playlist.append("First Guy", "First URL")
        playlist.append("A Guy","Audio URL")
        val list: List<Map<String, String>> = playlist.list()
        assertEquals(list.last(), mutableMapOf("A Guy" to "Audio URL"))
        verifySequence {
            playlist.truncateAll()
            playlist.append("First Guy", "First URL")
            playlist.append("A Guy", "Audio URL")
            playlist.list()
        }
    }

    @Test
    fun `it should list existing playlist`() {
        val playlist = spyk(mockPlaylist, recordPrivateCalls = true)
        playlist.truncateAll()
        playlist.append("First Guy", "First URL")
        playlist.append("A Guy","Audio URL")
        val expectedList = listOf<Map<String, String>>(
                mutableMapOf("First Guy" to "First URL"),
                mutableMapOf("A Guy" to "Audio URL")
        )
        val list: List<Map<String, String>> = playlist.list()
        assertEquals(expectedList, list)
        verifySequence {
            playlist.truncateAll()
            playlist.append("First Guy", "First URL")
            playlist.append("A Guy", "Audio URL")
            playlist.list()
        }
    }

    @Test
    fun `it should empty playlist`() {
        val playlist = spyk(mockPlaylist, recordPrivateCalls = true)
        playlist.append("First Guy", "First URL")
        playlist.append("A Guy","Audio URL")
        playlist.truncateAll()
        val list: List<Map<String, String>> = playlist.list()
        assertTrue(list.isEmpty())
        verifySequence {
            playlist.append("First Guy", "First URL")
            playlist.append("A Guy", "Audio URL")
            playlist.truncateAll()
            playlist.list()
        }
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }
}