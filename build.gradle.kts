
plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"

    // kapt plugin
    id("org.jetbrains.kotlin.kapt") version "1.3.50"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    compile("com.sparkjava", "spark-core", "2.8.0")
    compile("com.google.dagger", "dagger", "2.4")
    compile("org.apache.logging.log4j","log4j-core", "2.12.1")
    compile("org.apache.logging.log4j","log4j-slf4j-impl", "2.12.1")
    compile("com.google.guava", "guava", "28.1-jre")
    compile("com.google.code.gson", "gson", "2.8.6")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("io.mockk:mockk:1.9.3")

    kapt("com.google.dagger:dagger-compiler:2.4")
}

application {
    // Define the main class for the application
    mainClassName = "com.rr.deejay.ContainerRunnerKt"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        javaParameters = true
        jvmTarget = "1.8"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "com.rr.deejay.ContainerRunnerKt")
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
}