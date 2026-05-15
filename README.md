Namma-Raste Reporter (Infrastructure)
Overview

Namma-Raste Reporter is an Android-based infrastructure reporting application developed to help citizens quickly report public infrastructure issues such as potholes and broken streetlights. The application simplifies the reporting process through a one-click system that captures images, GPS location, and issue details, then generates a unique Ticket ID for tracking.

The project aims to bridge the communication gap between citizens and local maintenance authorities by providing a fast and efficient reporting platform.

Problem Statement

Potholes and damaged streetlights are major causes of road accidents and public inconvenience. Existing complaint systems often involve lengthy paperwork and delayed responses. This project provides a digital solution that enables users to report infrastructure problems instantly using their mobile devices.

Objectives
> Simplify infrastructure issue reporting
> Improve road safety through faster complaint registration
> Enable accurate issue tracking using GPS and Ticket IDs
> Reduce manual work for local authorities
> Support smart city and smart village initiatives

Features
> Capture Infrastructure Issues
> Open device camera
> Capture issue image
# Select issue category:
 > Pothole
 > Broken Streetlight

Automatic Data Collection
> Captures GPS location automatically
> Records date and time of complaint

Ticket ID Generation
> Generates unique Ticket IDs for every report
> Prevents duplicate tracking records

Complaint Status Tracking
> Users can enter Ticket ID
> View progress or complaint status

Local Data Storage
> Reports can be stored using:
 > Room Database

# Tech Stack
 # Frontend
  > Kotlin
  > XML Layouts
  > Android Studio
 # Backend / Database
  > Firebase
  > Room Database
 # APIs and Libraries
  > CameraX API
  > Location Services API

# Security
 > User login authentication to prevent spam reports
 > Unique Ticket ID validation system

# Installation Steps
>> Prerequisites
 > Android Studio installed
 > Android SDK configured
 > Firebase account (optional for cloud storage)

# Steps
1. Clone the repository: git clone <repository-link>
2. Open the project in Android Studio
3. Sync Gradle files
4. Configure Firebase (if used)
5. Run the application on:
6. Android Emulator
7. Physical Android Device


# Success Criteria
 > Report generation within 3 clicks
 > Smooth camera preview on mid-range devices
 > Proper Ticket ID generation without duplication
 > Clean and maintainable Kotlin code structure
