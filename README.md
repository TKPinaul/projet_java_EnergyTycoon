# Energy Tycoon - City Management Simulator

Energy Tycoon is a Java-based city simulation game where you act as the mayor of a growing city. Your primary goal is to manage the city's power grid while expanding residential areas and maintaining citizens' satisfaction.

## 🚀 Features

- **Complex Power Grid Management**: Choose between Solar, Wind, Coal, or Nuclear energy.
- **Dynamic Economy**: Collect taxes every 6 hours and receive nightly bonuses.
- **Urban Growth**: Manage 5 types of residences from small Houses to massive Skyscrapers.
- **Real-time Simulation**: Includes a full time cycle (hours, days, months) and weather-dependent energy production.
- **Maintenance & Upgrades**: Buildings require repairs and strategic upgrades to keep up with increasing energy demand.
- **Survival Mechanics**: Manage citizens' satisfaction to avoid Game Over.

## 🛠️ Technical Stack
- **Language**: Java 17+
- **GUI**: Swing (Graphics2D vector-based rendering)
- **Architecture**: Model-View-Controller (MVC)

## 👥 Project Team & Collaboration

This project was developed through a collaborative effort between **TEPE Paulin Kossi** and **BOTRE LARE Aboudou**, working both in-person and remotely. Due to our decentralized workflow, commit frequencies may vary between members.

### **TEPE Paulin Kossi**
- **Resources & Power Plants**: Management of energy production logic and resource types.
- **Building Types**: Definition and configuration of the different building categories.
- **Documentation**: Author of the technical documentation and project guide.
- **Shared Design**: Jointly defined business rules and UI layout.

### **BOTRE LARE**
- **Building Management**: Implementation of the building lifecycle and states.
- **Simulation**: Development of the core simulation loop and timing logic.
- **Player & City Model**: Contributed to the design of the `Player` and `City` data structures.
- **Shared Design**: Jointly defined business rules and UI layout.

## 🎮 How to Run
The application entry point is the `Main.java` file.

1. Ensure you have Java 17 or higher installed.
2. Compile the source files.
3. Run `src/com/inf2328/energytycoon/Main.java`.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


---
Developed as part of the INF2328 Project.
