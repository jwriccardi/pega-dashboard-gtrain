# Product Guide: G-Train Dashboard App

## Initial Concept
A real-time dashboard application for the MTA G-Train, built for Android using Kotlin and Jetpack Compose.

## Target Audience
- Daily commuters relying on the G-Train in New York City.
- Transit enthusiasts tracking train movements.
- Users who need quick, reliable access to train arrival times and delays.

## Core Features
1. **Real-Time Tracking**: Integration with GTFS-realtime feeds to display live locations and arrival times for G-Train services.
2. **Interactive Dashboard**: A clean, modern UI utilizing Jetpack Compose to view station statuses at a glance.
3. **Alerts & Notifications**: Real-time service changes, delays, or planned work updates.
4. **Offline Resilience**: Caching mechanisms (potentially via OkHttp) to display recent data even with spotty subway connections.

## Goals & Metrics
- **Reliability**: Provide up-to-date and accurate GTFS data.
- **Performance**: Quick load times and smooth scrolling in the Compose UI.
- **Adoption**: Become the go-to utility for regular G-Train riders.
