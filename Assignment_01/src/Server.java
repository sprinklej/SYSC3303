/* The server program accepts read
 * and write requests from the client
 * program.
 */

import java.io.*;
import java.net.*;

public class Server {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	
	
	// constructor
	public Server() {
		try {
			// Construct a Datagram socket and bind it to port 69
			// This socket will be used receive UDP Datagram packets.
			receiveSocket = new DatagramSocket(1025); //CHANGE BACK TO 69!!!!!!!!!!!!!!!!!!!!!!!!!!
	          
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} 	    
	}
	
	
	// prints out the message in bytes (HEX)
	private void printByteMsg(byte[] bMsg, int len) {		
		System.out.println("\nServer: Packet in bytes - represented as HEX:");
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
	
		
	// Receive a Datagram packet
	// print out the packets details 
	private void receivePacket(byte data[]) {
		// Block until a datagram packet is received from receiveSocket.
		try {        
			System.out.println("Waiting..."); // so we know we're waiting
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		// Process and output the received Datagram info
		System.out.println("\nServer: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		System.out.println("\nServer: Packet as a string:" );  
		System.out.println(new String(data, 0, receivePacket.getLength()));
		this.printByteMsg(data, receivePacket.getLength());
	}
	
	
	// Parse incoming packets for errors
	private void parsePacket(byte data[]) {
		System.out.println("\nServer: Parsing received packet.");
		if (data[0] != (byte) 0) { // check first byte - should == 0 
			System.out.println("ERROR - First byte incorrect.");
			System.exit(1);
		}
										
		if ((data[1] == (byte) 1) || (data[1] == (byte) 2)) { // check second byte - should == 1 or 2
			;//System.out.println("data[1] equals 1 or 2"); // for testing
		} else {
			System.out.println("ERROR - Second byte incorrect.");
			System.exit(1);
		}
	
		if (data[receivePacket.getLength() - 1] != (byte) 0) { // check last byte - should == 0
			System.out.println("ERROR - Last byte incorrect.");
			System.exit(1);
		}
	
		// check for a single "0" between the text fields (check middle byte) - should contain a single 0
		int cntr = 0;
		for (int i = 2; i < receivePacket.getLength() - 1; i++) { // 3rd element of array to second last element
			if (data[i] == (byte) 0) {
				cntr++;
			}	
		}
		if (cntr != 1) {
			System.out.println("ERROR - No 0 byte or to many 0 bytes between text strings.");
			System.exit(1);
		}
		
		System.out.println("Server: Parsing complete.");		
	}
	
	
	// Sends Datagram packets to the client
	// Prints out the packets details
	private void sendPacket(byte[] bMsg) {
		// create Datagram packet
		sendPacket = new DatagramPacket(bMsg, bMsg.length,
					receivePacket.getAddress(), receivePacket.getPort());

		System.out.println("Server: Sending packet:");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());
		this.printByteMsg(bMsg, bMsg.length);

		// Construct a Datagram socket to send Datagram packets
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		// Send the Datagram packet
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("\nServer: packet sent.\n");

		// Close the socket.
		sendSocket.close();			
	}
	
	
	// Receives Datagram packets from the client
	// Parses received packets
	// Sends Datagram packets to the client
	public void receiveAndRespond() {
		// Construct a DatagramPacket for receiving packets up 
		// to 100 bytes long 
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		
		int i = 1; // counter strictly for convenience
		while (true) { // Continuously wait for incoming packets
			System.out.println("--------------------------------------------------------");
			System.out.println("--------------------------------------------------------");
			System.out.println("Server: Waiting for Packet. Round #" + i);
			i++;
			
			// Wait to receive packet
			this.receivePacket(data);	
			
			// Parse received Datagram packet
			this.parsePacket(data);
			
			// Create response message to send back
			System.out.println("\n------------------------");
			System.out.println("Server: Preparing packet for response.");
			byte[] bMsg = new byte[4];
			if (data[1] == (byte) 1) { // read request
				bMsg[0] = (byte) 0;
				bMsg[1] = (byte) 3;
				bMsg[2] = (byte) 0;
				bMsg[3] = (byte) 1;
			}
		
			if (data[1] == (byte) 2) { // write request
				bMsg[0] = (byte) 0;
				bMsg[1] = (byte) 4;
				bMsg[2] = (byte) 0;
				bMsg[3] = (byte) 0;
			}
		
			// Create a new Datagram packet and send the message
			this.sendPacket(bMsg);			
		} // end of while loop
	}
	
	
	// MAIN
	public static void main(String[] args) {
		Server s = new Server();
		s.receiveAndRespond();
	}

}
