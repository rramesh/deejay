package com.rr.deejay.so

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

fun randomButterMessage() : String {
    val butteryIndex = (buttery.indices).random()
    return buttery[butteryIndex]
}