## Weatherly Beginner Guide

### 1. Meet Weatherly
- Weatherly is an Android app that shows the current weather for any city.
- You type a city name, it contacts the OpenWeatherMap service, and it displays temperature, humidity, wind speed, and a short description.
- The app is written in Kotlin and uses modern Android tools so the code stays clean and the screen looks polished.

### 2. Crash Course: How Android Apps Work
- An Android app is made of activities (screens) and other components. Think of an activity as a single page in a book.
- Developers build apps in Android Studio, the official IDE (Integrated Development Environment).
- Android Studio uses Gradle (a build system) to bundle code, images, and settings into an APK that runs on devices.

### 3. Understanding Gradle in This Project
- Gradle automates building, testing, and packaging. It knows which plugins and libraries to pull in.
- Key files:
  - `settings.gradle.kts`: tells Gradle which modules exist (here only `:app`).
  - Root `build.gradle.kts`: applies common plugins.
  - `gradle/libs.versions.toml`: central list of library versions; makes updates easy.
  - `app/build.gradle.kts`: the heart of the module—defines compileSdk (36), defaultConfig, and dependencies like Retrofit, Compose, Coil, Coroutines.
  - `gradle.properties`: project-wide settings (Java memory, Kotlin style, etc.).
- During a build Gradle reads these files, downloads dependencies, compiles Kotlin to bytecode, merges resources, and produces the final APK.

### 4. What the Folders Mean
- `app/src/main/java/`: all Kotlin source code. Main packages:
  - `MainActivity.kt`: sets up the UI.
  - `WeatherViewModel.kt`: handles data fetching and state.
  - `network/`: houses API client and interface.
  - `model/`: data classes matching the JSON from the API.
  - `ui/`: helper utilities and theme definitions.
- `app/src/main/res/`: resources (icons, strings, colors, backup rules).
- `app/src/main/AndroidManifest.xml`: registers permissions (`INTERNET`) and the launcher activity.
- `local.properties`: holds machine-specific info like the Android SDK path and the `WEATHER_API_KEY`. Not tracked in Git.

### 5. Technologies & Libraries (Plain English)
- **Kotlin**: a concise programming language Google recommends for Android.
- **Jetpack Compose**: modern toolkit for building UI with code (no XML layouts). You describe what the screen should look like, Compose handles updates.
- **Material Design 3**: Google’s design system—gives consistent colors, typography, spacing.
- **MVVM pattern**: Model-View-ViewModel structure. ViewModel stores data, View (the UI) observes and reacts.
- **Retrofit**: library that simplifies HTTP calls; you define an interface and Retrofit creates the network code.
- **Gson**: converts JSON text from the API into Kotlin objects.
- **OkHttp** + **Logging Interceptor**: underlying HTTP client and logging helper to inspect requests/responses.
- **Kotlin Coroutines**: lets code run in the background (network calls) without freezing the screen.
- **StateFlow**: a data stream that Compose listens to; when data changes, the UI updates.
- **Coil**: loads weather icons from URLs and shows them in Compose.
- **Lottie**: animation library included for future enhancements.

### 6. How Weatherly Flows
1. The user opens the app (`MainActivity`).
2. `WeatherApp()` composable shows a search box and cards for welcome/loading/results/errors.
3. When “Search” is tapped, `WeatherViewModel.fetchWeather()` runs.
4. The ViewModel checks the API key, switches the UI state to `Loading`, then launches a coroutine.
5. Retrofit sends a GET request to `https://api.openweathermap.org/data/2.5/weather` with the city name and the API key.
6. The response JSON is converted into `WeatherResponse` data classes.
7. The ViewModel emits `Success` with the data; Compose recomposes to display the weather.
8. If something fails (empty city, bad key, no internet), the ViewModel emits `Error` with a friendly message.

### 7. Important Concepts Explained
- **Activity**: an Android screen. `MainActivity` hosts the Compose content.
- **Composable**: a function annotated with `@Composable`. Compose uses these to draw UI; for example `WeatherCard()`.
- **ViewModel**: survives configuration changes (like rotation) and holds UI state.
- **StateFlow**: a flow that emits updates; Compose collects it to stay in sync.
- **API (Application Programming Interface)**: a contract to request data. Here we call OpenWeatherMap’s API.
- **Intent**: a message object used to request an action from another component. Two types:
  - Explicit intent: you specify the exact component (e.g., start another activity in your app).
  - Implicit intent: you ask Android to find something that can handle the task (e.g., open a map app). In Weatherly we only use the implicit main intent in `AndroidManifest.xml` to mark the launcher activity.
- **BuildConfig**: a generated class that contains constants defined in Gradle. We put the API key there so Kotlin code can access it safely (`BuildConfig.API_KEY`).

### 8. Key Files in More Detail
- `MainActivity.kt`: sets edge-to-edge layout, hosts the `WeatherApp` composable, and applies `WeatherlyTheme`.
- `WeatherViewModel.kt`: holds `WeatherUiState` (Idle, Loading, Success, Error), makes API calls with Retrofit, handles errors, logs helpful information.
- `network/ApiInterface.kt`: declares the `GET weather` endpoint with query parameters.
- `network/ApiClient.kt`: builds the Retrofit instance with OkHttp logging.
- `model/WeatherResponse.kt`: data classes that mirror the JSON response (city name, `main` block, `weather` list, `wind`).
- `ui/WeatherBackground.kt`: chooses gradient colors based on weather (clear, clouds, rain, etc.) and applies them.
- `ui/theme` files (`Theme.kt`, `Color.kt`, `Type.kt`): define color schemes and typography using Compose Material 3.
- `AndroidManifest.xml`: adds the internet permission and registers `MainActivity` as the launcher with an intent filter.
- `local.properties`: stores the `WEATHER_API_KEY`. Because it’s local-only, you must add your own key when setting up the project.

### 9. Error Handling Highlights
- Empty city name → shows “Please enter a city name”.
- Missing API key → explains how to add `WEATHER_API_KEY` in `local.properties`.
- HTTP status codes: 401 (invalid key), 404 (city not found), 429 (rate limit).
- Network issues → advises checking the connection.

### 10. Building & Running the App
- Install Android Studio, open the project folder (`Weatherly`).
- Make sure the `local.properties` file contains your OpenWeatherMap key (`WEATHER_API_KEY=...`).
- Sync Gradle so dependencies download.
- Use the Run button (green triangle) to build and launch on an emulator or USB-connected device.
- Gradle compiles the code, Compose renders the UI, and you can search for cities to see live weather.

### 11. Next Steps (Optional ideas)
- Add hourly/weekly forecasts, notifications, voice search, or offline caching.
- Use Lottie animations for visual weather effects.
- Localize strings for more languages and add dark theme customization.

By reading this guide, you should now have a friendly overview of how the Weatherly app is built, what each part does, and how the pieces work together even if you’re completely new to Android development.

