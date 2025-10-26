package com.example.weatherapp.ui.model

data class weatherModel(
    val city: String = "",
    val dateTime: Int = 0,
    val updatedTime: String = "",
    val icon: String = "",
    val temperature: Double = 0.0,
    val weatherCondition: String = "",
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val feelsLike: Double = 0.0,
    val rainFall: Double ? = null,
    val pressure: Int = 0,
    val cloud: Int = 0,
    val sunriseTime: Int = 0,
    val sunsetTime: Int = 0,
    val isError: Boolean = false,
    val errorMsg: String = ""
)