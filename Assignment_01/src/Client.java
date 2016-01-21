/* The client program sends read and 
 * write requests to the server program.
 */

import java.io.*;
import java.net.*;

public class Client {
	
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	
	
	// constructor
	public Client() {
		try {
			// Construct a Datagram socket and bind it to any available socket
			// Socket will send and receive UDP Datagram packets.
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) {   // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	
	// Converts the message to be sent into a byte array
	// Char rwe: read request = r, write request = w, error = e
	private void byteMsg(byte[] array, char rwe, String fileN, String mode) {
		int i = 0;
		
		array[i] = (byte) 0; // leading zero
		i++;
		
		// set whether message request is a read/write/error request
		if (rwe == 'r') {
			array[i] = (byte) 1; // read
		} else if (rwe == 'w') { 
			array[i] = (byte) 2; // write
		} else {
			array[i] = (byte) 0; // error
		}
		i++;
		
		// add in the filename
		for (byte b : fileN.getBytes()) {
			array[i] = b;
			i++;
		}
		
		array[i] = (byte) 0; // add zero byte spacer
		i++;
		
		// add in the mode
		for (byte b : mode.getBytes()) {
			array[i] = b;
			i++;
		}
		
		array[i] = (byte) 0; // add trailing zero byte
	}
	
	
	// prints out the message to be sent in bytes (HEX)
	private void printByteMsg(byte[] bMsg, int len) {
		System.out.println("\nClient: Packet in bytes - represented as HEX:");
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
	
	
	// Sends Datagram packets to the server 
	// Prints out the packets details for each
	private void sendPacket(byte[] bMsg) {
		// Create the Datagram packet
		try {
			sendPacket = new DatagramPacket(bMsg, bMsg.length,
										InetAddress.getLocalHost(), 1024); // CHANGE BACK TO 68 !!!!!!!!!!!!!!!!!!!!!!!!!!
		} catch (UnknownHostException e) {
			e.printStackTrace();
				System.exit(1);
			}
				
		// output the sending packet info
		System.out.println("\nClient: Sending packet...");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		System.out.println("Length: " + sendPacket.getLength());

		// Send the Datagram packet to the server via the send/receive socket. 
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Client: Packet sent.");	
	}
	
	
	// Receive a Datagram packet on the sendReceive socket
	// and print out the packets details 
	private void receivePacket() {
		// Wait for incoming Datagram packet
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a Datagram is received via sendReceiveSocket.  
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process and output the received Datagram.
		System.out.println("\n------------------------");
		System.out.println("Client: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Length: " + receivePacket.getLength());
		this.printByteMsg(data, receivePacket.getLength());
		System.out.println("\n");		
	}
	
	
	// Send and receive Datagram Packets
	// This is where the hard work happens
	public void sendAndReceive() {
		// basic info
		String fileN = "test.txt";
		String mode = "netascii";
		String rType = "Read request";
		char wre = 'r';

		// send 11 packets to the server - 5 read requests, 5 write requests, 1 error 
		for (int i = 0; i < 11; i++) {
			// type of packet being sent
			if (i % 2 == 0) { 	// write
				rType = "Read request";
				wre = 'r';
			} else { 			// read
				rType = "Write request";
				wre = 'w';
			}
	
			if (i == 10) {		// error
				rType = "ERROR";
				wre = 'e';
			}
			
			System.out.println("--------------------------------------------------------");
			System.out.println("--------------------------------------------------------");
			System.out.println("Client: Preparing to send packet #" + (i+1) + " - " + rType);
		
			// create and convert message into bytes
			byte[] bMsg = new byte[4 + fileN.length() + mode.length()]; // 4, for bytes before/between/after strings
			this.byteMsg(bMsg, wre, fileN, mode);
		
			// print the message as a string and in bytes
			System.out.println("\nClient: Packet as a string:");  
			System.out.println(new String(bMsg,0,bMsg.length));
			this.printByteMsg(bMsg, bMsg.length);
		
			// send the packet to port 68 (the well known port)
			this.sendPacket(bMsg);
			
			// Wait for incoming Datagram packets
			this.receivePacket();	
		} // end of for loop

		// Finished, so close the socket. 
		// BUT we will never get here because of sending error packet!
		sendReceiveSocket.close();  
	}
	
	
	// MAIN
	public static void main(String[] args) {
		Client c = new Client();
		c.sendAndReceive();
	}

}
