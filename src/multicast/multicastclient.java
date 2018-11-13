package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class multicastclient {
public static void main(String[] args) {
	try {
		System.out.println("Client wurde gestartet");
		InetAddress inet = InetAddress.getByName("224.0.0.1");
		MulticastSocket multisocket = new MulticastSocket(2345);
		multisocket.joinGroup(inet);
		byte[] buffer = new byte [256];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		multisocket.receive(packet);
		System.out.println(new String (buffer));
		multisocket.close();
		} catch ( IOException e) {
		// TODO Automatisch generierter Erfassungsblock
		e.printStackTrace();
	}
	
}
}
