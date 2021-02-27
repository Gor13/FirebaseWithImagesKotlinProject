package com.hardzei.firebasewithimageskotlinproject

data class Section(var id: String, var nameOfSection: String, var listWithLocations: List<Location>)
data class Location(var nameOfLication: String, var listWithImages: List<String>)
