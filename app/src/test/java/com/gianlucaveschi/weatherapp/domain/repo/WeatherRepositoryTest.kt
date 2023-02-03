package com.gianlucaveschi.weatherapp.domain.repo

import com.gianlucaveschi.weatherapp.BaseJunitTest
import com.gianlucaveschi.weatherapp.data.remote.OpenMeteoWeatherApi
import com.gianlucaveschi.weatherapp.data.repo.WeatherRepositoryImpl
import com.gianlucaveschi.weatherapp.data.weather.HourlyWeatherData
import com.gianlucaveschi.weatherapp.data.weather.HourlyWeatherUnits
import com.gianlucaveschi.weatherapp.data.weather.WeatherDataApiResponse
import com.gianlucaveschi.weatherapp.domain.weather.WeatherData
import com.gianlucaveschi.weatherapp.domain.weather.WeatherInfo
import com.gianlucaveschi.weatherapp.domain.weather.WeatherType
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalCoroutinesApi
class WeatherRepositoryTest : BaseJunitTest<WeatherRepository>() {

    private val openMeteoWeatherApi: OpenMeteoWeatherApi = mockk()

    override fun initSelf() = WeatherRepositoryImpl(openMeteoWeatherApi)

    @Test
    fun `GIVEN correct coordinates WHEN fetching weather info THEN return correct response`() = runTest {
        val latitude = 41.40338
        val longitude = 2.17403
        coEvery {
            openMeteoWeatherApi.getWeatherData(latitude, longitude)
        } returns mockedWeatherDataApiResponse

        val result = systemUnderTest.getWeatherData(latitude, longitude).data

        assertEquals(mockedWeatherInfo, result)
    }

    private companion object {

        val mockedWeatherData = WeatherData(
            time = LocalDateTime.parse(
                "2023-02-03T18:14:28.545574300",
                DateTimeFormatter.ISO_DATE_TIME
            ),
            temperatureCelsius = 20.0,
            pressure = 1013.25,
            windSpeed = 5.0,
            humidity = 65.0,
            weatherType = WeatherType.fromWMO(1)
        )

        val mockedWeatherInfo = WeatherInfo(
            weatherDataPerDay = mapOf(0 to listOf(mockedWeatherData)),
            currentWeatherData = null
        )

        val mockedHourlyWeatherData = HourlyWeatherData(
            pressure_msl = listOf(1013.25),
            relativehumidity_2m = listOf(65.0),
            temperature_2m = listOf(20.0),
            time = listOf("2023-02-03T18:14:28.545574300"),
            weathercode = listOf(1),
            windspeed_10m = listOf(5.0)
        )

        val mockedHourlyWeatherUnits = HourlyWeatherUnits(
            pressure_msl = "1013.25 hPa",
            relativehumidity_2m = "65%",
            temperature_2m = "20.0°C",
            time = "12:00",
            weathercode = "01d",
            windspeed_10m = "5.0 m/s"
        )

        val mockedWeatherDataApiResponse = WeatherDataApiResponse(
            elevation = 1000.0,
            generationtime_ms = 1.614691200000,
            hourly = mockedHourlyWeatherData,
            hourly_units = mockedHourlyWeatherUnits,
            latitude = 37.7749,
            longitude = -122.4194,
            timezone = "America/Los_Angeles",
            timezone_abbreviation = "PST",
            utc_offset_seconds = -28800
        )
    }
}