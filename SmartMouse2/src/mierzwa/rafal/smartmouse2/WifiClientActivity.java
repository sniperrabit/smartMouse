package mierzwa.rafal.smartmouse2;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WifiClientActivity extends Activity {
	
	TextView textResponse;
	EditText editTextAddress, editTextPort; 
	Button buttonConnect, buttonClear;
	 private ConnectedThread mConnectedThread;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_connect);
		
		editTextAddress = (EditText)findViewById(R.id.address);
		editTextPort = (EditText)findViewById(R.id.port);
		buttonConnect = (Button)findViewById(R.id.connect);
		buttonClear = (Button)findViewById(R.id.clear);
		textResponse = (TextView)findViewById(R.id.response);
		
		buttonConnect.setOnClickListener(buttonConnectOnClickListener);
		editTextAddress.setText("10.8.0.222");
		editTextPort.setText("4444");
		buttonClear.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				textResponse.setText("lol");
				
			}});
	}
	
//	public void runMainView(){
//		Intent i = new Intent(this, MainView.class);
//		i.putExtra("connectionType","Wifi");
//	    startActivity(i);
//	}
	
	OnClickListener buttonConnectOnClickListener = 
			new OnClickListener(){

				@Override
				public void onClick(View arg0) {
//					MyClientTask myClientTask = new MyClientTask(
//							editTextAddress.getText().toString(),
//							Integer.parseInt(editTextPort.getText().toString()));
//					myClientTask.execute();
					  
					
					  Intent intent = new Intent();
			            intent.putExtra("adress", editTextAddress.getText().toString());
			            intent.putExtra("port", Integer.parseInt(editTextPort.getText().toString()));	
			            // Wy�lij wynik i zako�cz aktywno��
			            setResult(Activity.RESULT_OK, intent);
			            finish();
//					
//					Socket socket = null;
//					try {
//						socket = new Socket(editTextAddress.getText().toString(),Integer.parseInt(editTextPort.getText().toString()));
//					} catch (NumberFormatException e) {
//						e.printStackTrace();
//					} catch (UnknownHostException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					 mConnectedThread = new ConnectedThread(socket);
//				     mConnectedThread.start();
				}};
  
	public class MyClientTask extends AsyncTask<Void, Void, Void> {
		
		String dstAddress;
		int dstPort;
		String response = "";
		
		MyClientTask(String addr, int port){
			dstAddress = addr;
			dstPort = port;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			Socket socket = null;
			
			try {
				socket = new Socket(dstAddress, dstPort);
				
				
				 //Send the message to the server
	            OutputStream os = socket.getOutputStream();
	            OutputStreamWriter osw = new OutputStreamWriter(os);
	            BufferedWriter bw = new BufferedWriter(osw);
	 
	            String s = "hi from client wifi";
	 
	            String sendMessage = s + "\n";
	            bw.write(sendMessage);
	            bw.flush();
	            System.out.println("Message sent to the server : "+sendMessage);
				
	          //  runMainView();
				
				
				
//				ByteArrayOutputStream byteArrayOutputStream = 
//		                new ByteArrayOutputStream(1024);
//				byte[] buffer = new byte[1024];
//				
//				int bytesRead;
//				InputStream inputStream = socket.getInputStream();
//				
//				/*
//				 * notice:
//				 * inputStream.read() will block if no data return
//				 */
//	            while ((bytesRead = inputStream.read(buffer)) != -1){
//	                byteArrayOutputStream.write(buffer, 0, bytesRead);
//	                response += byteArrayOutputStream.toString("UTF-8");
//	            }

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			textResponse.setText(response);
			super.onPostExecute(result);
			
		}
		
	}

	
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
                tmpIn = socket.getInputStream();
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
