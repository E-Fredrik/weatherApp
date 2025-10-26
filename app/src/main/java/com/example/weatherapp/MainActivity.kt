package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.weatherapp.data.container.AppContainer
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.view.WeatherView
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val appContainer = AppContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = WeatherViewModel(appContainer.weatherRepository)
                    WeatherView(
                        modifier = Modifier.padding(),
                        viewModel = viewModel
                    )
//                }
            }
        }
    }
}
