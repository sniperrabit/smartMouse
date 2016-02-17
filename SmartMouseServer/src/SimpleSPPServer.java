import java.awt.event.InputEvent;
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

public class SimpleSPPServer {

	
	
	// start server
	private void startServer() throws IOException {

		// Create a UUID for SPP
		UUID uuid = new UUID("1101", true);
		// Create the servicve url
		String connectionString = "btspp://localhost:" + uuid
				+ ";name=Sample SPP Server";
		System.out.println("\nRUN BLUETHOOTH SERVER");
		// open server url
		StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector
				.open(connectionString);

		// Wait for client connection
		System.out.println("\nServer Started. Waiting for clients to connect...");
		StreamConnection connection = streamConnNotifier.acceptAndOpen();

		RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
		System.out.println("Remote device address: "+ dev.getBluetoothAddress());
		System.out.println("Remote device name: " + dev.getFriendlyName(true));
		int i = 1;
		// read string from spp client

		InputStream inStream = connection.openInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));


//		
		
//			String everything = null ;
//			String str;
//		    int line;
//		    while( (line = bReader.read()) != 0) {
//		    	str=String.valueOf(Character.toChars(line));
//		    	if( str.equals("M")){
//		    		everything="";
//		    		System.out.println("Mouse");
//		    		 everything=everything.concat(str);
//		    	}
//		    	System.out.println(everything);
//		      
//		      // System.out.print(Character.toChars(line));
//		      // System.out.println(everything.toString());
//		    }
		   
		
		int oldA = 0,oldB=0;
		int posx = 0,posy=0;
		int tempA,tempB;
		RobotWindows objRobot = new RobotWindows();
		boolean state=objRobot.listen(bReader);
		boolean first=true;
		
//		while (( x = bReader.readLine()) != null) {
//		//	System.out.println(x);
//			y=x.split(" ");
//
//			a=Integer.valueOf(y[0]);
//			b=Integer.valueOf(y[1]);
//			
//			if(first){
//				oldA=a;
//				oldB=b;
//				first=false;
//			}
//			 tempA=a-oldA;
//			 tempB=b-oldB;
//			System.out.println("roznica "+tempA+" "+tempB);
//			posx=posx+tempA;
//			posy=posy+tempB;
//			
//			
//			
//			test.moveMouse(posx,posy);
//			System.out.println(posx+" "+posy);
//		//	System.out.println(y[0]+""+y[1]);
//		}
		
		
		// send response to spp client
//		OutputStream outStream = connection.openOutputStream();
//		PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
//		pWriter.write("Response String from SPP Server\r\n");
//		pWriter.flush();
//
//		pWriter.close();

		streamConnNotifier.close();
		
		SimpleSPPServer sampleSPPServer = new SimpleSPPServer();
		sampleSPPServer.startServer();
	}

	public static void main(String[] args) throws IOException {

		// display local device address and name
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		System.out.println("Address: " + localDevice.getBluetoothAddress());
		System.out.println("Name: " + localDevice.getFriendlyName());
		
//		WifiSocketServer wifiSocketServer = new WifiSocketServer();
//		wifiSocketServer.startServer();
		
		(new Thread(new WifiSocketServer())).start();
		
		SimpleSPPServer sampleSPPServer = new SimpleSPPServer();
		sampleSPPServer.startServer();
		
	}
}