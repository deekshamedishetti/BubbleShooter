# Bubble Shooter Game

This project is a simple Bubble Shooter game developed using Java Swing. The player shoots colored bubbles to form clusters and score points.

## Description

The game displays a shooter bubble at the bottom of the screen. The player moves the bubble horizontally and shoots it upward. When three or more bubbles of the same color connect, they are removed from the screen.

## Controls

Left Arrow key moves the bubble to the left  
Right Arrow key moves the bubble to the right  
Space Bar shoots the bubble

## Rules

Bubbles are shot upward and attach to existing bubbles.  
Three or more connected bubbles of the same color are removed.  
Each removed bubble gives ten points.  
The game ends when bubbles reach the bottom or when too many bubbles appear.

## Features

Randomly generated bubble colors  
Smooth animation using Swing Timer  
Automatic cluster detection  
Score calculation  
Game over detection

## Technologies Used

Java  
Java Swing  
AWT

## Project Structure

BubbleShooter.java  
README.md

## How to Run

Ensure Java JDK 8 or higher is installed.

Compile the program  
javac BubbleShooter.java

Run the program  
java BubbleShooter

## Game Logic

The game uses a Swing Timer for continuous updates.  
Bubble collisions are detected using distance calculations.  
Clusters are identified using recursive search.  
Score and game state are updated dynamically.

## Future Enhancements

Add angled shooting  
Add sound effects  
Add multiple levels  
Add pause and restart options

## Author

Developed as a Java Swing mini-game project for learning purposes.

## License

This project is intended for educational use.
