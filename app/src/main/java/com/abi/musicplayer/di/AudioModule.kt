package com.abi.musicplayer.di

import android.content.Context
import com.abi.musicplayer.data.repository.AudioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Provides
    @Singleton
    fun provideAudioRepository(
        @ApplicationContext context: Context
    ) : AudioRepository = AudioRepository(context)

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: Context,
        audioEffectsManager: AudioEffectsManager
    ): AudioPlayerManager = AudioPlayerManager(context, audioEffectsManager)

}