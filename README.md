# Habitat Simulation

## Overview
This repository is a JavaFX application initially developed as coursework at King’s College London. I’ve been expanding and refining it beyond the original requirements. The simulation models a dynamic habitat environment with complex food chain interactions, including multiple species with distinct behaviors, predator-prey relationships & genetic evolution and environmental influences like disease.

## Features
- 🦊 Simulates predator-prey interactions with distinct hunting behaviors.
- 🌿 Implements plant consumption mechanics for herbivores.
- 🦠 Includes disease spread among species.
- 🧬 Genetic inheritance and mutations affecting evolution.
- ⏳ Age-based predator behavior.
- 🛠️ Extensible object-oriented design with reusable logic.

## Installation
To install this project, follow these steps:
1. Clone the repository:
   ```sh
   git clone https://github.com/Climber1705/habitat-simulation.git
   ```
2. Navigate to the project directory:
   ```sh
   cd habitat-simulation
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
``` graphql
habitat-simulation/                   # Root directory of the project
├── src/                              # Source code directory
│   ├── main/                         # Main application source files
│   │   ├── java/                     # Java source code
│   │   │   ├── com/                  # Main application package
│   │   │   │   └── tomtrotter/habitatsimulation/
│   │   │   │       ├── controller/   # Contains application control logic
│   │   │   │       │    └── Simulator.java  # Manages the simulation loop and logic
│   │   │   │       ├── model/        # Simulation logic and data models
│   │   │   │       │    ├── Animal.java  # Abstract class for all animals
│   │   │   │       │    ├── Counter.java  # Tracks population counts
│   │   │   │       │    ├── Deer.java  # Class representing deer behavior
│   │   │   │       │    ├── Field.java  # Defines the simulation grid
│   │   │   │       │    ├── FieldStats.java  # Gathers statistics about the field
│   │   │   │       │    ├── Genetics.java  # Handles genetic mutations and inheritance
│   │   │   │       │    ├── Hare.java  # Class representing hare behavior
│   │   │   │       │    ├── Leopard.java  # Class representing leopard behavior
│   │   │   │       │    ├── Location.java  # Represents a position in the simulation grid
│   │   │   │       │    ├── Plant.java  # Class representing plant behavior
│   │   │   │       │    ├── Predator.java  # Interface defining predator-specific behavior
│   │   │   │       │    ├── Prey.java  # Interface defining prey-specific behavior
│   │   │   │       │    ├── Randomizer.java  # Utility class for generating random values
│   │   │   │       │    ├── Tiger.java  # Class representing tiger behavior
│   │   │   │       │    └── WildBoar.java  # Class representing wild boar behavior
│   │   │   │       ├── view/          # UI components using JavaFX
│   │   │   │       │    ├── FieldCanvas.java  # Renders the simulation grid
│   │   │   │       │    └── SimulatorView.java  # Defines the simulation UI layout
│   │   │   │       └── Main.java      # Entry point of the JavaFX application
│   │   │   └── module-info.java  # Java module definition
│   └── test/java/com/tomtrotter/habitatsimulation/  # Unit tests for the project
│         ├── controller/  # Tests for application logic
│         │    └── SimulatorTests.java  # Tests for simulation logic
│         ├── model/  # Tests for model behavior
│         │    ├── BreedingTests.java  # Tests genetic inheritance and reproduction
│         │    ├── DiseaseTests.java  # Tests disease spread mechanics
│         │    ├── FoodTests.java  # Tests food consumption and availability
│         │    ├── GeneticTests.java  # Tests genetic mutation behaviors
│         │    └── PlantTests.java  # Tests plant growth and behavior
│         └── view/  # Tests for UI components
│              └── FieldCanvasTests.java  # Tests the rendering of the simulation grid
├── pom.xml                           # Maven configuration file managing project dependencies and build settings
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
- Add a section to show the number of each infected species.
- See the average characteristic of each species.
- Additional species-specific traits (e.g., movement patterns, field of view).
- Seasonal changes affecting food availability and survival rates.

## License
This project operates under the GNU General Public License v3.0. The [LICENSE](https://choosealicense.com/licenses/gpl-3.0/) file provides details.

