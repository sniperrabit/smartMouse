package mierzwa.rafal.smartmouse2;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;


public class WifiClientService  {	
	
	 private ConnectedThread mConnectedThread;
	 
	
	public WifiClientService(Context context) {
		// TODO Auto-generated constructor stub
	}

	 public void write(byte[] out) {
	        // Create temporary object
	        ConnectedThread r;
	        // Synchronize a copy of the ConnectedThread
	        synchronized (this) {
	           // if (mState != STATE_CONNECTED) return;
	            r = mConnectedThread;
	        }
	        // Perform the write unsynchronized
	      
	        r.write(out);
	    }
	/**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     */
    public synchronized void connect(String adress, int port) {
    	
    	Socket socket = null;
		try {
			socket = new Socket(adress,port);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		if (socket==null)System.out.println("ERROR NULL socket");		
//		if (mConnectedThread==null)System.out.println("ERROR NULL mConnectedThread");
		
		
		 mConnectedThread = new ConnectedThread(socket);
//	     mConnectedThread.start();
       
    }
				
//				
//	public class MyClientTask extends AsyncTask<Void, Void, Void> {
//		
//		String dstAddress;
//		int dstPort;
//		String response = "";
//		
//		MyClientTask(String addr, int port){
//			dstAddress = addr;
//			dstPort = port;
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			
//			Socket socket = null;
//			
//			try {
//				socket = new Socket(dstAddress, dstPort);
//				
//				
//				 //Send the message to the server
//	            OutputStream os = socket.getOutputStream();
//	            OutputStreamWriter osw = new OutputStreamWriter(os);
//	            BufferedWriter bw = new BufferedWriter(osw);
//	 
//	            String s = "hi from client wifi";
//	 
//	            String sendMessage = s + "\n";
//	            bw.write(sendMessage);
//	            bw.flush();
//	            System.out.println("Message sent to the server : "+sendMessage);
//				
//	          //  runMainView();
//				
//				
//				
////				ByteArrayOutputStream byteArrayOutputStream = 
////		                new ByteArrayOutputStream(1024);
////				byte[] buffer = new byte[1024];
////				
////				int bytesRead;
////				InputStream inputStream = socket.getInputStream();
////				
////				/*
////				 * notice:
////				 * inputStream.read() will block if no data return
////				 */
////	            while ((bytesRead = inputStream.read(buffer)) != -1){
////	                byteArrayOutputStream.write(buffer, 0, bytesRead);
////	                response += byteArrayOutputStream.toString("UTF-8");
////	            }
//
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				response = "UnknownHostException: " + e.toString();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				response = "IOException: " + e.toString();
//			}finally{
//				if(socket != null){
//					try {
//						socket.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			textResponse.setText(response);
//			super.onPostExecute(result);
//			
//		}
//		
//	}

	
	
	/**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
     //  private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(Socket socket) {
        //    mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
              //  tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
//                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
//                    mHandler.obtainMessage(MainView.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
                } catch (IOException e) {
//                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(MainView.MESSAGE_WRITE, -1, -1, buffer)
//                        .sendToTarget();
            } catch (IOException e) {
            }
        }

       
    }
}
