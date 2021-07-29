package com.hardzei.firebasewithimageskotlinproject.db

import com.hardzei.firebasewithimageskotlinproject.pojo.Section

sealed class Result
class Success(val listWithSections: List<Section>) : Result()
class Faild(val errorMessage: String) : Result()
