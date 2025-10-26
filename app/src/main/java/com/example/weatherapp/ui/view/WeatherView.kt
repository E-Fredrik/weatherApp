package com.example.weatherapp.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.ui.model.weatherModel
import com.example.weatherapp.ui.viewmodel.WeatherUiState
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.times


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherView(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }


    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.weather___home_2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = cityInput,
                    onValueChange = { cityInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter city name...", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (cityInput.isNotBlank()) {
                            viewModel.fetchWeather(cityInput)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Search", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            when (val state = uiState) {
                is WeatherUiState.Idle -> {
                    IdleState()
                }
                is WeatherUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is WeatherUiState.Success -> {
                    WeatherDetails(
                        weather = state.weather,
                        viewModel = viewModel
                    )
                }
                is WeatherUiState.Error -> {
                    ErrorView(state.message)
                }
            }
        }
    }
}

@Composable
fun ErrorView(error: String? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error Icon",
            modifier = Modifier.size(80.dp),
            tint = Color.Red.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Oops! Something went wrong.",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = error ?: "Unknown error occurred.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun IdleState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_search),
            contentDescription = "Search icon",
            modifier = Modifier.size(80.dp),
            tint = Color.White.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for a city to get started",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 18.sp
        )
    }
}

@Composable
fun WeatherDetails(
    weather: weatherModel,
    viewModel: WeatherViewModel
) {
    val weatherDetails by viewModel.weatherDetails.collectAsState(initial = emptyList())
    val weatherConditionIcon by viewModel.weatherConditionIcon.collectAsState()
    LazyColumn (

        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = weather.city,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val date = Date(weather.dateTime * 1000L)
            val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
            Text(
                text = dateFormat.format(date),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Updated as of ${viewModel.formattedUpdatedTime()}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(125.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Icon(
                        painter = rememberAsyncImagePainter(model = weatherConditionIcon),
                        contentDescription = "${weather.weatherCondition} icon",
                        modifier = Modifier.size(80.dp),
                        tint =
                           when (weather.weatherCondition) {
                               "Clear" -> Color(0xFFDB7555)
                               else -> Color.White
                           }
                    )
                    Text(
                        text = weather.weatherCondition,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${weather.temperature.toInt()}°C",
                        color = Color.White,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Image(
                    painter = painterResource(
                        when (weather.weatherCondition) {
                            "Clear" -> R.drawable.blue_and_black_bold_typography_quote_poster_3
                            "Rain" -> R.drawable.blue_and_black_bold_typography_quote_poster_2
                            "Clouds" -> R.drawable.blue_and_black_bold_typography_quote_poster
                            else -> 0
                        }
                    ),
                    contentDescription = "Weather Illustration",
                    modifier = Modifier.size(150.dp),
                )
            }

            Spacer(modifier = Modifier.height(200.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height((weatherDetails.size + 3 / 3 - 1) / 3 * 130.dp)
            ) {
                items(weatherDetails) {
                    WeatherMetricCard(
                        label = it.first,
                        value = it.second,
                        iconRepresentation = it.third
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SunCardView(
                    title = "SUNRISE",
                    value = viewModel.formatTime(weather.sunriseTime),
                    iconRes = R.drawable.vector
                )
                SunCardView(
                    title = "SUNSET",
                    value = viewModel.formatTime(weather.sunsetTime),
                    iconRes = R.drawable.vector_21png
                )
            }
        }
    }
}
