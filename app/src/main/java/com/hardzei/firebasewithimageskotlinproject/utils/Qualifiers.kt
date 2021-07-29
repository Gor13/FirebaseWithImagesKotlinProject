package com.hardzei.firebasewithimageskotlinproject.utils

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Local
