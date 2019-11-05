package com.rr.deejay.test.service

import com.rr.deejay.ServiceRunner
import com.rr.deejay.logger
import com.rr.deejay.service.PlaylistServiceImpl
import com.rr.deejay.so.Playlist
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaylistServiceImplTest {
    @MockK
    private lateinit var playlist: Playlist

    @InjectMockKs
    lateinit var mockPlaylistServiceImpl: PlaylistServiceImpl

    @BeforeAll
    fun setup() {
        logger = LoggerFactory.getLogger(PlaylistServiceImpl::class.java)
        mockkObject(ServiceRunner)
        every{ ServiceRunner.serviceComponent.inject(any() as PlaylistServiceImpl)} just runs
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { playlist.append(any() as String, any() as String) } just runs
    }

    @Test
    fun `it should add to the playlist`() {
        assertTrue(mockPlaylistServiceImpl.addToPlaylist("First Guy", "First Record"))
    }

    @Test
    fun `it should return current playlist`() {
        val expected : List<Map<String, String>> = listOf(
                mutableMapOf("First Guy" to "First Song"),
                mutableMapOf("Second Guy" to "Second Song"),
                mutableMapOf("Third Guy" to "Third Song")
        )
        every { playlist.list() } returns expected
        val actual = mockPlaylistServiceImpl.currentPlaylist()
        assertEquals(expected, actual)
    }
}