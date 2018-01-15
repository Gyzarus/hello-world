

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ComClient extends JFrame {
	JTextArea incoming;
	JTextField outgoing;
	static String clientName;
	private static MulticastSocket ds;
	private int port = 8088;
	String host = "228.5.6.7";
	InetAddress group = null;
	private String remoteHost = "localhost";
	
	public ComClient(String str) throws Exception {
		super(str+"登陆中的公共聊天室");
		clientName=str;
		ds = new MulticastSocket(port);
		group = InetAddress.getByName(host);
		ds.joinGroup(group);
		clientName = str;

		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 20);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);

		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		outgoing = new JTextField(20);

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());

		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);

		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		this.getContentPane().add(BorderLayout.CENTER, mainPanel);
		this.setSize(400, 350);
		this.setVisible(true);

	}

	public static void main(String[] args) throws Exception {
		ComClient sc = new ComClient("test");
	}

	public class IncomingReader implements Runnable {
		public void run() {
			String input;
			try {
				while (true) {
					DatagramPacket inputPacket = new DatagramPacket(new byte[512], 512);
					ds.receive(inputPacket);
					input = new String(inputPacket.getData(), 0, inputPacket.getLength());
					input = caesar.encode(input);
					incoming.append(input + "\n");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public class SendButtonListener implements ActionListener {
		InetAddress group = null;

		public void actionPerformed(ActionEvent ev) {
			try {
				group = InetAddress.getByName(host);
				InetAddress address = InetAddress.getByName(remoteHost);
				String msg =caesar.code(clientName+" :"+outgoing.getText());
				byte[] buffer = msg.getBytes();
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
				ds.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void finalize() {
		ds.close();
	}
}