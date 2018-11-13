package broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class BroadCastServer implements Runnable {
	private DatagramSocket socket = null;
	private boolean running = true;
	static int port = 4445;

	public static void main(String[] args) throws SocketException {
		byte[] buf = new byte[256];
		System.out.println("Server wurde gestartet");
		DatagramSocket socket = new DatagramSocket(port);
		while (true) {

			try {
				System.out.println("Auf eine Anfrage wird gewartet");
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				socket.receive(packet);
				System.out.println("Ein Paket ist reingekommen");
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				int laenge = packet.getLength();
				byte[] data = packet.getData();
				System.out.println("Anfrage von " + address);
				//System.out.println(byteToString(data));
			} catch (IOException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			} // blockierende Methode
				// Auslesen des Empfängers

		}

	}

	@Override
	public void run() {
		// TODO Automatisch generierter Methodenstub

	}
	public static String byteToString(byte [] bytearray) {
		String s = new String (bytearray);
		return s;
		
	}

}
