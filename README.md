# 🦓 **Habitat Simulation**

## 🔍 **Overview**
This repository is a **JavaFX application** initially developed as coursework at **King’s College London**. I’ve been expanding and refining it beyond the original requirements. The simulation models a dynamic habitat environment with **complex food chain interactions**, including multiple species with distinct behaviours, **predator-prey relationships, genetic evolution** and **environmental influences like disease**.

## 🌟 **Features**
- 🦊 **Simulates predator-prey interactions** with distinct hunting behaviors.
- 🌿 **Implements plant consumption mechanics** for herbivores.
- 🦠 **Includes disease spread** among species and **Immunity**.
- 🧬 **Genetic inheritance and mutations** affecting evolution.
- ⏳ **Age-based predator behavior**.
- 🛠️ **Extensible object-oriented design** with reusable logic.

## ⚙️ **Installation**
To install this project, follow these steps:
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Climber1705/habitat-simulation.git
   ```
2. **Navigate to the project directory:**
   ```bash
   cd habitat-simulation
   ```
3. **Ensure you have Java and JavaFX installed:**
   - **Install Java (JDK 17 or later):** [Download here](https://jdk.java.net/)
   - **Install JavaFX SDK:** [Download here](https://gluonhq.com/products/javafx/)
4. **Configure JavaFX** in your IDE (e.g., IntelliJ IDEA or Eclipse).
5. **Build the project using Maven or Gradle:**
   ```bash
   mvn clean install
   ```
   or
   ```bash
   gradle build
   ```

## 🚀 **Usage**
To run the JavaFX application, use:
```bash
mvn javafx:run
```
or
```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar target/your-app.jar
```
After running the command, the **JavaFX window should launch, displaying the habitat simulation.**

## 🗂️ **Repository Structure**
``` graphql
habitat-simulation/                   # Root directory of the project
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/tomtrotter/habitatsimulation/
│   │           ├── controller/              # Contains application control logic
│   │           │   └── Simulator.java       # Manages the simulation loop and logic
│   │           ├── model/                   # Contains all simulation models and logic
│   │           │   ├── core/                # Core classes shared by all organisms
│   │           │   │   ├── Animal.java          # Abstract class representing general animal behaviour
│   │           │   │   ├── Disease.java         # Class handling disease infection and transmission
│   │           │   │   ├── Organism.java        # Base class for all organisms in the simulation
│   │           │   │   ├── Plant.java           # Class representing plant behaviour and growth
│   │           │   │   ├── Predator.java        # Interface for predator behaviour
│   │           │   │   └── Prey.java            # Interface for prey 
│   │           │   ├── entities/            # Specific animal species/entities in the simulation
│   │           │   │   ├── Deer.java            # Implements Deer behaviour as a Prey
│   │           │   │   ├── Hare.java            # Implements Hare behaviour as a Prey
│   │           │   │   ├── Leopard.java         # Implements Leopard behaviour as a Predator
│   │           │   │   ├── Tiger.java           # Implements Tiger behaviour as a Predator
│   │           │   │   └── WildBoar.java        # Implements Wild Boar behaviour (could be predator or prey)
│   │           │   ├── environment/         # Environmental components of the simulation
│   │           │   │   ├── Field.java           # Represents the 2D simulation grid
│   │           │   │   ├── FieldStats.java      # Gathers and calculates statistics about the field
│   │           │   │   └── Location.java        # Represents a coordinate within the simulation grid
│   │           │   ├── factory/             # Factory classes for object instantiation
│   │           │   │   └── OrganismFactory.java # Central class for creating organisms
│   │           │   ├── genetics/            # Handles genetic logic
│   │           │   │   └── Genetics.java        # Manages inheritance, mutation, and gene mixing
│   │           │   ├── state/               # Maintains simulation and view state
│   │           │   │   ├── SimulatorState.java  # Holds simulation status and statistics
│   │           │   │   └── ViewState.java       # Holds UI state (selected organism, etc.)behaviour
│   │           │   └── util/                # Utility/helper classes
│   │           │       ├── Counter.java         # Tracks population counts by organism type
│   │           │       └── Randomizer.java      # Provides seeded random number generation
│   │           ├── view/                    # JavaFX-based UI classes
│   │           │   ├── BaseView.java            # Base class for shared view functionality
│   │           │   ├── FieldCanvas.java         # Canvas for rendering the simulation grid
│   │           │   ├── SettingsView.java        # UI component for adjusting simulation settings
│   │           │   └── SimulatorView.java       # Main UI layout for running simulations
│   │           └── Main.java                # JavaFX application entry point
│   └── test/
│       └── java/
│           └── com/tomtrotter/habitatsimulation/
│               ├── controller/              # Tests for controller logic
│               │   └── SimulatorTests.java      # Unit tests for simulation loop and event handling
│               ├── model/                   # Model-specific test cases
│               │   ├── BreedingTests.java       # Tests inheritance and breeding behaviour
│               │   ├── DiseaseTests.java        # Tests infection spread, recovery, and death
│               │   ├── FoodTests.java           # Tests the organism's feeding behaviour and food availability
│               │   ├── GeneticTests.java        # Tests for mutation rates and gene mixing
│               │   └── PlantTests.java          # Tests plant growth, reproduction, and lifecycle
│               └── view/                    # UI tests
│                   └── FieldCanvasTests.java    # Tests rendering logic of the field grid
├── module-info.java                         # Java module declaration file (module dependencies)
├── pom.xml                                  # Maven build configuration (dependencies, build, plugins)
├── README.md                                # Project documentation (overview, setup, usage)
└── LICENSE                                  # Licensing information for the project

```

## 🧪 **Testing**
Run the test suite using the following command:
```sh
mvn test
```
or
```sh
gradle test
```
**JUnit tests cover:**
- ✔️ Animal behaviour logic
- ✔️ Hunting mechanisms
- ✔️ Plant consumption
- ✔️ Breeding mechanics
- ✔️ Disease spread
- ✔️ Genetic inheritance

## 🔮 **Improvements**
Future improvements and features include:
- 📈 **See the average characteristic** of each species.
- 🦌 **Additional species-specific traits** (e.g., movement patterns, field of view).
- 🌦️ **Seasonal changes affecting food availability and survival rates.**

## 📜 **License**
This project operates under the **GNU General Public License v3.0**. The **[LICENSE](https://choosealicense.com/licenses/gpl-3.0/)** file provides details.

