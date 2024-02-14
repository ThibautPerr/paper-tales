# PAPER TALES
Author : Thibaut Perrouin

The aim of this project is to re-create the Paper Tales board game, including various strategies to simulate games.

## Requirements

- Java 8 or higher
- Maven

## How to run
    java -jar target/paper-tales-project-1.0-SNAPSHOT.jar nbGames logStartPhases logEndPhases

- numberOfGames is the number of games simulated (default : 1)
- logStartPhases is a boolean, to log the beginning of phases for every game (only if numberOfGames == 1, default : false)
- logEndPhases is a boolean, to log the end of phases for every game (only if numberOfGames == 1, default : false)