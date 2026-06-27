# 🇮🇳 India Local Event & Festival Tracker

A Java Swing desktop application for tracking local events and festivals across India, backed by a MySQL database. Built with core OOP principles: **Encapsulation**, **Abstraction**, **Inheritance**, and **Polymorphism**.

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [OOP Design](#oop-design)
- [Database Setup](#database-setup)
- [How to Run](#how-to-run)
- [Usage Guide](#usage-guide)
- [Pre-Seeded Events](#pre-seeded-events)

---

## ✨ Features

- **View All Events** — Browse all upcoming Indian events in a sortable table
- **Add New Event** — Submit events with fields for name, location, date, state, type, category, organizer, and description
- **Search Events** — Keyword search across name, state, type, and location; quick-filter chips for popular categories and states
- **Delete Events** — Remove events directly from the events table
- **Auto-Seeding** — 15 real Indian events pre-loaded on first launch
- **Dark UI Theme** — Saffron/gold accent palette inspired by the Indian tricolour
- **Animated Sidebar** — Animated accent line and hover effects on navigation buttons
- **Focus Glow Fields** — Input fields highlight with accent colour on focus

---

## 🛠 Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Language   | Java (JDK 8+)                       |
| GUI        | Java Swing (CardLayout, GridBagLayout) |
| Database   | MySQL 8+                            |
| JDBC Driver| mysql-connector-j-9.6.0.jar         |
| IDE        | VS Code (with Java Extension Pack)  |

---

## 📁 Project Structure

```
LOCALEVENTTRACKER/
├── lib/
│   └── mysql-connector-j-9.6.0.jar     # MySQL JDBC driver
├── src/
│   ├── Main.java                        # Entry point
│   ├── Models/
│   │   ├── Event.java                   # Abstract base class
│   │   ├── Concert.java                 # Extends Event
│   │   ├── Festival.java                # Extends Event
│   │   ├── SportsEvent.java             # Extends Event
│   │   └── CulturalEvent.java           # Extends Event
│   ├── database/
│   │   ├── DBConnection.java            # MySQL connection singleton
│   │   └── EventDAO.java                # CRUD operations + seeder
│   └── gui/
│       ├── MainFrame.java               # Main window with sidebar nav
│       ├── ViewEventsPanel.java         # All Events tab
│       ├── AddEventPanel.java           # Add Event form tab
│       ├── SearchPanel.java             # Search tab with filter chips
│       ├── AnimatedButton.java          # Custom hover-animated button
│       └── Theme.java                   # Colours, fonts, type icons
└── vscode/
    └── settings.json                    # Java classpath config
```

---

## 🧱 OOP Design

### Abstraction
`Event` is an abstract base class. It declares two abstract methods — `getEventType()` and `getEventIcon()` — that every subclass must implement, hiding implementation details behind a common interface.

### Encapsulation
All fields in `Event` (id, name, location, date, state, description) are `private`, accessed only through public getters and setters.

### Inheritance
Four concrete classes extend `Event`, each adding type-specific fields:

| Class          | Extra Fields              | Icon |
|----------------|---------------------------|------|
| `Concert`      | artist, genre             | 🎵   |
| `Festival`     | festivalType              | 🎉   |
| `SportsEvent`  | sport, teams              | 🏆   |
| `CulturalEvent`| artForm, organizer        | 🎭   |

### Polymorphism
Each subclass overrides `getEventType()`, `getEventIcon()`, and `toString()`, so the same method calls return type-specific results at runtime.

---

## 🗄 Database Setup

### 1. Create the Database

```sql
CREATE DATABASE event_tracker;
USE event_tracker;
```

### 2. Create the Events Table

```sql
CREATE TABLE events (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    location    VARCHAR(255)  NOT NULL,
    date        DATE          NOT NULL,
    state       VARCHAR(100),
    description TEXT,
    event_type  VARCHAR(50),
    extra1      VARCHAR(255),
    extra2      VARCHAR(255)
);
```

> **Column mapping:**
> - `extra1` → category / artist / sport / art form (depending on event type)
> - `extra2` → organizer / genre / teams (depending on event type)

### 3. Update Credentials

Open `src/database/DBConnection.java` and update:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/event_tracker";
private static final String USER     = "root";
private static final String PASSWORD = "your_password_here";
```

---

## ▶ How to Run

### Prerequisites
- JDK 8 or higher
- MySQL Server running locally on port `3306`
- VS Code with **Extension Pack for Java** (or any Java IDE)

### Steps

1. **Clone or unzip** the project.

2. **Add the JDBC driver** to your classpath. In VS Code, ensure `vscode/settings.json` points to the correct path of `mysql-connector-j-9.6.0.jar`:
   ```json
   {
     "java.project.referencedLibraries": [
       "lib/mysql-connector-j-9.6.0.jar"
     ]
   }
   ```

3. **Set up the database** using the SQL commands in the [Database Setup](#database-setup) section above.

4. **Run `Main.java`** — the app will launch, connect to MySQL, and auto-seed 15 Indian events if the table is empty.

### Compile & Run from Terminal

```bash
# From the project root
javac -cp lib/mysql-connector-j-9.6.0.jar -d out src/Main.java src/Models/*.java src/database/*.java src/gui/*.java

java -cp "out;lib/mysql-connector-j-9.6.0.jar" Main
# On macOS/Linux use : instead of ; in the classpath
java -cp "out:lib/mysql-connector-j-9.6.0.jar" Main
```

---

## 📖 Usage Guide

### All Events Tab (`📋 All Events`)
- Displays every event from the database sorted by date
- Click a row to select it; use the **Delete** button to remove it
- Events are colour-coded by type in the Type column

### Add Event Tab (`➕ Add Event`)
- Fill in the required fields (marked with `*`): Event Name, Location, Date
- Select a State from the dropdown (all 28 states + UTs included)
- Choose an Event Type: Festival, Concert, Sports, Cultural, Exhibition, Workshop, or Other
- Click **Add Event** — the app validates inputs and saves to the database, then redirects to the All Events view

### Search Tab (`🔍 Search`)
- Type any keyword (event name, state, type, location) and press **Enter** or click **Search**
- Use the **Quick Filter chips** for one-click searches:
  - By type: Festival, Concert, Sports, Cultural
  - By state: Maharashtra, Rajasthan, Kerala, Goa, Delhi
- Click **Clear** to reset results

---

## 🎪 Pre-Seeded Events

The app seeds 15 events on first launch:

| Event | Location | State | Type |
|-------|----------|-------|------|
| Sunburn Festival 2026 | Vagator Beach | Goa | Concert |
| IPL 2026 Final | Narendra Modi Stadium | Gujarat | Sports |
| Hornbill Festival | Kisama Heritage Village | Nagaland | Festival |
| Jaipur Literature Festival | Diggi Palace | Rajasthan | Cultural |
| Lollapalooza India | Mahalaxmi Racecourse | Maharashtra | Concert |
| Kerala Snake Boat Race | Punnamada Lake | Kerala | Sports |
| Delhi Comic Con | IARI Grounds Pusa | Delhi | Cultural |
| Thrissur Pooram | Vadakkumnathan Temple | Kerala | Festival |
| Rath Yatra Puri | Grand Road Puri | Odisha | Festival |
| Pushkar Camel Fair | Pushkar Grounds | Rajasthan | Festival |
| Kochi-Muziris Biennale | Fort Kochi | Kerala | Cultural |
| Ziro Music Festival | Ziro Valley | Arunachal Pradesh | Concert |
| Magnetic Fields Festival | Alsisar Mahal | Rajasthan | Concert |
| India International Trade Fair | Pragati Maidan | Delhi | Cultural |
| Durga Puja Kolkata | City Wide Pandals | West Bengal | Festival |

---

## 👨‍💻 Author

Built as a Java OOP project demonstrating Encapsulation, Abstraction, Inheritance, and Polymorphism with a real-world GUI and database integration.
