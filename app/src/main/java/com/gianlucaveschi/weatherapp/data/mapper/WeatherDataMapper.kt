package com.gianlucaveschi.weatherapp.data.mapper

import com.gianlucaveschi.weatherapp.data.model.WeatherApiModel
import com.gianlucaveschi.weatherapp.data.model.WeatherDataApiModel
import com.gianlucaveschi.weatherapp.domain.model.WeatherData
import com.gianlucaveschi.weatherapp.domain.model.WeatherInfo
import com.gianlucaveschi.weatherapp.domain.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

/**
 * Returns a map containing weather data values where each key represents a day of the week
 * */
fun WeatherDataApiModel.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        IndexedWeatherData(
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

fun WeatherApiModel.toWeatherInfo(): WeatherInfo {
    val weatherDataMap: Map<Int, List<WeatherData>> = weatherData.toWeatherDataMap()
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