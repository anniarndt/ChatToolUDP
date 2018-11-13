package broadcast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BroadcastChat extends JFrame {

	private static final long serialVersionUID = 1L;
	private utils.KeyboardListener inputListener;
	private String nick;
	private DatagramSocket socket;
	private InetAddress adress;
	private static int port = 4446;

	public BroadcastChat() throws SocketException {

		JPanel mainPanel;

		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		nick = retrieveNickName();
		setTitle("Chat Tool von " + nick);
		mainPanel = setupChatView();
		getContentPane().add(mainPanel);
		getContentPane().getParent().invalidate();
		getContentPane().validate();
		getContentPane().revalidate();
		waitforMessage();

	}

	private JPanel setupChatView() {
		JPanel panel = new JPanel();
		JPanel southPanel = new JPanel();
		JMenuBar bar = new JMenuBar();
		panel.setAutoscrolls(true);
		JScrollBar scrollbar = new JScrollBar(JScrollBar.VERTICAL, 30, 10, 0, 100);
		scrollbar.setForeground(Color.MAGENTA);

		JMenu menu0 = new JMenu("Start");
		JMenu menu1 = new JMenu("Bearbeiten");
		JMenuItem item01 = new JMenuItem("Speichern");
		JMenuItem item03 = new JMenuItem("Letzten Chat laden");
		JMenuItem item02 = new JMenuItem("Beenden");
		JMenuItem item1 = new JMenuItem("Farbe");
		JMenuItem item2 = new JMenuItem("Fenstergröße");
		JMenuItem item3 = new JMenuItem("Sprache");
		JTextArea textArea = new JTextArea();

		item02.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});
		item03.addActionListener(new ActionListener() {
			JFileChooser chooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int userSelection = chooser.showOpenDialog(null);
				chooser.setDialogTitle("Specify a file to open");
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					System.out.println("die zu öffnende Datei ist" + chooser.getSelectedFile().getAbsolutePath());
					String buffer = null;
					buffer = "";
					BufferedReader br = null;
					String line = null;
					try {

						br = new BufferedReader(new FileReader(new File(chooser.getSelectedFile().getAbsolutePath())));
						while ((line = br.readLine()) != null) {
							buffer = buffer.concat(line);
							// fehlen nur noch die Zeilenumbrüche, dass diese auch mitkommen

						}
						br.close();
					} catch (IOException e) {
						// TODO Automatisch generierter Erfassungsblock
						e.printStackTrace();
					}

					inputListener.inputReceivedTotal(buffer, nick);

				}

			}

		});

		menu1.add(item1);
		menu1.add(item2);
		menu1.add(item3);
		menu0.add(item01);
		menu0.add(item03);
		menu0.add(item02);

		bar.add(menu0);
		bar.add(menu1);
		panel.add(bar);

		setJMenuBar(bar);

		final JTextField textField = new JTextField();
		JFileChooser chooser = new JFileChooser();
		item01.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int userSelection = chooser.showSaveDialog(null);
				chooser.setDialogTitle("Specify a file to save");

				String name = textArea.getText();
				try {

					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToSave = chooser.getSelectedFile();
						FileWriter fw = new FileWriter(fileToSave);
						fw.write("Name " + name + "\r\n");
						fw.flush();
						fw.close();
						System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					}

				} catch (IOException e) {
					// TODO Automatisch generierter Erfassungsblock
					e.printStackTrace();
				}

			}

		});
		JButton sendButton = new JButton("Send");
		this.getRootPane().setDefaultButton(sendButton); // dann wird mit Enter die Nachricht verschickt
		textField.setColumns(60);

		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// hier passiert das Versenden einer Nachricht

				
				try {
					socket = new DatagramSocket();
					socket.setBroadcast(true);
					InetAddress adress = InetAddress.getByName("255.255.255.255");
					String broadcastMessage = textField.getText();
					byte[] buffer = broadcastMessage.getBytes();
					System.out.println("Inhalt des Textfeldes ist" + broadcastMessage);
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adress, 4445);
					socket.send(packet);
					System.out.println("der socket sendet ein Datagramm");
					socket.close();
				
					

				} catch (IOException e1) {
					// TODO Automatisch generierter Erfassungsblock
					e1.printStackTrace();
				}
				textField.setText("");

			}

		});

		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.white);
		textArea.setEditable(false);

		southPanel.setLayout(new FlowLayout());
		southPanel.add(textField);
		southPanel.add(sendButton);

		panel.setLayout(new BorderLayout());
		panel.add(scrollbar, BorderLayout.EAST);
		panel.add(textArea, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);

		// this is just an example, please modify for your listeners accordingly...
		// listener = new SocketListener(sock,textArea);
		inputListener = new utils.KeyboardListener(textArea, nick); // überladenen Konstruktor

		return panel;
	}

	private String retrieveNickName() {
		return (String) JOptionPane.showInputDialog(this, "Enter your nickname please:", "Enter nickname", JOptionPane.QUESTION_MESSAGE);
	}

	public void waitforMessage() throws SocketException {
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
				inputListener.inputReceivedTotal(byteToString(data), nick);
				System.out.println("Anfrage von " + address);
				System.out.println(byteToString(data));
			} catch (IOException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		}

	}

	public static String byteToString(byte[] bytearray) {
		String s = new String(bytearray);
		return s;

	}


	public static void main(String[] args) throws SocketException {
		new BroadcastChat();

	}

}
