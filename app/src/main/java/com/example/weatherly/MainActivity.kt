package com.example.weatherly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.weatherly.ui.WeatherBackground
import com.example.weatherly.ui.theme.WeatherlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            WeatherlyTheme {
                WeatherApp()
            }
        }
    }
}

/**
 * Main weather app composable
 * Displays search field and weather information with dynamic backgrounds
 */
@Composable
fun WeatherApp(viewModel: WeatherViewModel = viewModel()) {
    var cityName by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    
    // Get weather condition for dynamic background
    val weatherMain = when (val state = uiState) {
        is WeatherUiState.Success -> state.weather.weather.firstOrNull()?.main
        else -> null
    }
    
    // Animated background gradient
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    // Background with dynamic gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherBackground.getGradientBrush(weatherMain))
    ) {
        // Weather animation overlay - using gradient animations for now
        // Lottie animations can be added later by downloading JSON files to assets folder
        
        // Subtle animated overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // App Title with proper top padding for status bar
            Spacer(modifier = Modifier.statusBarsPadding())
            Text(
                text = "Weather Today",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            
            // Search Section
            SearchSection(
                cityName = cityName,
                onCityNameChange = { cityName = it },
                onSearchClick = { viewModel.fetchWeather(cityName) }
            )
            
            // Weather Display Section
            when (val state = uiState) {
                is WeatherUiState.Idle -> {
                    WelcomeCard()
                }
                is WeatherUiState.Loading -> {
                    LoadingCard()
                }
                is WeatherUiState.Success -> {
                    WeatherCard(weather = state.weather)
                }
                is WeatherUiState.Error -> {
                    ErrorCard(message = state.message)
                }
            }
        }
    }
}

/**
 * Improved search section with better design
 */
@Composable
fun SearchSection(
    cityName: String,
    onCityNameChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // City name input field
            OutlinedTextField(
                value = cityName,
                onValueChange = onCityNameChange,
                modifier = Modifier.weight(1f),
                placeholder = { 
                    Text(
                        "Enter city name",
                        color = Color.Gray.copy(alpha = 0.7f)
                    ) 
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF4A90E2)
                    )
                }
            )
            
            // Search button
            Button(
                onClick = onSearchClick,
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    "Search",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Improved welcome card
 */
@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "🌤️",
                fontSize = 80.sp
            )
            Text(
                text = "Search for Weather",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2)
            )
            Text(
                text = "Enter a city name above to get started",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Improved loading card
 */
@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4A90E2),
                modifier = Modifier.size(56.dp),
                strokeWidth = 4.dp
            )
            Text(
                text = "Loading weather data...",
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Improved weather card with better design
 */
@Composable
fun WeatherCard(weather: com.example.weatherly.model.WeatherResponse) {
    val weatherCondition = weather.weather.firstOrNull()
    val iconUrl = "https://openweathermap.org/img/wn/${weatherCondition?.icon}@2x.png"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // City name
            Text(
                text = weather.name,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2),
                style = MaterialTheme.typography.headlineMedium
            )
            
            // Weather icon and description
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = weatherCondition?.description,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = weatherCondition?.description?.replaceFirstChar { it.uppercase() } ?: "",
                    fontSize = 20.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Temperature (large and prominent)
            Text(
                text = "${weather.main.temp.toInt()}°C",
                fontSize = 88.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2),
                style = MaterialTheme.typography.displayLarge
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.LightGray.copy(alpha = 0.5f),
                thickness = 1.dp
            )
            
            // Weather details in a row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    label = "Humidity",
                    value = "${weather.main.humidity}%",
                    icon = "💧"
                )
                
                WeatherDetailItem(
                    label = "Wind Speed",
                    value = "${weather.wind.speed} m/s",
                    icon = "💨"
                )
                
                WeatherDetailItem(
                    label = "Pressure",
                    value = "${weather.main.pressure} hPa",
                    icon = "📊"
                )
            }
        }
    }
}

/**
 * Improved weather detail item
 */
@Composable
fun WeatherDetailItem(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = icon,
            fontSize = 32.sp
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A90E2)
        )
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Improved error card
 */
@Composable
fun ErrorCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "⚠️",
                fontSize = 80.sp
            )
            Text(
                text = "Error",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF5252)
            )
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}
