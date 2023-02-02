package com.gianlucaveschi.weatherapp.data.mapper

import com.gianlucaveschi.weatherapp.data.weather.WeatherDataApiResponse
import com.gianlucaveschi.weatherapp.domain.model.WeatherData
import com.gianlucaveschi.weatherapp.domain.model.WeatherInfo
import com.gianlucaveschi.weatherapp.domain.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedGenWeatherData(
    val index: Int,
    val data: WeatherData
)

/**
 * Returns a map containing weather data values where each key represents a day of the week
 * */
fun WeatherDataApiResponse.toWeatherInfo(): WeatherInfo {
    val weatherDataMap: Map<Int, List<WeatherData>> = this.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentDay = 0
    // We only have data for each specific hour so we approximate
    // 13:20 -> weather at 13:00
    // 13.45 -> weather at 14:00
    val currentWeatherData = weatherDataMap[currentDay]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}

private fun WeatherDataApiResponse.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return this.hourly.run {
        time.mapIndexed { index, time ->
            val temperature = temperature_2m[index]
            val weatherCode = weathercode[index]
            val windSpeed = windspeed_10m[index]
            val pressure = pressure_msl[index]
            val humidity = relativehumidity_2m[index]
            IndexedGenWeatherData(
                index = index,
                data = WeatherData(
                    time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                    temperatureCelsius = temperature,
                    pressure = pressure,
                    windSpeed = windSpeed,
                    humidity = humidity,
                    weatherType = WeatherType.fromWMO(weatherCode)
                )
            )
        }
            // The time array contains 7 x 24 = 168 entries.
            // To get the exact day we divide the index by 24, so i.e.
            // index = 13 -> 13/24 = 0 -> Today (0..24)
            // index = 36 -> 36/24 = 1.5 -> Tomorrow (25..48)
            // index = 51 -> 51/24 = 2.1 -> The day after tomorrow (49..72)
            .groupBy {
                it.index / 24
            }.mapValues {
                it.value.map { indexedWeatherData ->
                    indexedWeatherData.data
                }
            }
    }
}
