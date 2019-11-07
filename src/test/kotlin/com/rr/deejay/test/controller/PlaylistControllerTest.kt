package com.rr.deejay.test.controller

import com.rr.deejay.ServiceRunner
import com.rr.deejay.controller.PlaylistController
import com.rr.deejay.service.PlaylistService
import com.rr.deejay.so.randomButterMessage
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import spark.Request
import spark.Response
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaylistControllerTest {
    @MockK
    private lateinit var service: PlaylistService

    @InjectMockKs
    lateinit var mockPlaylistController: PlaylistController

    @BeforeAll
    fun setup() {
        mockkObject(ServiceRunner)
        every{ ServiceRunner.serviceComponent.inject(any() as PlaylistController)} just runs
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("com.rr.deejay.so.ButterMessageKt")
        every{ randomButterMessage()} returns "Utterly buttery"
    }

    @Test
    fun `it should process slack command request`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        val userName = "Raama"
        val url = "A Youtube URL"

        every{ mockRequest.queryParams("user_name")} returns userName
        every{ mockRequest.queryParams("youtubeUrl")} returns url
        every{ mockResponse.type("application/json")} just runs
        every{ service.addToPlaylist(userName, url)} returns true

        val expected = mutableMapOf(
                "response_type" to "in_channel",
                "text" to "*Utterly buttery. Thank you ${userName}. I have added the song to the playlist.*\n" +
                        "<${url}>"
        )

        val actual = mockPlaylistController.processSlackCommandRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @Test
    fun `it should default user name to Unknown if not passed when processing slack command request`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        val userName = "Unknown"
        val url = "A Youtube URL"

        every{ mockRequest.queryParams("user_name")} returns null
        every{ mockRequest.queryParams("youtubeUrl")} returns url
        every{ mockResponse.type("application/json")} just runs
        every{ service.addToPlaylist(userName, url)} returns true

        val expected = mutableMapOf(
                "response_type" to "in_channel",
                "text" to "*Utterly buttery. Thank you ${userName}. I have added the song to the playlist.*\n" +
                        "<${url}>"
        )
        val actual = mockPlaylistController.processSlackCommandRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @Test
    fun `it should return error if valied URL is not passed when processing slack command request`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        val userName = "Raama"

        every{ mockRequest.queryParams("user_name")} returns userName
        every{ mockRequest.queryParams("youtubeUrl")} returns null
        every{ mockResponse.type("application/json")} just runs

        val expected = mutableMapOf(
                "response_type" to "ephemeral",
                "text" to "Error, you didn't provide a valid youtube URL."
        )
        val actual = mockPlaylistController.processSlackCommandRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @Test
    fun `it should return success string message when processing play request`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        val userName = "Raama"
        val url = "A Youtube URL"

        every{ mockRequest.queryParams("user_name")} returns userName
        every{ mockRequest.queryParams("youtubeUrl")} returns url
        every{ mockResponse.type("text/plain")} just runs
        every{ service.addToPlaylist(userName, url)} returns true

        val expected = "Utterly buttery. Thank you ${userName}. I have added the song to the playlist. ${url}"

        val actual = mockPlaylistController.processPlayRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @Test
    fun `it should default user to Unknown and return success string message when processing play request`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        val userName = "Unknown"
        val url = "A Youtube URL"

        every{ mockRequest.queryParams("user_name")} returns null
        every{ mockRequest.queryParams("youtubeUrl")} returns url
        every{ mockResponse.type("text/plain")} just runs
        every{ service.addToPlaylist(userName, url)} returns true

        val expected = "Utterly buttery. Thank you ${userName}. I have added the song to the playlist. ${url}"

        val actual = mockPlaylistController.processPlayRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @Test
    fun `it should return error message if url is not provided when processing play request`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        val userName = "Raama"

        every{ mockRequest.queryParams("user_name")} returns userName
        every{ mockRequest.queryParams("youtubeUrl")} returns null
        every{ mockResponse.type("text/plain")} just runs

        val expected = "Error, you didn't provide a valid youtube URL."

        val actual = mockPlaylistController.processPlayRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @Test
    fun `it should return the current playlist`() {
        val mockRequest = mockk<Request>()
        val mockResponse = mockk<Response>()
        every{ mockResponse.type("application/json")} just runs
        val expected : List<Map<String, String>> = listOf(
                mutableMapOf("Raama" to "Rama's song URL"),
                mutableMapOf("Arjuna" to "Arjuna's Rap Song URL")
        )
        every {service.currentPlaylist()} returns expected
        val actual = mockPlaylistController.processPlaylistRequest(mockRequest, mockResponse)
        assertEquals(expected, actual)
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }
}