import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.nio.CharBuffer;

import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
public class SimpleSPPServer {
	
	private JFrame mainFrame;
	private JEditorPane editorPane;
 private  JScrollPane editorScrollPane;
	
	public SimpleSPPServer() throws IOException{
	      prepareGUI();
	      prepareServer();
	}
	
	public static void main(String[] args) throws IOException  {
		SimpleSPPServer simpleServer = new SimpleSPPServer();  
	      			      						 
	}
	
	 private void prepareServer() throws IOException{
		 
		// display local device address and name
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			System.out.println("Address: " + localDevice.getBluetoothAddress());
			System.out.println("Name: " + localDevice.getFriendlyName());
			
			append("\nAddress: " + localDevice.getBluetoothAddress());	
			append("\nName: " + localDevice.getFriendlyName());
			
			(new Thread(new WifiSocketServer(this))).start();
			
			startServerBluethooth();
	 }
	 
	 private void prepareGUI(){
		 
	      mainFrame = new JFrame("SmartMouse Server");
	      mainFrame.setSize(400,505);
	      mainFrame.setLayout(new GridLayout(3, 1));
	      
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	      });    

	      editorPane = new JEditorPane();
	      editorPane.setEditable(false);
	      editorScrollPane = new JScrollPane(editorPane);
	      editorScrollPane.setVerticalScrollBarPolicy(
	                      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      editorScrollPane.setPreferredSize(new Dimension(250, 500));
	      editorScrollPane.setMinimumSize(new Dimension(10, 10));
	      
	      mainFrame.add(editorScrollPane);
	      mainFrame.setVisible(true);  
	   }
	 
		// start server
		private void startServerBluethooth() throws IOException {
			
			UUID uuid = new UUID("1101", true);		
			String connectionString = "btspp://localhost:" + uuid+ ";name=Sample SPP Server";
			System.out.println("\nRUN BLUETHOOTH SERVER");
			append("\nRUN BLUETHOOTH SERVER");
			// open server url
			StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

			// Wait for client connection
			System.out.println("\nServer Started. Waiting for clients to connect...");
			append("\nServer Started. Waiting for clients to connect...");
			StreamConnection connection = streamConnNotifier.acceptAndOpen();

			RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
			
			System.out.println("Remote device address: "+ dev.getBluetoothAddress());
			System.out.println("Remote device name: " + dev.getFriendlyName(true));
			append("\nRemote device address: "+ dev.getBluetoothAddress());
			append("\nRemote device name: " + dev.getFriendlyName(true));
			
			InputStream inStream = connection.openInputStream();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

			RobotWindows objRobot = new RobotWindows();
			boolean state=objRobot.listen(bReader);	

			streamConnNotifier.close();
			
			startServerBluethooth();
		}
		
		public void append(String s) {
			   try {
			      Document doc = editorPane.getDocument();
			      doc.insertString(doc.getLength(), s, null);	
			      
			      JScrollBar vertical = editorScrollPane.getVerticalScrollBar();
			      vertical.setValue( vertical.getMaximum() );
			      
			   } catch(BadLocationException exc) {
			      exc.printStackTrace();
			   }
			}
}