Joel Sprinkle
ID# 100960023
SYSC 3303
Assignment 01

——Purpose
This project is composed of 3 systems that send messages between each other, using datagram packets and datagram sockets. The messages are sent using the UDP/IP protocols.


——Java Files
Server.java	
	- Is the server program. It receives messages (read/write requests) from the client (intermediate host), then parses the received message and formulates a response back to the client.
	-Continuously listens on port 69 for new incoming messages.
InterHost.java
	- Is the intermediate host program. It receives messages from the client and passes them along to the server (and vice versa).
	-Continuously listens on port 68 for new incoming messages.
Client.java	
	- Is the client program. It sends messages (read/write requests) to the server (intermediate host), then receives responses back from the server 


——Setup instructions:
Import the project into Eclipse
	-Should be able to import project as: 
	General -> Projects into Workspace 


——Run instructions:
The Server needs to be run first.
The InterHost needs to be run second.
The Client needs to be run last.
	-As soon as the client is executed it will start sending messages


——Issues:
There are no known issues at this time.
