package com.rr.deejay.controller

import com.google.gson.Gson
import com.rr.deejay.ServiceRunner
import com.rr.deejay.buttery
import com.rr.deejay.service.PlaylistService
import spark.Spark.get
import spark.Spark.post
import javax.inject.Inject

class ServiceController {
    @Inject
    lateinit var service: PlaylistService
    @Inject
    lateinit var gson: Gson

    init {
        ServiceRunner.serviceComponent.inject(this)
        initRoutes()
    }

    private fun initRoutes() {
        get("/deejay/playlist", { _, res ->
            res.type("application/json")
            service.currentPlaylist()
        }, gson::toJson)

        post("/deejay/slackbot", { req, res ->
            res.type("application/json")
            var name = req.queryParams("user_name")
            var url = req.queryParams("text")
            if (name.isNullOrBlank()) name = "Unknown"
            var responseType = "ephemeral"
            var content: String
            if (url.isNullOrBlank()) {
                content = "Error, either you didn't provide your name or youtube URL"
            } else {
                url = url.replace("<", "").replace(">", "")
                service.addToPlaylist(name = name, youtubeUrl = url)
                responseType = "in_channel"
                content = butterMessage(name, url)
            }
            slackResponse(responseType, content)
        }, gson::toJson)

        post("/deejay/play", { req, res ->
            res.type("text/plain")
            val name = req.queryParams("name")
            val url = req.queryParams("youtubeUrl")
            if (name.isNullOrBlank() || url.isNullOrBlank()) {
                "Error, either you didn't provide your name or youtube URL"
            } else {
                service.addToPlaylist(name = name, youtubeUrl = url)
                butterMessage(name, url, false)
            }
        }, gson::toJson)
    }

    private fun butterMessage(name: String, url: String, markdown: Boolean = true) : String{
        val butteryIndex = (buttery.indices).random()
        return if (markdown)
            "*${buttery[butteryIndex]}. Thank you ${name}. I have added the song to the playlist*\n<${url}>"
        else
            "${buttery[butteryIndex]}. Thank you ${name}. I have added the song to the playlist. ${url}"
    }

    private fun slackResponse(responseType: String, content: String) : Map<String, Any> {
        return mutableMapOf(
                "response_type" to responseType,
                "text" to content
        )
    }
}