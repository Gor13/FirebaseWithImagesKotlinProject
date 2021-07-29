package com.hardzei.firebasewithimageskotlinproject.di.module

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.repositories.LocationsRepository
import dagger.Module
import dagger.Provides

@Module(includes = [FirebaseModule::class, RoomModule::class])
class RepositoryModule {
    @Provides
    fun provideLocationsRepository(remoteDatabase: MainContract.BaseDatabase): MainContract.BaseRepository {
        return LocationsRepository(remoteDatabase)
    }
}


