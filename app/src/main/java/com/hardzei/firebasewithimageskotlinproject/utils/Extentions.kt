package com.hardzei.firebasewithimageskotlinproject.utils

import com.hardzei.firebasewithimageskotlinproject.pojo.Location

fun List<Location>.changeLocationName(
    numberOfLocation: Int,
    changedText: String
): List<Location> {
    val listLocations = this.toMutableList()
    listLocations[numberOfLocation].nameOfLication = changedText
    return listLocations.toList()
}

fun List<Location>.deleteSelectedImages(
    numberOfLocation: Int,
    listWithNumbersOfImages: List<Int>
): List<Location> {
    val listLocations = this.toMutableList()
    val listImages = this[numberOfLocation].listWithImages.toMutableList()
    for (number in listWithNumbersOfImages.sorted().reversed()) {
        listImages.removeAt(number)
    }
    listLocations[numberOfLocation].listWithImages = listImages
    return listLocations.toList()
}

fun List<Location>.addnewImage(
    numberOfLocation: Int,
    referenceWithCurentImage: String
): List<Location> {
    val listLocations = this.toMutableList()
    val listImages = this[numberOfLocation].listWithImages.toMutableList()
    listImages.add(referenceWithCurentImage)
    listLocations[numberOfLocation].listWithImages = listImages
    return listLocations.toList()
}

fun List<Location>.addnewLocation(): List<Location> {
    val list = this.toMutableList()
    list.add(Location("Name of location", listOf()))
    return list.toList()
}
