# CS 296-25 Honors Project

This repository contains the code we did in class.  You can
use it as a starting point for your own project if you want.

## Installation

Do a `git clone git@gitlab-beta.engr.illinois.edu:mattox/fa16-honors-project.git` if you have uploaded your secure shell public key to gitlab.  Otherwise, use `git clone https://gitlab-beta.engr.illinois.edu/mattox/fa16-honors-project.git`

## Usage

The main file you want to edit is `src/adventure/core.clj`.

To run the program, use `lein run` from the command line.


## License

Copyright © 2016 Mattox Beckman, free for personal and educational use.

Description:

Launch the game then you will be in the foyer. I have 15 rooms. 14 rooms are counted in the house and another one as the exit-forest. You cannot go to  forest without finding the key.

There are six ways for you to go in the house. North, south, west, east and upstair.

If you are in one room and you want to find what is in that room. There is a command search. 

search -- It will return what is in that room.

"the name of objects"-- to pick it up(ex: books). 

cook -- cook something in the kitchen.

unlock -- to open the box you find. There is a quiz for you to answer to get the things in the box. (answer: mile)

read -- you can read what is in the books.

map -- you can watch the map of the room. It may has some direction.

inventory -- check what is in your inventory.

exit -- to end the game.

eat -- to eat what you cook.

here is the map for the house:
                        basement
first floor:
            forest
            garden
bedroom     foyer       gallery
bathroom    grue-pen    gameroom
            backyard

second floor:
kitchen     livingroom  studyroom

third floor:
            attic


Sometime clojure is really confusing to me on some degree. I believe I can do a better game with more understanding on clojure. But it is very interesting to learn about such a language.

Xiyu Chen