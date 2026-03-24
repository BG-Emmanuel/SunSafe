# ☀️ SunSafe — UV Exposure Tracker

A smart health application designed to help users monitor their sun exposure and prevent skin damage. The app sends daily reminders to apply sunscreen and provides real-time UV index updates based on the user's location. Users can log their sun exposure time manually or let the app automatically track duration using the ambient light sensor.

---

## 📋 Project Overview

| Property | Value |
|---|---|
| Language | Kotlin |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 34 (Android 14) |
| Architecture | MVVM + Clean Architecture |
| DI | Dagger Hilt |
| UI | Jetpack Compose + Material 3 |
| Database | Room |
| Networking | Retrofit + OkHttp + Moshi |
| Background | WorkManager |
| Preferences | DataStore |
| Location | Google Play Services |

---

## 🗂️ Project Structure

```
app/src/main/kotlin/com/sunsafe/app/
├── MainActivity.kt                      # Entry point, splash screen, nav
├── SunSafeApplication.kt                # Hilt app + WorkManager config
│
├── domain/
│   ├── model/Models.kt                  # All domain data models
│   ├── repository/Repositories.kt       # Repository interfaces
│   └── usecase/
│       └── ExposureUseCases.kt          # Core safe-time calculation engine
│
├── data/
│   ├── local/
│   │   ├── SunSafeDatabase.kt           # Room database
│   │   ├── dao/Daos.kt                  # UserDao, ExposureDao, SkinPhotoDao
│   │   └── entity/Entities.kt           # Room entities + domain mappers
│   ├── remote/
│   │   └── api/ApiInterfaces.kt         # OpenUV + OpenWeatherMap APIs
│   └── repository/
│       ├── Repositories.kt              # User, UV, Exposure, Photo impls
│       ├── SettingsRepositoryImpl.kt    # DataStore preferences
│       └── LocationRepositoryImpl.kt    # Fused Location Provider
│
├── di/
│   └── Modules.kt                       # Hilt modules (DB, Network, Repos)
│
├── notification/
│   └── SunSafeNotificationManager.kt   # All 5 notification channels + actions
│
├── worker/
│   └── Workers.kt                       # ExposureTracking, UvRefresh, Reminder
│
├── service/
│   └── ExposureTrackingService.kt       # Foreground service for active tracking
│
├── receiver/
│   └── Receivers.kt                     # Boot + NotificationAction receivers
│
└── presentation/
    ├── theme/Theme.kt                   # Material 3 + UV color palette
    ├── navigation/Navigation.kt         # NavHost with 6 destinations
    ├── components/Components.kt         # Reusable Compose components
    ├── viewmodel/
    │   ├── MainViewModel.kt
    │   ├── DashboardViewModel.kt        # Live sensor + UV + exposure state
    │   └── OtherViewModels.kt           # Onboarding, History, Settings, Forecast
    └── screens/
        ├── onboarding/OnboardingScreen.kt    # 5-page pager flow
        ├── dashboard/DashboardScreen.kt      # Main UV + exposure dashboard
        ├── history/HistoryScreen.kt          # 30-day calendar history
        ├── forecast/ForecastScreen.kt        # 5-day UV forecast
        ├── report/ReportScreen.kt            # PDF generation + share
        └── settings/SettingsScreen.kt        # Full preferences screen
```

---

## 🚀 Getting Started

### 1. Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK API 34

### 2. API Keys

Open `app/build.gradle.kts` and replace the placeholder keys:

```kotlin
buildConfigField("String", "OPENUV_API_KEY", "\"YOUR_OPENUV_API_KEY\"")
buildConfigField("String", "OWM_API_KEY", "\"YOUR_OWM_API_KEY\"")
```

**Getting API keys:**
- **OpenUV** (primary): https://www.openuv.io — Free tier: 500 req/day
- **OpenWeatherMap** (fallback): https://openweathermap.org/api — Free tier available

> **Note:** The app gracefully falls back to OpenWeatherMap when OpenUV fails, and further falls back to a time-of-day estimation algorithm when both APIs are unavailable.

### 3. Build & Run

```bash
./gradlew assembleDebug
```

Or open the project in Android Studio and Run (▶).

