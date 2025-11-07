package com.example.weatherly.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Weather-based background colors
 * Returns gradient colors based on weather conditions
 */
object WeatherBackground {
    
    /**
     * Get gradient colors based on weather condition
     * @param weatherMain Main weather condition (e.g., "Clear", "Clouds", "Rain")
     * @return List of colors for gradient background
     */
    fun getGradientColors(weatherMain: String?): List<Color> {
        return when (weatherMain?.lowercase()) {
            "clear" -> listOf(
                Color(0xFF87CEEB),  // Sky blue
                Color(0xFFE0F6FF),  // Light blue
                Color(0xFFFFE4B5)   // Light yellow (sun)
            )
            "clouds" -> listOf(
                Color(0xFFB0C4DE),  // Light steel blue
                Color(0xFFD3D3D3),  // Light gray
                Color(0xFFE6E6FA)   // Lavender
            )
            "rain", "drizzle" -> listOf(
                Color(0xFF708090),  // Slate gray
                Color(0xFF778899),  // Light slate gray
                Color(0xFFB0C4DE)   // Light steel blue
            )
            "thunderstorm" -> listOf(
                Color(0xFF2F4F4F),  // Dark slate gray
                Color(0xFF4B0082),  // Indigo
                Color(0xFF483D8B)   // Dark slate blue
            )
            "snow" -> listOf(
                Color(0xFFE0E0E0),  // Light gray
                Color(0xFFF5F5F5),  // White smoke
                Color(0xFFFFFFFF)   // White
            )
            "mist", "fog", "haze" -> listOf(
                Color(0xFFD3D3D3),  // Light gray
                Color(0xFFE0E0E0),  // Gainsboro
                Color(0xFFF5F5F5)   // White smoke
            )
            else -> listOf(
                Color(0xFF87CEEB),  // Default: Sky blue
                Color(0xFFE0F6FF)   // Light blue
            )
        }
    }
    
    /**
     * Get gradient brush based on weather condition
     * @param weatherMain Main weather condition
     * @return Brush for gradient background
     */
    fun getGradientBrush(weatherMain: String?): Brush {
        val colors = getGradientColors(weatherMain)
        return Brush.verticalGradient(colors = colors)
    }
    
    /**
     * Get Lottie animation URL based on weather condition
     * Uses free Lottie animations from LottieFiles
     * @param weatherMain Main weather condition
     * @return Lottie animation URL or null if not available
     */
    fun getLottieAnimationUrl(weatherMain: String?): String? {
        return when (weatherMain?.lowercase()) {
            "clear" -> null // Will use gradient for now
            "clouds" -> null
            "rain", "drizzle" -> null
            "thunderstorm" -> null
            "snow" -> null
            else -> null
        }
    }
}

