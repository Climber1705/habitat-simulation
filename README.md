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
habitat-simulation/                   # Root directory of the project
├── src/                              # Source code directory
│   ├── main/                         # Main application source files
│   │   ├── java/                     # Java source code
│   │   │   ├── com/          # Main application package
│   │   │   │   └── tomtrotter/habitatsimulation/
│   │   │   │       ├── controller/
│   │   │   │       │    └── Simulator.java
│   │   │   │       ├── model/
│   │   │   │       │    ├── Animal.java
│   │   │   │       │    ├── Counter.java
│   │   │   │       │    ├── Deer.java
│   │   │   │       │    ├── Field.java
│   │   │   │       │    ├── FieldStats.java
│   │   │   │       │    ├── Genetics.java
│   │   │   │       │    ├── Hare.java
│   │   │   │       │    ├── Leopard.java
│   │   │   │       │    ├── Location.java
│   │   │   │       │    ├── Plant.java
│   │   │   │       │    ├── Predator.java
│   │   │   │       │    ├── Prey.java
│   │   │   │       │    ├── Randomizer.java
│   │   │   │       │    ├── Tiger.java
│   │   │   │       │    └── WildBoar.java
│   │   │   │       ├── view/
│   │   │   │       │    ├── FieldCanvas.java
│   │   │   │       │    └── SimulatorView.java
│   │   │   │       └── Main.java
│   │   │   └── module-info.java  # Initializes and launches the application
│   └── test/java/com/tomtrotter/habitatsimulation/
│         ├── controller/
│         │    └── SimulatorTests.java
│         ├── model/
│         │    ├── BreedingTests.java
│         │    ├── DiseaseTests.java
│         │    ├── FoodTests.java
│         │    ├── GeneticTests.java
│         │    └── PlantTests.java
│         └── view/
│              └── FieldCanvasTests.java
├── pom.xml                           # Maven configuration file managing project dependencies and build settings
├── build.gradle                      # Gradle configuration file (if Gradle is used instead of Maven)
├── README.md                         # Project documentation providing an overview, features, installation instructions, usage guidelines, and other relevant information
└── LICENSE                           # License information specifying the terms under which the project's code can be used and distributed

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

