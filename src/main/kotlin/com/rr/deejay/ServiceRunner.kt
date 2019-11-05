package com.rr.deejay

import com.rr.deejay.controller.ServiceController
import com.rr.deejay.injection.component.DaggerServiceComponent
import com.rr.deejay.injection.component.ServiceComponent
import com.rr.deejay.injection.module.ServiceModule
import org.slf4j.LoggerFactory
import spark.Spark.port
import spark.Spark.staticFiles

class ServiceRunner {

    companion object {
        @JvmStatic lateinit var serviceComponent: ServiceComponent
    }

    init {
        logger = LoggerFactory.getLogger(
                ServiceRunner::class.java
        )
        serviceComponent = DaggerServiceComponent.builder()
            .serviceModule(ServiceModule())
            .build()
    }
    fun run() {
        port(9092)
        staticFiles.location("/public")
        initControllers()
    }
    private fun initControllers() {
        ServiceController()
    }
}