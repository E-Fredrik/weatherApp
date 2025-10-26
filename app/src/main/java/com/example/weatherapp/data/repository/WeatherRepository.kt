package com.example.weatherapp.data.repository

import com.example.weatherapp.data.dto.WeatherResponse
import com.example.weatherapp.data.service.WeatherApiService
import com.example.weatherapp.ui.model.weatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val service: WeatherApiService) {
    suspend fun getWeather(city: String): weatherModel {
        val weathers = service.getWeather(
            city = city,
            units = "metric",
            apiKey = "e717489766f05e383533979eb3237a08"
        ).body()!!
        return weatherModel(
            city = weathers.name,
            dateTime = weathers.dt,
            updatedTime = weathers.dt.toString(),
            icon = weathers.weather[0].icon,
            temperature = weathers.main.temp,
            weatherCondition = weathers.weather[0].main,
            humidity = weathers.main.humidity,
            windSpeed = weathers.wind.speed,
            feelsLike = weathers.main.feels_like,
            rainFall = weathers.rain?.`1h` ?: 0.0,
            pressure = weathers.main.pressure,
            cloud = weathers.clouds.all,
            sunriseTime = weathers.sys.sunrise,
            sunsetTime = weathers.sys.sunset
        )
    }

    fun getWeatherIconUrl(iconId: String): String {
        val url ="https://openweathermap.org/img/wn/$iconId@2x.png"
        return url
    }
}
