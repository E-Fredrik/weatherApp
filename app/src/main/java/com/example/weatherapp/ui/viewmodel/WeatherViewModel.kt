package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.data.container.AppContainer
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.model.weatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: weatherModel) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _weather = MutableStateFlow(weatherModel())
    val weather: StateFlow<weatherModel> = _weather.asStateFlow()


    private val _weatherConditionIcon = MutableStateFlow<String?>(null)
    val weatherConditionIcon: StateFlow<String?> = _weatherConditionIcon.asStateFlow()

    fun fetchWeather(city: String) {
        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val weather = repository.getWeather(city)
                _weather.value = weather
                _uiState.value = WeatherUiState.Success(weather)
                _weatherConditionIcon.value = repository.getWeatherIconUrl(weather.icon)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "HTTP 404 Not Found")
            }
        }
    }

    fun formattedUpdatedTime(): String {
        val updated = _weather.value.updatedTime
        val timePattern = SimpleDateFormat("h:mm a", Locale.getDefault())
        return try {
            val epoch = updated.toLong()
            val date = if (epoch > 1_000_000_000_000L) Date(epoch) else Date(epoch * 1000L)
            timePattern.format(date)
        } catch (e: Exception) {
            val tried = listOf(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss"
            )
            var parsed: Date? = null
            for (fmt in tried) {
                try {
                    parsed = SimpleDateFormat(fmt, Locale.getDefault()).parse(updated)
                    if (parsed != null) break
                } catch (_: Exception) {}
            }
            if (parsed != null) timePattern.format(parsed) else updated
        }
    }


    fun formatTime(epochSeconds: Int): String {
        val timePattern = SimpleDateFormat("h:mm a", Locale.getDefault())
        return try {
            val epoch = epochSeconds.toLong()
            val date = if (epoch > 1_000_000_000_000L) Date(epoch) else Date(epoch * 1000L)
            timePattern.format(date)
        } catch (e: Exception) {
            ""
        }
    }



    val weatherDetails = weather.map {
        listOf(
            Triple("HUMIDITY", "${it.humidity}%", R.drawable.icon_humidity),
            Triple("WIND", "${it.windSpeed} km/h", R.drawable.icon_wind),
            Triple("FEELS LIKE", "${it.feelsLike}°C", R.drawable.icon_feels_like),
            Triple("RAIN FALL", "${it.rainFall ?: 0} mm", R.drawable.vector_2),
            Triple("PRESSURE", "${it.pressure} hPa", R.drawable.devices),
            Triple("CLOUDS", "${it.cloud}%", R.drawable.cloud)
        )
    }

    val sunDetails = weather.map {
        listOf(
            Triple("SUNRISE", it.sunriseTime, R.drawable.vector),
            Triple("SUNSET", it.sunsetTime, R.drawable.vector_21png)
        )
    }
}