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
	
	OnClickListener buttonConnectOnClickListener = 
			new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					
					  Intent intent = new Intent();
			            intent.putExtra("adress", editTextAddress.getText().toString());
			            intent.putExtra("port", Integer.parseInt(editTextPort.getText().toString()));	
			            
			            setResult(Activity.RESULT_OK, intent);
			            finish();

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
}

