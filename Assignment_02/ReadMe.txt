Joel Sprinkle
ID# 100960023
SYSC 3303
Assignment 02


——Purpose
This project lets chefs make sandwiches out of 3 ingredients - bread, peanut butter, and jam.
There is an agent which has unlimited availability of all 3 ingredients. The agent puts 2 out of the 3 ingredienta onto a table. 
There is also three chefs, each with an unlimited supply of only one of the 3 ingredients, each chef has a different unlimited ingredient. The chef who has the unlimited supply of the ingredient that the agent did not place on the table, takes the ingredients from the table (leaving the table empty) makes a sandwich and eats it.
This process of the agent supplying 2 ingredients and the chefs taking the ingredients to makes sandwiches repeats - 20 times total.


——Java Files
Agent.java
Contains the code to run an agent thread, which randomly decides which 2 ingredients to place on the table when it is empty.
-The thread dies after putting ingredients on the table 20 times.

Chef.java
Contains the code to run a chef thread, which takes ingredients from the table (leaving it empty) to make a sandwich with and then eat it.
In one step the chef takes the ingredients, and in another step (when the chef process becomes active again) the chef makes and eats the sandwich with the ingredients it took earlier.
-The thread dies after detecting that Agent thread has terminated and the thread cannot make a sandwich with any of the ingredients that may be on the table.

Kitchen.java
Contains the main. Sets up the agent and the chef threads to start making sandwiches. As well as creating the table object the agent and chef threads interact with.

Table.java
Contains the code to hold 2 ingredients as well as the code to synchronize the agent and chef threads so that only one thread is adding to or removing from the table at a time.


——Setup instructions:
Import the project into Eclipse
  -Should be able to import project as: General -> Existing Projects into Workspace 


——Run instructions:
Run the kitchen class as a Java Application.


——Issues:
There are no known issues at this time.


——Diagrams:
The class diagram was generated using the ObjectAid UML Diagram plugin for eclipse.
All other diagrams were built using https://www.draw.io


——Learning:
This project is multi-threaded and contains a class that is synchronized between 4 thread.
