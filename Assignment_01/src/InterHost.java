/* The intermediate host program intercepts
 * packets before passing them along to 
 * their final destinations.
 */

import java.io.*;
import java.net.*;

//import java.lang.*;

public class InterHost {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket receiveSocket, sendSocket, sendReceiveSocket;
	int clientPort, clientLen;
	InetAddress clientAddress;
	
	
	// constructor
	public InterHost() {
		try {
			// Construct a Datagram socket. This socket will be used to
	     	// send/receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket();
			
	      	// Construct a Datagram socket and bind it to port 68 
	     	// This socket will be used to receive UDP Datagram packets.
	     	receiveSocket = new DatagramSocket(1024);  //CHANGE BACK TO 68!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} 
	}
	
	
	// prints out the message to be sent in bytes (HEX)
	private void printByteMsg(byte[] bMsg, int len) {				
		System.out.println("\nIntermediate: Packet in bytes - represented as HEX:");
		for (int i = 1; i <= len; i++) {
			if (Integer.toHexString(bMsg[i-1]).length() == 1) {
				System.out.print("0");
			}
			// http://www.tutorialspoint.com/java/lang/integer_tohexstring.htm
			System.out.print(Integer.toHexString(bMsg[i-1]) + " ");
			if (i % 4 == 0) {
				System.out.print("\n");
			}
		}
		
	}
	
	
	// Receives packets from the client system
	private void receiveClientPacket(byte[] data) {
		// Block until a Datagram packet is received on the receiveSocket.
		try {        
			System.out.println("Waiting..."); // so we know we're waiting
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		// To be able to respond to the client
		clientAddress = receivePacket.getAddress();
		clientPort = receivePacket.getPort();
		clientLen = receivePacket.getLength();

		// Process and output the received Datagram
		System.out.println("\nIntermediate: Packet received:");
		System.out.println("From host: " + clientAddress);
		System.out.println("Host port: " + clientPort);
		System.out.println("Length: " + clientLen);
		System.out.println("\nIntermediate: Packet as a string:");  
		System.out.println(new String(data,0,clientLen));
		this.printByteMsg(data, clientLen);		
	}
	
	
	// Send packets to the client system
	public void sendClientPacket(byte[] data) {
		// Create packet and output its info
		sendPacket = new DatagramPacket(data, receivePacket.getLength(),
								clientAddress, clientPort);
		System.out.println("\n------------------------");
		System.out.println("Intermediate: Forwarding packet to CLIENT:");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());
		this.printByteMsg(sendPacket.getData(), sendPacket.getLength());


		// Construct a Datagram socket to send a packet to client
		try {
			sendSocket = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	
		// Send the Datagram packet
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\nIntermediate: packet sent to CLIENT.\n");
	
		// Close the socket.
		sendSocket.close();		
	}
	
	
	// Send packets to the server system
	public void sendServerPacket(byte[] data) {
		// Create packet and output its info
		sendPacket = new DatagramPacket(data, receivePacket.getLength(),
									receivePacket.getAddress(), 1025); // CHANGE BACK TO 69 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("\n------------------------");
		System.out.println("Intermediate: Forwarding packet to SERVER:");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());
		System.out.println("\nIntermediate: Packet as a string:");  
		System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
		this.printByteMsg(sendPacket.getData(), sendPacket.getLength());
	
		// Send the Datagram packet
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\nIntermediate: packet sent to SERVER.");	
	}
	
	
	// Receives packets from the server system
	public void receiveServerPacket(byte[] data) {
		// Block until a Datagram packet is received on the sendReceiveSocket.
		try { 
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process and output the received Datagram
		System.out.println("\n------------------------------");
		System.out.println("Intermediate: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		this.printByteMsg(data, receivePacket.getLength());		
	}
	
	
	// Receives packets from the client and passes
	//them along to the server. Or vice versa.
	public void receiveAndPassAlong() {
		// Construct a DatagramPacket for receiving packets up 
		// to 100 bytes long
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		
		int i = 1; // counter strictly for convenience
		while (true) { // Continuously wait for incoming packets
			System.out.println("--------------------------------------------------------");
			System.out.println("--------------------------------------------------------");
			System.out.println("Intermediate: Waiting for Packet. Round #" + i);
			i++;
			
			// Wait to receive packet from client
			this.receiveClientPacket(data);

			// Send (forward) packet to server
			this.sendServerPacket(data);

			// Wait to receive packet from server		
			this.receiveServerPacket(data);

			// Send (forward) packet to client
			this.sendClientPacket(data);	
		} 
	}
	
	
	// MAIN
	public static void main(String[] args) {
		InterHost ih = new InterHost();
		ih.receiveAndPassAlong();

	}

}
