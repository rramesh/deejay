package com.rr.deejay

import com.rr.deejay.server.Configuration
import org.slf4j.Logger

lateinit var logger: Logger
lateinit var configuration: Configuration
fun main(args: Array<String>) {
    ServiceRunner().run()
}