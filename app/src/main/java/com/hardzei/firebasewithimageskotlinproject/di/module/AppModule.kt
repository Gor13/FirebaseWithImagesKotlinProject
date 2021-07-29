package com.hardzei.firebasewithimageskotlinproject.di.module

import android.app.Application
import com.hardzei.firebasewithimageskotlinproject.InitApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule constructor(private val initApplication: InitApplication) {
    @Provides
    @Singleton
    fun provideApplication(): Application = initApplication
}
