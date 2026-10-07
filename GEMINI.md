# G Train Dashboard (pega-dashboard-gtrain)

## Project Overview
This project is a native Android application designed to serve as a real-time dashboard for the NYC MTA G train. It specifically displays upcoming arrivals for the Myrtle-Willoughby Avs station (Northbound and Southbound). 

The app is built to be a persistent, kiosk-like dashboard:
- Enforces always-on screen behavior (`FLAG_KEEP_SCREEN_ON`).
- Uses Immersive Mode to hide system navigation and status bars.
- Polls the MTA GTFS-realtime feed automatically (every 30 seconds).

## Technology Stack & Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Data Fetching**: 
  - `OkHttp` for network requests (includes custom TrustManager for SSL).
  - `gtfs-realtime-bindings` for parsing the MTA's Protocol Buffer data.
- **Concurrency**: Kotlin Coroutines and Flows (used in the ViewModel for state management and polling).

## Key Components
- `MainActivity`: Entry point, sets up window flags for kiosk mode and sets the Compose content.
- `DashboardViewModel`: Manages application state (`DashboardUiState`), handles periodic polling of the MTA API, and exposes a `StateFlow` to the UI.
- `MtaDataService`: Handles network communication with the MTA GTFS-realtime endpoint, parses the protobuf feed, and filters data for the target stop (`G32N`/`G32S`).
- **Python Scripts**: The repository root also contains several Python utility scripts (`fix_fonts.py`, `fix_skew_clip.py`, `scale_fix.py`, `update_skins.py`), likely used for pre-processing assets, designs, or environment maintenance.

## Development Guidelines
- **UI Updates**: All UI changes should use Jetpack Compose. Avoid introducing legacy Android Views unless absolutely necessary.
- **Kiosk Mode**: Preserve the immersive and keep-screen-on behaviors in `MainActivity` since this is intended to be a mounted dashboard.
- **Networking**: Maintain the usage of `OkHttp` and the GTFS protobuf bindings for MTA data fetching.
- **Coroutines**: Use Kotlin Coroutines and Flows for background work and state propagation.
