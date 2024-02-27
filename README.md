#### Cotos Mihaela, 322CD
# Proiect GlobalWaves

## Overview
This project is a simplistic implementation of a music streaming platform such as Spotify. The realisation of this project helped me a lot in understanding and consolidating the basic concepts of OOP design and Java fundamentals.

The application is accessed by users. Every user has a player that executes commands. Also, every user has private playlists and liked songs that are visible to him.  All the public playlists, podcasts and songs are stored in a library that is accessible for everyone.

The project works like a back-end application. It takes the input as a JSON file and outputs the result also in a JSON file.

## Structure
* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker. 
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file.
* input/ - contains the tests and library in JSON format
* ref/ - contains all reference output for the tests in JSON format

#### Users - Factory Pattern
In this stage, functionalities of the initial User class were extended to create three types of users:

- Normal users: retaining methods implemented in the initial stage.
- Artists: users capable of adding albums and possessing their own associated page.
- Hosts: users able to add podcasts and having their own associated page.

The Factory Pattern was implemented to manage the specific functionalities of each user type. UserFactory declares the create method, overridden in specific classes (SimpleUserFactory, ArtistFactory, HostFactory), which returns the created user.

#### Pages - Strategy Pattern

A page structure was incorporated into the application to allow users to access different sections:

- HomePage
- LikedContentPage
- ArtistPage
- HostPage

The page system was implemented using the Strategy Pattern, whereby printCurrentPage is implemented specifically for each page. Each print has its own page that implements the common PrintPageStrategy interface, defining its print strategy (e.g., HomePageStrategy, LikedContentPageStrategy).
