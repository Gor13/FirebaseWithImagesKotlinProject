package com.hardzei.firebasewithimageskotlinproject.utils

import android.net.Uri
import com.hardzei.firebasewithimageskotlinproject.pojo.Section

sealed class Request
class Read : Request()
class Create(
    val section: Section?,
    val numberOfLocation: Int?,
    val uri: Uri?
) : Request()

class Update(
    val section: Section,
    val numberOfLocation: Int?,
    val newText: String
) : Request()

class Delete(
    val section: Section,
    val numberOfLocation: Int,
    val listWithNumbersOfImages: List<Int>
) : Request()
