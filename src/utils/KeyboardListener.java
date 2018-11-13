package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class KeyboardListener implements InputListener {

	private Socket sock;
	private JTextArea area;
	private String nick;
	private String channel = "";
	private Socket clientsock;
	private boolean connection; // info, ob es schon verbunden ist.
	// Socket übergeben

	public KeyboardListener(JTextArea area, String nick) {
		// clientSocket muss mitgegeben werden
		this.area = area;
		this.nick = nick;

	}

	@Override
	public void inputReceived(String str) {
		System.out.println("inputreceived wird ausgeführt");

	}

	@Override
	public void inputReceivedTotal(String str, String nick) {
		area.append(nick + ": " + str + "\r\n");
		System.out.println("input received wird ausgeführt");

	}
}
