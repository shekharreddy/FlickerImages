package com.nsr.flickerimages.hilt

import android.content.Context
import com.nsr.flickerimages.network.ApiHelper
import com.nsr.flickerimages.network.FlickerAPIClient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlickerHiltModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAPIHelper(): ApiHelper {
        return ApiHelper(FlickerAPIClient.apiService)
    }
}