# Color Flow Circuit 🌈

Color Flow Circuit is an engaging puzzle game where players create and mix color paths to solve increasingly challenging levels. Built with modern Android development technologies and featuring smooth animations, cosmic themes, and intuitive touch controls. This is a personal project that I could expand on in the future, mainly to test making an Android Application.
## 🎮 Gameplay

Players navigate through 10 progressively challenging levels where they:
- Draw paths from color sources to receivers
- Mix colors using mixer nodes to create new colors
- Solve puzzles while avoiding path intersections
- Complete levels by matching all receiver color requirements

## 🛠 Technical Details

### Technologies Used
- Kotlin 1.9.22
- Jetpack Compose with Material 3
- AndroidX Core KTX 1.12.0
- Custom sound management system
- State management using Compose state
- Canvas-based game rendering

### Architecture Highlights
- Clean separation of game logic and UI components
- Reactive state management
- Singleton pattern for global managers
- Extensible level system
- Efficient path intersection detection

### Key Components
- `GameBoard`: Main game logic and state management
- `ColorMixer`: Advanced color mixing calculations
- `PathIntersectionUtil`: Path collision detection
- `SoundManager`: Audio system with volume control
- `ProgressManager`: Level progression tracking

## 📱 Screens

1. **Main Menu**: Game entry point with level selection and settings
2. **Level Select**: Grid of available and locked levels
3. **Game Screen**: Main gameplay area with:
   - Color sources and receivers
   - Mixer nodes
   - Path drawing canvas
   - Control buttons
4. **Settings**: Audio control and game options

## 🔧 Setup

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on an Android device or emulator (minimum SDK 24)

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
