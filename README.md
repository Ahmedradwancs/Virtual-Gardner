# Virtual Gardener

## Course Information
This project is part of the **DA324B HT24 Development of Mobile Applications** course.

## Overview
The **Virtual Gardener** is an Android mobile application designed to monitor and enhance plant health through real-time data insights, leveraging IoT technology. Developed as a team project in collaboration with Assimilatus Company, this application provides users with data on critical plant health parameters—such as soil moisture, temperature, and humidity—and offers timely notifications to support plant care.

### Problem Statement
Plants often require continuous monitoring to thrive, yet consistent manual care can be challenging. The Virtual Gardener app helps reduce human intervention by providing real-time monitoring, enabling early detection of issues that could impact plant health.

## Objective
To give users accessible, real-time data on plant health, enabling them to make informed decisions with notifications when conditions require attention.

## Features
- **Real-time Data Visualization**: Monitors humidity, temperature, and soil moisture.
- **Bluetooth Connectivity**: Pairs with IoT sensors for data transmission.
- **Graphical Insights**: Displays temperature vs. humidity trends.
- **Watering Reminders**: Sends notifications to remind users to water plants.
- **Data Export**: Allows exporting of plant health data for external analysis.

## Technologies Used

### Software
- **Programming Language**: Kotlin
- **Frameworks and Libraries**:
  - Android Jetpack (Compose, ViewModel, LiveData)
  - Navigation Components
  - Google APIs (Location API, Firebase Authentication, Firestore)
- **Android SDK and Build Tools**: Gradle, Android SDK, Material Design Components
- **Networking Libraries**: OkHttp, Volley, Gson
- **Version Control**: Git and GitHub
- **Project Structure and Design Patterns**: MVVM (Model-View-ViewModel), Modularization

### Hardware
- **Smart Nanotubes Smell Inspector**
- **Raspberry Pi Pico W**
- **DHT11 Temperature and Humidity Sensor**
- **Soil Moisture Sensor**

## Design
The app interface was prototyped in Figma with a user-centered layout and Material 3 design principles, ensuring a clean, responsive user experience that integrates seamlessly with real-time data insights.

## Limitations
- **Time Constraints**: Due to time limitations, machine learning functionality for smell recognition was not fully implemented.
- **Bluetooth Connectivity**: Limited support for newer Bluetooth APIs due to compatibility issues.

## Future Improvements
- **Smell Recognition Models**: Integrate machine learning models for smell classification and expand to detect additional smells.
- **Personalized Gardening Tips**: Provide location-based insights tailored to the user's area.
- **Improved Accuracy**: Enhance location-based advice for plant care by integrating precise location access.

## Setup and Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/Ahmedradwancs/Virtual-Gardner

### Setup and Installation
1. Open the project in Android Studio.
2. Connect the necessary hardware sensors to the Raspberry Pi and pair with your Android device via Bluetooth.
3. Build and run the application on your device.

### Contributors
- [Ahmed Saber Elsayed Radwan](https://github.com/Ahmedradwancs)
- [Abshir Muhumed Abdi](https://github.com/Abshir112)
- [Jwan Mardini](https://github.com/JwanMardini)
- [Lakshmi Vishal Hayagrivan](https://github.com/lakshmivishal9496)
- [Mohamad Alloush](https://github.com/Alloush95)
- [Solomon Sugamo](https://github.com/Solomon-hkr)
### Acknowledgments
This project was developed in coordination with Assimilatus Company. Special thanks to all team members for their hard work and collaboration.


Thank you for your interest in Virtual Gardener!
