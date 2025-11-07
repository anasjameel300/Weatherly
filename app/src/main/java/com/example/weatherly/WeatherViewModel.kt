package com.example.weatherly

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherly.model.WeatherResponse
import com.example.weatherly.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * ViewModel to manage weather data state
 * Handles API calls and manages UI state (loading, success, error)
 */
class WeatherViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "WeatherViewModel"
    }
    
    // API key from BuildConfig (set in local.properties)
    private val apiKey = BuildConfig.API_KEY
    
    // UI State
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    init {
        // Log API key status (first 4 chars only for security)
        Log.d(TAG, "API Key loaded: ${if (apiKey.isBlank()) "EMPTY" else "${apiKey.take(4)}..."}")
    }
    
    /**
     * Fetch weather data for a city
     * @param cityName Name of the city to get weather for
     */
    fun fetchWeather(cityName: String) {
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("Please enter a city name")
            return
        }
        
        // Check if API key is set
        if (apiKey.isBlank()) {
            Log.e(TAG, "API key is empty!")
            _uiState.value = WeatherUiState.Error(
                "API key not found. Please add WEATHER_API_KEY to local.properties file and sync the project."
            )
            return
        }
        
        Log.d(TAG, "Fetching weather for: $cityName")
        Log.d(TAG, "Using API key: ${apiKey.take(4)}...")
        
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            
            try {
                Log.d(TAG, "Making API call...")
                val response = ApiClient.apiService.getWeatherData(
                    cityName = cityName,
                    apiKey = apiKey
                )
                Log.d(TAG, "API call successful! City: ${response.name}, Temp: ${response.main.temp}°C")
                _uiState.value = WeatherUiState.Success(response)
            } catch (e: HttpException) {
                // Handle HTTP errors (401, 404, etc.)
                Log.e(TAG, "HTTP Error: ${e.code()} - ${e.message()}")
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "Response body: $errorBody")
                
                val errorMessage = when (e.code()) {
                    401 -> {
                        // Try to parse the error message from the response
                        val apiMessage = if (errorBody?.contains("Invalid API key") == true) {
                            "Invalid API key. Please verify your API key on OpenWeatherMap website. " +
                            "Make sure the key is active and correctly copied to local.properties. " +
                            "Note: New API keys may take 10-15 minutes to fully activate."
                        } else {
                            "Invalid API key. Please check your OpenWeatherMap API key in local.properties. " +
                            "Note: New API keys may take a few minutes to activate."
                        }
                        apiMessage
                    }
                    404 -> "City not found. Please check the city name and try again."
                    429 -> "Too many requests. Please wait a moment and try again."
                    else -> "Error ${e.code()}: ${e.message()}"
                }
                _uiState.value = WeatherUiState.Error(errorMessage)
            } catch (e: IOException) {
                // Handle network errors
                Log.e(TAG, "Network error: ${e.message}", e)
                _uiState.value = WeatherUiState.Error(
                    "Network error: Please check your internet connection and try again."
                )
            } catch (e: Exception) {
                // Handle other errors
                Log.e(TAG, "Error fetching weather: ${e.message}", e)
                _uiState.value = WeatherUiState.Error(
                    "Failed to fetch weather data: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }
}

/**
 * Sealed class to represent different UI states
 */
sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

