# voxel-game
A simple voxel game written in Java.

<img src="screenshots/screenshot0.png" alt="Screenshot" width="50%" height="50%">

## Requirements
* Java 11+
* Git

## Clone, Compile, and Run the Project

### Linux Systems
1. `git clone https://github.com/SnkSynthesis/voxel-game`
2. `cd voxel-game`
3. `chmod +x mvnw`
4. * `./mvnw clean compile exec:java` (Works on Linux only)
   * Create a JAR:
        1. `./mvnw clean package`
        2. `java -jar ./target/voxel-game-1.0.jar`

### MacOS Systems
1. `git clone https://github.com/SnkSynthesis/voxel-game`
2. `cd voxel-game`
3. `chmod +x mvnw`
4. Create a JAR:
        1. `./mvnw clean package`
        2. `java -XstartOnFirstThread -jar ./target/voxel-game-1.0.jar` ([MacOS users should pass VM option: `-XstartOnFirstThread`](https://www.lwjgl.org/guide))

### Windows Systems
1. `git clone https://github.com/SnkSynthesis/voxel-game`
2. `cd voxel-game`
3.  * `mvnw compile exec:java`
    * Create a JAR:
         1. `mvnw clean package`
         2. `java -jar ./target/voxel-game-1.0.jar`

## Screenshots

<details><summary>View Screenshots</summary>
  
<img src="screenshots/screenshot1.jpg" alt="Screenshot" width="50%" height="50%">
<img src="screenshots/screenshot2.jpg" alt="Screenshot" width="50%" height="50%">
<img src="screenshots/screenshot3.jpg" alt="Screenshot" width="50%" height="50%">
<img src="screenshots/screenshot4.jpg" alt="Screenshot" width="50%" height="50%">
<img src="screenshots/screenshot5.jpg" alt="Screenshot" width="50%" height="50%">
<img src="screenshots/screenshot6.jpg" alt="Screenshot" width="50%" height="50%">
  
</details>

## Controls

* `[W]`, `[A]`, `[S]`, `[D]` - Move around
* `[Move Mouse]` - Look around
* `[Q]` - Lock mouse to window
* `[ESC]` - Unlock mouse from window
* `[Left Shift]` - Move down
* `[Left Control]` - Sprint (increase in movement speed)
* `[Space]` - Move up 
* `[T]` - Toggle wireframe view
