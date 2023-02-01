package com.gianlucaveschi.weatherapp.presentation.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@Composable
fun WeatherForecast(
    state: WeatherState,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.weatherDataPerDay?.let { data ->
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.LightGray,
            modifier = modifier.padding(16.dp)
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                for (i in data.keys) {
                    data[i]?.let { weatherDataList ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Day ${
                                weatherDataList[i].time.format(
                                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                )
                            }",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = Color.DarkGray
                        ) {
                            LazyRow(content = {
                                items(weatherDataList) {
                                    HourlyWeatherDisplay(
                                        weatherData = it,
                                        modifier = Modifier
                                            .height(100.dp)
                                            .padding(horizontal = 16.dp)
                                    )
                                }
                            })
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}