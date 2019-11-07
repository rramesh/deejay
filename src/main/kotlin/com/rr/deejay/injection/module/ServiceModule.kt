package com.rr.deejay.injection.module

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rr.deejay.configuration
import com.rr.deejay.controller.PlaylistController
import com.rr.deejay.service.PlaylistService
import com.rr.deejay.service.PlaylistServiceImpl
import com.rr.deejay.so.Playlist
import com.rr.deejay.so.PlaylistFileWriter
import com.rr.deejay.so.PlaylistWriter
import com.sun.org.apache.xerces.internal.util.PropertyState
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides @Singleton fun providePlaylistFile(): File {
        return configuration.getPlaylistFile()
    }

    @Provides @Singleton fun providePlaylistWriter() : PlaylistWriter {
        return PlaylistFileWriter()
    }

    @Provides @Singleton fun providePlaylist(): Playlist {
        return Playlist()
    }

    @Provides @Singleton fun provideInternalService(): PlaylistService {
        return PlaylistServiceImpl()
    }

    @Provides @Singleton fun providePlaylistController() : PlaylistController {
        return PlaylistController()
    }

    @Provides @Singleton fun gson(): Gson {
        return GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy{
            override fun shouldSkipClass(clazz: Class<*>?): Boolean = clazz == PropertyState::class.java
            override fun shouldSkipField(f: FieldAttributes?): Boolean = false
        }).create()
    }
}