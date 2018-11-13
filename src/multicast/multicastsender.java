package multicast;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class multicastsender {
	public static void main(String[] args) {
		
		
		try {
			System.out.println("wurde gestartet");
			InetAddress group =  InetAddress.getByName("224.0.0.1");
			MulticastSocket multisock = new MulticastSocket();
			String message = "hello";
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, group, 2345);
			multisock.send(packet);
			multisock.close();
		} catch ( IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
	}

}
