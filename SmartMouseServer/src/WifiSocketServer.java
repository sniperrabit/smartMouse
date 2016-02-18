import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;


public class WifiSocketServer implements Runnable {


	String message = "";
	ServerSocket serverSocket;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//
//
//        ServerSocket serverSocket = null;
//        try {
//            serverSocket = new ServerSocket(4444);
//            System.out.println(getIpAddress());
//            System.out.println("I'm waiting here: "
//					+ serverSocket.getLocalPort());
//        } catch (IOException e) {
//            System.err.println("Could not listen on port: 4444.");
//            System.exit(1);
//        }
//
//        Socket clientSocket = null;
//        try {
//            clientSocket = serverSocket.accept();
//            System.err.println("accept");
//        } catch (IOException e) {
//            System.err.println("Accept failed.");
//            System.exit(1);
//        }
//
//   
//        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        
//        String inputLine;
//
//        while ((inputLine = in.readLine()) != null) {
//        	System.out.println(inputLine);
//        }
//
//       in.close();
//        clientSocket.close();
//        serverSocket.close();
        
        
    }
    
	 public void run() {
			System.out.println("\nRUN WIFI SERVER IN THREAD");
	        
	        ServerSocket serverSocket = null;
	        try {
	            serverSocket = new ServerSocket(4444);
	            System.out.println(getIpAddress());
	            System.out.println("I'm waiting here: "
						+ serverSocket.getLocalPort());
	        } catch (IOException e) {
	            System.err.println("Could not listen on port: 4444.");
	            System.exit(1);
	        }

	        Socket clientSocket = null;
	        try {
	            clientSocket = serverSocket.accept();
	            System.err.println("Connection Wifi ACCEPTED");
	        } catch (IOException e) {
	            System.err.println("Accept failed.");
	            System.exit(1);
	        }
	        try {
		   
		        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));    
//		        String inputLine;
		        RobotWindows objRobot = new RobotWindows();
				boolean state=objRobot.listen(in);
//					while ((inputLine = in.readLine()) != null) {
//						System.out.println(inputLine);
//					}
				System.out.println("END CONNECTION WIFI");
		        in.close();
		        clientSocket.close();
		        serverSocket.close();
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        (new Thread(new WifiSocketServer())).start();
	    }
    
    public static String getIpAddress() {
    	
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += "SiteLocalAddress: " 
								+ inetAddress.getHostAddress() + "\n";
					}
					
				}

			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		return ip;
	}
}



