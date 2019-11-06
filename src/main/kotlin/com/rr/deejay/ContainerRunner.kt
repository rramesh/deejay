package com.rr.deejay

import com.rr.deejay.server.Configuration
import org.slf4j.Logger

val buttery:List<String> = listOf<String>(
        "Awesome Song",
        "Lovely Song",
        "I Like your taste",
        "Nice Song",
        "Oooh that's an interesting Song",
        ":heart_eyes:",
        "Beautiful Song",
        "Hey, I like that song too",
        "Mesmerizing Song",
        "This song can be on repeat mode isn't it? :hugging_face:"
)

lateinit var logger: Logger
lateinit var configuration: Configuration
fun main(args: Array<String>) {
    ServiceRunner().run()
}