# How to launch
## Option 1
1. Go to JetBrains idea
2. Select this project
3. Compile & run Main.java in src/main/java/vu/oop/passwordmanager/app/Main.java
## Option 2
Run this command in the root directory of the project. 
```bash
./mvnw clean javafx:run
```


# Project structure
## Main classes for program
Main java classes are placed inside ``"src/main/java/lt/vu/passwordmanager/"``
1. app/             ← Main class, entry point (e.g., `Main.java`)
2. controller/      ← JavaFX controllers (for FXML)
3. service/         ← Business logic, password generation/encryption 
4. db/              ← Database logic (SQLite handling)
5. util/            ← Helpers and utilities
## Program resources
Are resources are placed inside ``"src/main/resources/vu/oop/passwordmanager/"``.
1. FXMLFiles/       ← FMXL files
2. icons/           ← Icons

# Contacting
andrius.kolenda@mif.stud.vu.lt

Kelecius.dovydas@gmail.com