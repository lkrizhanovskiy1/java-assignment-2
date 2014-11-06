package try2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
//import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Calendar;
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class clientGUI  implements ActionListener  {
	
	private JLabel jlab, jt, uMsg;
	private JFrame jfrm;
	private JButton a,b,c;
	private JPanel jp;
	private static JTextArea textAr;
	private TextField textFld;
	static String acc=null;
	Socket clientSocket;
	DataOutputStream dosToServer;
	static DataInputStream  disFromServer;


	clientGUI() throws IOException {
		jfrm = new JFrame("Search the word in text program");
		jfrm.setSize(550, 450);
		jfrm.getContentPane().setLayout(new FlowLayout());
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//jp = new JPanel();
		//jp.setBackground(Color.RED);
		jlab = new JLabel ("Enter the text");
		jt = new JLabel ("         Enter word for search    ");
		textAr = new JTextArea(10, 40);
		textFld = new TextField("", 30);
		
		
		a = new JButton("  Connect  ");
		b = new JButton("  Disconnect  ");
		c = new JButton("  Append  ");
		
		a.addActionListener(this);
		b.addActionListener(this);
		c.addActionListener(this);
		textFld.addActionListener(this);
		b.setEnabled(false);
		c.setEnabled(false);
		textAr.setLineWrap(true);
		textAr.setWrapStyleWord(true);
						
		jfrm.getContentPane().add(jlab);
		jfrm.getContentPane().add(textAr);
		jfrm.getContentPane().add(a);
		jfrm.getContentPane().add(b);
		jfrm.getContentPane().add(c);
		jfrm.getContentPane().add(jt);
		jfrm.getContentPane().add(textFld);
		
		jfrm.setVisible(true);
		
	}

	public static void main(String[] agrs) throws IOException {
		new clientGUI();
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource()==a){


			System.out.println("Welcome to Client side");
			try {
				clientSocket = new Socket( InetAddress.getByName( "localhost" ),
						8000 );

				System.out.println( "Connected to " +
						clientSocket.getInetAddress().getHostName() );
				dosToServer = new DataOutputStream(
						clientSocket.getOutputStream() );

				disFromServer= new DataInputStream(
						clientSocket.getInputStream() );

				System.out.println( "I/O streams connected to the socket" );

				b.setEnabled(true);
				c.setEnabled(true);
				a.setEnabled(false);

			} catch( IOException ioe ) { 
				System.out.println( "I/O errors in socket connection" );
			}

		}
		if (e.getSource() ==b){
			try {
				clientSocket.close();
				System.out.println("Connection Closed");
				a.setEnabled(true);
				b.setEnabled(false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error");
			} 
		}
		if (e.getSource() ==c) {
			String str= "textAr" + textAr.getText();
			try {
				dosToServer.writeUTF(str);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("string from textAr was not sent");
			}
		}

		if (e.getSource()==textFld) {
			String st = "textFld" + textFld.getText();
			System.out.println("1");
			try {
				System.out.println("2");
				dosToServer.writeUTF(st);
				//dosToServer.flush();
				System.out.println("word to search sent to server");
			}catch(IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("string from textFld was not sent");
			}
			System.out.println("3");
			try {
				System.out.println("4");
				System.out.println("Doing ReadUTF");
				acc=disFromServer.readUTF();
				textAr.setText(acc);
			} catch (IOException e1) {
				System.out.println("ERROR BLYAT");
			}
		}		
	}
}