---

## 🧮 Exposure Calculation Engine

The core algorithm in `CalculateSafeExposureUseCase`:

```
Safe Time = Base Burn Time × UV Multiplier × SPF Multiplier × Effectiveness Multiplier
```

| Factor | Values |
|---|---|
| **Base burn time by skin type** | Type I: 10m, II: 20m, III: 30m, IV: 50m, V: 80m, VI: 120m |
| **UV Index multiplier** | 0–2: ×1.0 · 3–5: ×0.8 · 6–7: ×0.6 · 8+: ×0.4 |
| **SPF multiplier** | SPF 15: ×8 · SPF 30: ×15 · SPF 50+: ×25 · None: ×1 |
| **Effectiveness decay** | 0–80min: ×1.0 · 80–120min: ×0.7 · 120min+: ×0.5 |

---

## 🔔 Notification System

Five channels, all configurable in Settings:

| Channel | Trigger |
|---|---|
| **UV Warnings** | 80% / 95% / 100% of safe limit reached |
| **Sunscreen Reminders** | Morning (configurable time) + reapplication every 2h |
| **Daily Forecast** | 7:30 AM with UV peak prediction |
| **Achievements** | Vitamin D goal, streaks |
| **Tracking** | Persistent foreground service notification |

Notification actions: **Applied ✓** (logs sunscreen immediately) and **Snooze 30m**.

---

## ⚙️ Background Workers

| Worker | Frequency | Constraints |
|---|---|---|
| `ExposureTrackingWorker` | Every 15 min | Battery not low |
| `UvDataRefreshWorker` | Daily at 7:30 AM | Network required, battery not low |
| `ReminderWorker` | On-demand | None |

---

## 🧪 Running Tests

```bash
./gradlew test
```

The test suite covers:
- **ExposureCalculationTest** — All skin types, UV multipliers, SPF multipliers, effectiveness decay, status thresholds
- **SunscreenStatusTest** — Model logic for reapplication and effectiveness
- **UvSeverityTest** — All UV boundary conditions

---

## 📱 Screens

| Screen | Description |
|---|---|
| Splash | Splash screen with animated icon |
| Onboarding (5 pages) | Welcome → Skin Type (Fitzpatrick) → Sunscreen SPF → Profile → Permissions |
| Dashboard | UV index card, circular exposure progress, sunscreen status, timeline, recommendations |
| History | 30-day list with daily summaries, expandable session detail, burn logging |
| Forecast | 5-day UV forecast with severity bars and scale legend |
| Report | Summary statistics + PDF generation & sharing |
| Settings | Profile edit, notification toggles, sensor sensitivity, dark mode |

---

## 🎨 Design System

- **Material 3** throughout
- **UV color scale**: 🟢 Low · 🟡 Moderate · 🟠 High · 🔴 Very High · 🟣 Extreme
- **Dark mode** supported
- **Edge-to-edge** layout with `enableEdgeToEdge()`
- Adaptive typography from `SunSafeTypography`

---

## 🔒 Permissions

| Permission | Required? | Purpose |
|---|---|---|
| `ACCESS_FINE_LOCATION` | Recommended | Precise UV data |
| `ACCESS_COARSE_LOCATION` | Yes | UV data |
| `ACCESS_BACKGROUND_LOCATION` | Optional | Background tracking |
| `POST_NOTIFICATIONS` | Recommended | Reminders + warnings |
| `CAMERA` | Optional | Skin photo logging |
| `FOREGROUND_SERVICE_HEALTH` | Yes | Exposure tracking service |

All denials are handled gracefully with fallback behaviour.

---

## 🏗️ Architecture Decisions

- **Clean Architecture layers**: `domain` ← `data` ← `presentation` — domain never depends on Android
- **StateFlow** everywhere for reactive UI, no LiveData
- **Hilt** for compile-time safe DI across all layers including Workers
- **Repository pattern** isolates data sources; UI only sees domain models
- **Room + DataStore** for persistence — structured data in Room, preferences in DataStore
- **Fallback chain**: OpenUV API → OpenWeatherMap → Time-of-day estimate — always returns a value

---

## 📄 Licence

MIT — free to use, modify, and distribute.
