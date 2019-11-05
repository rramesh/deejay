package com.rr.deejay.injection.component

import com.google.gson.Gson
import com.rr.deejay.controller.ServiceController
import com.rr.deejay.injection.module.ServiceModule
import com.rr.deejay.service.PlaylistServiceImpl
import com.rr.deejay.so.Playlist
import com.rr.deejay.so.PlaylistFileWriter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class])
interface ServiceComponent {
    fun gson(): Gson
    fun inject(serviceRunner: PlaylistServiceImpl)
    fun inject(serviceController: ServiceController)
    fun inject(playlistFileWriter: PlaylistFileWriter)
    fun inject(playlist: Playlist)
}