package com.example.weatherly.model

/**
 * Data classes to map OpenWeatherMap API JSON response
 * These classes represent the structure of weather data returned by the API
 */

// Main weather response containing all weather information
data class WeatherResponse(
    val name: String,              // City name
    val main: Main,                // Main weather data (temp, humidity, pressure)
    val weather: List<Weather>,    // Weather conditions (description, icon)
    val wind: Wind                 // Wind information (speed)
)

// Main weather data
data class Main(
    val temp: Double,              // Temperature in Kelvin (we'll convert to Celsius)
    val humidity: Int,             // Humidity percentage
    val pressure: Int               // Atmospheric pressure in hPa
)

// Weather condition details
data class Weather(
    val main: String,              // Main weather condition (e.g., "Clear", "Clouds")
    val description: String,        // Detailed description (e.g., "clear sky")
    val icon: String                // Icon code for weather image
)

// Wind information
data class Wind(
    val speed: Double               // Wind speed in m/s
)

