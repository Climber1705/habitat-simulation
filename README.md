# Habitat Simulation

## Overview
This project is a JavaFX application designed to simulate a habitat environment with complex food chain interactions. The simulation includes multiple species with distinct behaviors, implementing predator-prey relationships, genetic evolution, and environmental influences such as disease. The project provides a realistic and engaging representation of ecosystem dynamics.

## Features
- Simulates predator-prey interactions with distinct hunting behaviors.
- Implements plant consumption mechanics for herbivores.
- Includes disease spread among species.
- Genetic inheritance and mutations affecting evolution.
- Age-based predator behavior.
- Extensible object-oriented design with reusable logic.

## Installation
To install this project, follow these steps:
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/your-repo.git
   ```
2. Navigate to the project directory:
   ```sh
   cd your-repo
   ```
3. Ensure you have Java and JavaFX installed:
   - Install Java (JDK 17 or later): [Download here](https://jdk.java.net/)
   - Install JavaFX SDK: [Download here](https://gluonhq.com/products/javafx/)
4. Configure JavaFX in your IDE (e.g., IntelliJ IDEA or Eclipse).
5. Build the project using Maven or Gradle:
   ```sh
   mvn clean install
   ```
   or
   ```sh
   gradle build
   ```

## Usage
To run the JavaFX application, use:
```sh
mvn javafx:run
```
or
```sh
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar target/your-app.jar
```
After running the command, the JavaFX window should launch, displaying the habitat simulation.

## Repository Structure
```
/habitat-simulation
│-- src/main/java/       # Java source code
│   │-- model/           # Simulation logic and animal behavior
│   │-- view/            # JavaFX UI components
│   │-- controller/      # Application control logic
│-- src/main/resources/  # FXML and CSS files
│-- src/test/java/       # Unit tests
│-- pom.xml              # Maven configuration file
│-- build.gradle         # Gradle configuration file
│-- README.md            # Project documentation
```

## Testing
Run the test suite using the following command:
```sh
mvn test
```
or
```sh
gradle test
```
JUnit tests cover animal behavior logic, hunting mechanisms, plant consumption, breeding mechanics, disease spread, and genetic inheritance.

## Improvements
Future improvements and features include:
- Dynamic plant growth and spread.
- Additional species-specific traits (e.g., metabolism, movement patterns).
- Seasonal changes affecting food availability and survival rates.

## License
This project is licensed under the [License Name] - see the [LICENSE](LICENSE) file for details.

