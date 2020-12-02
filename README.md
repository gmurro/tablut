# :chess_pawn: Tablut Challenge 2020

<img align="right" width="280" height="280" src="src/it/unibo/ai/didattica/competition/tablut/gui/resources/screen.png">

**_Tablut_** is an ancient board game which was popular in nothern Europe and whose story is still uncertain.
The game board is grid of 9x9 squares where two players alternate in moving their checkers:
* attacker (black soldiers) 
* defender (white soldiers and one king)   

This repository contains an Java-based intelligent agent able to play to the game and it was designed to take part in the Tablut Students Challenge 2020 of the Fundamentals of Artificial Intelligence and Knowledge Representation course held at the University of Bologna.                   
The agent is based on one of the many versions of the rules for this game, known as "Ashton Tablut" (available in this [paper](http://ww.aagenielsen.dk/LinnaeusPaper-Longer.pdf)) and it interact with a Server maintained by tutor [Andrea Galassi](https://github.com/AGalassi/TablutCompetition).


## Download

First of all, you need to download the project through cloning.

```sh
git clone https://github.com/gmurro/Tablut.git
cd Tablut
```

## Requirements

You need to have JDK >= 11. From Ubuntu/Debian console, you can install it with these commands:
```sh
sudo apt update
sudo apt install openjdk-11-jdk -y
```

## Run

To start playing, you will simply move in `jars` directory and run the Server with:
```sh
java -jar Server.jar
```
Then you will run either black and white artificial intelligent player:
```sh
./runmyplayer black 60 localhost
```
```sh
./runmyplayer white 60 localhost
```

You can run players also with different parameters:
```sh
./runmyplayer <role> <timeout-in-seconds> <server-ip> <debug>
    
    <role> : role of the player in the game (black or white, it is mandatory)
    <timeout-in-seconds> : time taken by the player to find the best moves (default: 60)
    <server-ip> : ip address of the server (default: localhost)
    <debug> : if this argument is explicit, player print logs during the search of the next move 
```


## Resources & Libraries

* Java 8 [API](https://docs.oracle.com/javase/8/docs/api/)
* Gson 2.2.2 [library](https://www.javadoc.io/doc/com.google.code.gson/gson/2.2.2/com/google/gson/Gson.html)
* AIMA-code [library](https://github.com/aimacode/aima-java)

## Versioning

We use Git for versioning.

## Group members

<img align="left" width="292" height="214" src="src/it/unibo/ai/didattica/competition/tablut/gui/resources/logo.png">

> The name of group enrolled in the Tablut Students Challenge 2020 that presented this project is **_brAInmates_**.

|  Reg No.  |  Name     |  Surname  |     Email                              |    Username      |
| :-------: | :-------: | :-------: | :------------------------------------: | :--------------: |
|   997317  | Giuseppe  | Murro     | `giuseppe.murro@studio.unibo.it`       | [_gmurro_](https://github.com/gmurro)         |
|   985203  | Salvatore | Pisciotta | `salvatore.pisciotta2@studio.unibo.it` | [_SalvoPisciotta_](https://github.com/SalvoPisciotta) |
|  1005271  | Giuseppe  | Boezio    | `giuseppe.boezio@studio.unibo.it`      | [_giuseppeboezio_](https://github.com/giuseppeboezio) |



## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

