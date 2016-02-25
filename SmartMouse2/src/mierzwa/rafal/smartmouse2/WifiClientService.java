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
    public synchronized  boolean  connect(String adress, int port) {
    	
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
		
		if (socket==null||mConnectedThread==null){
			System.out.println("ERROR NULL mConnectedThread");
			return false;
		}
		
		 mConnectedThread = new ConnectedThread(socket);
		 return true;
    }
				

	
	
	/**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final OutputStream mmOutStream;

        public ConnectedThread(Socket socket) {
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
//                Log.e(TAG, "temp sockets not created", e);
            }
            mmOutStream = tmpOut;
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
            }
        }

       
    }
}
