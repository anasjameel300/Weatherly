package com.example.weatherly.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit client configuration
 * Creates and provides the Retrofit instance for API calls
 */
object ApiClient {
    
    private const val TAG = "ApiClient"
    
    // Base URL for OpenWeatherMap API
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    
    // Retrofit instance (lazy initialization - created only when needed)
    private val retrofit: Retrofit by lazy {
        // Create logging interceptor to see the actual API calls
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG, message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Create OkHttpClient with logging
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Get the API interface instance
     * Use this to make API calls
     */
    val apiService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}

