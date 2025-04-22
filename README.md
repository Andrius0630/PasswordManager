# How to launch

1. Go to getbrains idea
2. Select this project
3. Compile & run Main.java in src/main/java/vu/oop/passwordmanager/app/Main.java
4. "controller" folder is for JavaFX UI

# Project structure
## Main clases for programm
Going by this path "src/main/java/lt/vu/passwordmanager/" you should place new classes
1. app/             ← Main class, entry point (e.g., `Main.java`)
2. controller/      ← JavaFX controllers (for FXML)
3. model/           ← Data models (User, Entry, etc.)
4. service/         ← Business logic, password generation/encryption 
5. db/              ← Database logic (SQLite handling)
6. util/            ← Helpers and utilities
7. And so on
## JavaFX scene configs
Going by this path "src/main/resources/vu/oop/passwordmanager/" you should find JavaFX scene configs