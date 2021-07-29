package com.hardzei.firebasewithimageskotlinproject.di.module

import com.hardzei.firebasewithimageskotlinproject.MainContract
import com.hardzei.firebasewithimageskotlinproject.db.remote.FirebaseDatabase
import com.hardzei.firebasewithimageskotlinproject.utils.Remote
import dagger.Module
import dagger.Provides

@Module
@Remote
class FirebaseModule {
    @Provides
    fun provideFirebaseDatabase(): MainContract.BaseDatabase {
        return FirebaseDatabase()
    }
}
