package com.rr.deejay.controller

import com.rr.deejay.ServiceRunner
import com.rr.deejay.service.PlaylistService
import com.rr.deejay.so.randomButterMessage
import spark.Request
import spark.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistController {
    @Inject
    lateinit var service: PlaylistService

    init {
        ServiceRunner.serviceComponent.inject(this)
    }

    fun processSlackCommandRequest(request: Request, response: Response) : Map<String, Any> {
        response.type("application/json")
        var (name, url) = requestParams(request)
        var responseType = "ephemeral"
        var content: String
        if (url.isNullOrBlank()) {
            content = "Error, you didn't provide a valid youtube URL."
        } else {
            url = url.replace("<", "").replace(">", "")
            service.addToPlaylist(name = name, youtubeUrl = url)
            responseType = "in_channel"
            content = butterMessage(name, url)
        }
        return slackResponse(responseType, content)
    }

    fun processPlayRequest(request: Request, response: Response) : String {
        response.type("text/plain")
        var (name, url) = requestParams(request)
        if (url.isNullOrBlank()) {
            return "Error, you didn't provide a valid youtube URL."
        } else {
            service.addToPlaylist(name = name, youtubeUrl = url)
        }
        return butterMessage(name, url, false)
    }

    fun processPlaylistRequest(request: Request, response: Response) : List<Map<String, String>> {
        response.type("application/json")
        return service.currentPlaylist()
    }

    private fun requestParams(request: Request) : Pair<String, String> {
        var name = request.queryParams("user_name")
        val url = request.queryParams("text")
        if (name.isNullOrBlank()) name = "Unknown"
        return Pair(name, url)
    }

    private fun slackResponse(responseType: String, content: String) : Map<String, Any> {
        return mutableMapOf(
                "response_type" to responseType,
                "text" to content
        )
    }

    private fun butterMessage(name: String, url: String, markdown: Boolean = true) : String{
        return if (markdown)
            "*${randomButterMessage()}. Thank you ${name}. I have added the song to the playlist.*\n<${url}>"
        else
            "${randomButterMessage()}. Thank you ${name}. I have added the song to the playlist. ${url}"
    }

}