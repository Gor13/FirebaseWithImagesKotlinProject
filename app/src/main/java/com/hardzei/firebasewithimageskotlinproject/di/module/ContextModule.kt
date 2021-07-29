package com.hardzei.firebasewithimageskotlinproject.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule constructor(private val context: Context) {
    @Provides
    fun provideContext() = context
}
