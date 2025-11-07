package com.example.weatherly.network

import com.example.weatherly.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for OpenWeatherMap API
 * Defines the API endpoint for fetching weather data
 */
interface ApiInterface {
    
    /**
     * Get current weather data for a city
     * @param cityName Name of the city to get weather for
     * @param apiKey Your OpenWeatherMap API key
     * @param units Temperature units (metric for Celsius)
     * @return WeatherResponse containing weather data
     */
    @GET("weather")
    suspend fun getWeatherData(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"  // Use metric for Celsius
    ): WeatherResponse
}

