package com.hardzei.firebasewithimageskotlinproject

import android.app.Application
import android.content.Context
import com.hardzei.firebasewithimageskotlinproject.di.component.AppComponent
import com.hardzei.firebasewithimageskotlinproject.di.component.DaggerAppComponent
import com.hardzei.firebasewithimageskotlinproject.di.module.ContextModule
import com.hardzei.firebasewithimageskotlinproject.di.module.DataModule

class InitApplication : Application() {
    private lateinit var component: AppComponent

    operator fun get(context: Context): InitApplication {
        return context.applicationContext as InitApplication
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .dataModule(DataModule())
            //  .roomModule(RoomModule(this))
            .build()
    }

    fun component(): AppComponent {
        return component
    }
}
