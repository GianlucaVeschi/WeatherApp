package com.gianlucaveschi.weatherapp.data.util

object NetworkService {
    const val BASE_URL: String = "https://api.open-meteo.com/"
    const val WEATHER_ENDPOINT = "v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
}

object AppStrings {

    // HomeScreen -> ForecastSection
    const val hourly_forecast = "Hourly Forecast"
    const val daily_forecast = "Daily Forecast"

    // HomeScreen -> WeatherDetailSection
    const val temp = "🌡 TEMP"
    const val feels_like = "🌡 FEELS LIKE"
    const val cloudiness = "☁ CLOUDINESS"
    const val humidity = "💧 HUMIDITY"
    const val sunrise = "🌇 SUNRISE"
    const val sunset = "🌆 SUNSET"
    const val wind = "🌬 WIND"
    const val metric = "KM"
    const val pressure = "⏲ PRESSURE"
    const val degree = "°"

    // SearchCityScreen
    const val topbar_title = "Weather"

    // SearchCityScreen -> SearchField
    const val placeholder = "Search for a city"

    // SearchCityScreen -> CityWeatherList
    const val subtitle1 = "My Cities"
    const val subtitle2 = "Search Result"
    const val no_city = "You don't have any city"

    // SearchCityScreen -> SearchCityScreenContent
    const val error_title = "OOOOPS!!!"
}