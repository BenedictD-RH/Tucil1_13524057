# Tucil1_13524057
## About this template

The Queens Solver is a program to find a solution to a board of a Queens puzzle game by using a brute force algorithm.

## Requirements

Before building and running the **Matrix Calculator**, make sure you have the following installed:

Before building and running the **Matrix Calculator**, make sure you have the following installed:

### Java
- **Version:** 17 or higher
- **Download links:**
  - [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads)

### Maven
- **Version:** 3.2.5 or higher (recommended 3.6.3+)
- **Download links:**
  - [Direct Apache Maven Official Downloads](https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip)

### Additional installation info

### Windows
For maven installation, download the .zip and it should contain a directory with
```
apache-maven-<version>/
├── bin/               <-- executable scripts (mvn, mvn.cmd)
├── boot/         
├── conf/          
├── lib/          
├── NOTICE
├── LICENSE
├── README.txt
```

Put bin/ in environment PATH to use in terminal. [Add folder to PATH tutorial](https://www.youtube.com/watch?v=pGRw1bgb1gU)

### Linux
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
sudo apt install maven -y
```

### MacOS
```bash
brew install openjdk@17
brew install maven
```


## Project Structure

```bash
Tucil1_13524057/
├──bin/
├──data/
|   ├──input/
|   └──results/
|       ├──png/
|       └──txt/
├──docs/
├──src/
|   └──main/
|       ├──java/
|       |   ├──controllers/
|       |   |   ├──BoardController.java
|       |   |   └──SolverTask.java
|       |   ├──helper/
|       |   |   └──ColorPicker.java
|       |   ├──modules/
|       |   |   ├──Board.java
|       |   |   ├──Solver.java
|       |   |   └──Tile.java
|       |   └──App.java
|       └──resources/
|           ├──crown_icon.png
|           ├──leftbutton.png
|           ├──mainscreen.fxml
|           └──style.css
├──.gitignore
├──pom.xml
└──README.md
```


## How to run
1. Compiling the program
```bash
mvn clean compile
```

2. Running the program (Windows)
```bash
mvn package
cd target
javaw -jar queens-solver-1.0-SNAPSHOT-shaded.jar
```

## Using the Application
1. Input through GUI
Board dimension can be adjusted according to the input in the bottom left corner. To change the color of tiles on the board, select a color on the left side of the interface, then click a tile on the board change it to the selected color. Press `CTRL` while moving the mouse to drag the selected color across the board. The board will only be solvable if there is no blank spaces left and every color is present atleast once on the board. To solve the board, press the `Solve` button.

2. Input through .txt file
Inputting through .txt files can be done by creating the file in the `data/input` directory. The .txt files needs to be valid to be inputted. Once created, the file can be inputted by pressing the `data/input` directory. or through typing its name in the text field above it.

3. Saving the result as an image
After a board is solved and an answer is found, the result can be saved through pressing the `Save Result As` button. The .png file will be stored in the `data/results/png` directory.

4. Misc. Features
- To reset the board to a blank state, press the `Reset Board` button.
- To turn on Colorblind Mode, press the `Colorblind Mode` button. This will display the assigned character that represents a color.

## Contributors
Benedict Darrel Setiawan - 13524057
