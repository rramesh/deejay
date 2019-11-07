package com.rr.deejay.controller

import com.google.gson.Gson
import com.rr.deejay.ServiceRunner
import org.slf4j.MDC
import spark.Spark.*
import java.util.*
import javax.inject.Inject

private const val CORRELATION_ID_KEY = "com.rr.deejay.correlationID"

class ServiceController {
    @Inject
    lateinit var playlistController: PlaylistController

    @Inject
    lateinit var gson: Gson

    init {
        ServiceRunner.serviceComponent.inject(this)
        initRoutes()
    }

    private fun initRoutes() {
        before("*", generateCorrelationID())
        get("/deejay/playlist", { req, res ->
            playlistController.processPlaylistRequest(req, res)
        }, gson::toJson)

        post("/deejay/slackbot", { req, res ->
            playlistController.processSlackCommandRequest(req, res)
        }, gson::toJson)

        post("/deejay/play", { req, res ->
            playlistController.processPlayRequest(req, res)
        }, gson::toJson)
    }

    private fun generateCorrelationID() : String {
        val id = UUID.randomUUID().toString()
        MDC.put(CORRELATION_ID_KEY, id)
        return id
    }
}