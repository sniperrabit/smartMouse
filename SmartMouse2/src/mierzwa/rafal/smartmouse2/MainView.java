package mierzwa.rafal.smartmouse2;


import java.util.ArrayList;
import java.util.List;







import android.app.Activity;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v4.view.MotionEventCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainView extends Activity implements SensorEventListener {

	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CONNECT_WIFI = 3;
	private static final int VOICE_RECOGNITION_REQUEST_CODE=4;
	// Layout Views
	private TextView mTitle;
	private EditText mOutEditText;
	private Button mSendButton;
	private Button butClickLeft;
	private Button butClickRight;
	private Button butUpArrow;
	private Button butDownArrow;
	private Button butLeftArrow;
	private Button butRightArrow;
	private Button butDelete;
	private Button butEnter;
	private Button butEsc;
	// Array adapter for the conversation thread
//	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	private WifiClientService wifiChatService = null;

	private SensorManager mSensorManager;
	private Sensor mAccelerometr;
	public String connectionType;
	
	private Button mbtSpeak;
	private Button mbtSpeakLoop;
	private View vbtSpeakLoop;
	private boolean isWifiConected;
	
	boolean isThreadVoice=true;
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Intent myIntent = getIntent(); // gets the previously created intent
		connectionType = myIntent.getStringExtra("connectionType");
		
		if(connectionType.equals("Bluethooth")){
			Intent serverIntent = new Intent(this, BluetoothDeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			
			// Get local Bluetooth adapter
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			// If the adapter is null, then Bluetooth is not supported
			if (mBluetoothAdapter == null) {
				Toast.makeText(this, "Bluetooth is notf available",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}
		}else {
			
			Intent serverIntent = new Intent(this, WifiClientActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_WIFI);
			
		}
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometr = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.custom_title);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		// mTitle.setText("app_name");
		mTitle = (TextView) findViewById(R.id.title_right_text);
		butClickLeft = (Button) findViewById(R.id.button1);
		butClickRight = (Button) findViewById(R.id.button2);
		
		butUpArrow= (Button) findViewById(R.id.butUpArrow);
		butDownArrow= (Button) findViewById(R.id.butDownArrow);
		butLeftArrow= (Button) findViewById(R.id.butLeftArrow);
		butRightArrow= (Button) findViewById(R.id.butRightArrow);
		butDelete= (Button) findViewById(R.id.butDelete);
		butEnter= (Button) findViewById(R.id.butEnter);
		butEsc= (Button) findViewById(R.id.butEsc);
		mbtSpeak = (Button) findViewById(R.id.btSpeak);
		mbtSpeakLoop = (Button) findViewById(R.id.btSpeakLoop);
		vbtSpeakLoop=findViewById(R.id.btSpeakLoop);
		
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				
				// Get the device MAC address
				String address = data.getExtras().getString(
						BluetoothDeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				mChatService.connect(device);
				System.out.println("mChatService.connect(device);");
				
				
				Toast toast= Toast.makeText(getApplicationContext(), 
						"Please wait...", Toast.LENGTH_LONG);  
						toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
						toast.show();
				Toast toast2= Toast.makeText(getApplicationContext(), 
						"Please wait...", Toast.LENGTH_LONG);  
						toast2.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
						toast2.show();
			}
			break;
		case REQUEST_CONNECT_WIFI:
			// When WifiDeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				
				setupWifi();
				// Get the device MAC address
				String adress = data.getExtras().getString("adress");
				int port = data.getExtras().getInt("port");			
				// Attempt to connect to the device
				System.out.println("TRY CONNECT");
				 isWifiConected=wifiChatService.connect(adress,port);
				System.out.println("TRY2 CONNECT: "+ isWifiConected);
				if(!isWifiConected){
					Toast.makeText(this, "Cannot connect to server",
							Toast.LENGTH_SHORT).show();
					finish();
				}
//				System.out.println("wifiChatService"+wifiChatService+" " +this);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				System.out.println("SETUP CHAT FOR BLUETHOOTH");
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				System.out.println("BLUETHOOTH NOT ENABLED");
				
				Toast.makeText(this, "bt_not_enabled_leaving",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			
		case VOICE_RECOGNITION_REQUEST_CODE:			
	//		System.out.println("resultCode "+resultCode);
				
				if(isThreadVoice && resultCode != RESULT_OK && resultCode != 0) {
					
					mbtSpeak.performClick();
					break;
				}

				//If Voice recognition is successful then it returns RESULT_OK
				if( resultCode != 0) {
						
					ArrayList<String> textMatchList = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

					if (!textMatchList.isEmpty()) {
						// If first Match contains the 'search' word
						// Then start web search.
//						if (textMatchList.get(0).contains("search")) {
//
//							String searchQuery = textMatchList.get(0).replace("search",
//							" ");
//							Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
//							search.putExtra(SearchManager.QUERY, searchQuery);
//							startActivity(search);
//						} else
						if (isThreadVoice){											
							sendMessage(textMatchList.get(0)+" \n");
							speak(vbtSpeakLoop);
							break;
						}else {					
							sendMessage(textMatchList.get(0)+" \n");	
							break;
						}

					}
				//Result code for various error.	
				}else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
					showToastMessage("Audio Error");
				}else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
					showToastMessage("Client Error");
				}else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
					showToastMessage("Network Error");
				}else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
					showToastMessage("No Match");
				}else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
					showToastMessage("Server Error");
				}
			break;
		}
	}


	
	public void checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			mbtSpeak.setEnabled(false);
			Toast.makeText(this, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void speak(View view) {
		System.out.println("view.getId() "+view.getId());
		if (R.id.btSpeak==view.getId()){
			 isThreadVoice=false;
		}else{
			 isThreadVoice=true;
		}
		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		// Display an hint to the user about what he should say.
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

	//	 intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		
		// Given an hint to the recognizer about what the user is going to say
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
			
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	
	void showToastMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	
	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	int x, y,x2, y2;
	int oldX = 0, oldY = 0;
	int posX = 0, posY = 0;
	int tmpX, tmpY;
	int difX, difY;
	
	int numberOfTaps = 0;
    long lastTapTimeMs = 0;
    long touchDownMs = 0;
	
    int xOldScroll;
	int yOldScroll;
    
	
	Handler handler = new Handler();
	boolean tripleTap=false;
	

	int option = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = MotionEventCompat.getActionMasked(event);
		
		
		int index = MotionEventCompat.getActionIndex(event);	 
		x = (int)MotionEventCompat.getX(event, index);
		y = (int)MotionEventCompat.getY(event, index);	

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				  touchDownMs = System.currentTimeMillis();
				  
				  if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
	                    //it was not a tap					
	                    numberOfTaps = 0;
	                    lastTapTimeMs = 0;
	                    break;
	                }

	                if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
	                    numberOfTaps += 1;
	                } else {
	                    numberOfTaps = 1;
	                }

	                lastTapTimeMs = System.currentTimeMillis();
	                if (numberOfTaps == 3) {
	                	System.out.println("Triple tap ");		
	                	tripleTap=true;
	                	yOldScroll=y;
	                	xOldScroll=x;
	                	numberOfTaps = 0;
		                lastTapTimeMs = 0;
	                } else if (numberOfTaps == 2) {
	                    handler.postDelayed(new Runnable() {
	                        @Override
	                        public void run() {
	                        	if (tripleTap==false){
	                        	sendMessage("PRESS\n");
	    	                    System.out.println("PRESS ");
	                        	}
	                        }
	                    }, ViewConfiguration.getDoubleTapTimeout());
	                    
	                }
				   			  
				difX = oldX - x;
				difY = oldY - y;				
				break;
			case MotionEvent.ACTION_MOVE:
				if(tripleTap){
					
					if(y<yOldScroll)sendMessage("UP\n");
					if(y>yOldScroll)sendMessage("DOWN\n");
//					if(x<xOldScroll)sendMessage("LEFT\n");
//					if(x>xOldScroll)sendMessage("RIGHT\n");
					
					yOldScroll=y;
					xOldScroll=x;
					break;
				}else{
					x += difX;
					y += difY;
					tmpX = x;
					tmpY = y;
					
					sendMessage("Mouse" + x + " " + y + "\n");
				}
				break;
			case MotionEvent.ACTION_UP:
				if(tripleTap)tripleTap=false;
				
				sendMessage("RELEASE\n");
					oldX = tmpX;
					oldY = tmpY;
				break;
			}	
		return false;
	}

	
	private boolean isClick(Long biggerTime, Long smallerTime) {
		Long diff = ((biggerTime - smallerTime));
	//	System.out.println("diff " + diff);

		if (diff > 230)
			return false;
		
		return true;
	}

	
	
	public void sendMessage(String message) {
	
		// Check that we're actually connected before trying anything
		if(connectionType.equals("Bluethooth")){
			if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			//	Toast.makeText(this, "not_connected", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			if(connectionType.equals("Bluethooth")){
				mChatService.write(send);
				
			}else{
				wifiChatService.write(send);
			}
	
			
			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			//mOutEditText.setText(mOutStringBuffer);
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem element) {
		switch (element.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, BluetoothDeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;

			// Wstawiamy tutaj pozosta�e elementy (je�li jakie� b�d�) ...

		}
		return false;
	}

	@Override
	protected synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");
		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
		

		mSensorManager.registerListener(this, mAccelerometr,
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	private void setupChat() {
		// Initialize the array adapter for the conversation thread
	//	mConversationArrayAdapter = new ArrayAdapter<String>(this,
	//			R.layout.message);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);

		mOutEditText.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				/*
				 * This method is called to notify you that, within s, the count
				 * characters beginning at start are about to be replaced by new
				 * text with length after. It is an error to attempt to make
				 * changes to s from this callback.
				 */
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// System.out.println("onTextChanged");
				// System.out.println(s.toString().substring(s.length() - 1));
				// sendMessage(s.toString().substring(s.length() - 1) + "\n");
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// System.out.println("afterTextChanged");
			}
		});

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(buttonClickListner);
		butClickLeft.setOnClickListener(buttonClickListner);
		butClickRight.setOnClickListener(buttonClickListner);

		butUpArrow.setOnClickListener(buttonClickListner);
		butDownArrow.setOnClickListener(buttonClickListner);
		butLeftArrow.setOnClickListener(buttonClickListner);
		butRightArrow.setOnClickListener(buttonClickListner);
		butDelete.setOnClickListener(buttonClickListner);
		butEnter.setOnClickListener(buttonClickListner);
		butEsc.setOnClickListener(buttonClickListner);
		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}
	
	private void setupWifi() {
		// Initialize the array adapter for the conversation thread
//		mConversationArrayAdapter = new ArrayAdapter<String>(this,
//				R.layout.message);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);

		mOutEditText.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				/*
				 * This method is called to notify you that, within s, the count
				 * characters beginning at start are about to be replaced by new
				 * text with length after. It is an error to attempt to make
				 * changes to s from this callback.
				 */
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// System.out.println("onTextChanged");
				// System.out.println(s.toString().substring(s.length() - 1));
				// sendMessage(s.toString().substring(s.length() - 1) + "\n");
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// System.out.println("afterTextChanged");
			}
		});

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(buttonClickListner);
		butClickLeft.setOnClickListener(buttonClickListner);
		butClickRight.setOnClickListener(buttonClickListner);
		
		butUpArrow.setOnClickListener(buttonClickListner);
		butDownArrow.setOnClickListener(buttonClickListner);
		butLeftArrow.setOnClickListener(buttonClickListner);
		butRightArrow.setOnClickListener(buttonClickListner);
		butDelete.setOnClickListener(buttonClickListner);
		butEnter.setOnClickListener(buttonClickListner);
		butEsc.setOnClickListener(buttonClickListner);
		// Initialize the WIFIChatService to perform wifi connections

		wifiChatService = new WifiClientService(this);
//		System.out.println("wifiChatService"+wifiChatService+" " +this);
		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}
	
	
	
	private OnClickListener buttonClickListner = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Button b = (Button) v;
			int buttonId = b.getId();
			switch (buttonId) {
			case R.id.button1:
				sendMessage("CLICK1\n");
				break;
			case R.id.button2:
				sendMessage("CLICK2\n");
				break;
			case R.id.button_send:
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message + "\n");
				System.out.println("Button Sending: " + message);
				view.setText("");
				break;
			case R.id.butDelete:
				sendMessage("DEL\n");
				break;	
			case R.id.butUpArrow:
				sendMessage("UP\n");
				break;
			case R.id.butDownArrow:
				sendMessage("DOWN\n");
				break;
			case R.id.butLeftArrow:
				sendMessage("LEFT\n");
				break;
			case R.id.butRightArrow:
				sendMessage("RIGHT\n");
				break;
			case R.id.butEnter:
				sendMessage("ENTER\n");
				break;
			case R.id.butEsc:
				sendMessage("ESC\n");
				break;
			default:
				break;
			}

		}
	};

	
	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			System.out.println("COKOLWIEK9");
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};
	
	
	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					// mTitle.setText("title_connected_to");
					// mTitle.append(mConnectedDeviceName);
					// mConversationArrayAdapter.clear();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					// mTitle.setText("title_connecting");
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					// mTitle.setText("title_not_connected");
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				new String(writeBuf);
				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;

				new String(readBuf, 0, msg.arg1);
				// mConversationArrayAdapter.add(mConnectedDeviceName+":  " +
				// readMessage);`
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				// mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				// Toast.makeText(getApplicationContext(), "Connected to "
				// + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	
	

	@Override
	protected synchronized void onPause() {
		super.onPause();
		System.out.println("ON PAUSE");
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("ON STOP");
	
	}

	@Override
	public void onDestroy() {
		super.onDestroy();	
		System.out.println("DESTROY MAIN VIEW ACTIVITY");
		if(connectionType.equals("Bluethooth")){
			// Stop the Bluetooth chat services		
			System.out.println("TRY STOP ALL");
			if (mChatService != null){
				sendMessage("EXIT\n");
				mChatService.stop();
				}			
		}else{
			System.out.println("TRY STOP WIFI");
			if(isWifiConected){
				sendMessage("EXIT\n");
			}
		}		
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//	    if (keyCode == KeyEvent.KEYCODE_BACK
//	            && event.getRepeatCount() == 0) {
//	        event.startTracking();
//	        return true;
//	    }
//	    return super.onKeyDown(keyCode, event);
//	}
//
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//	    if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
//	            && !event.isCanceled()) {
//	    	System.out.println("TRY STOP ALL WIFI");
//			if (wifiChatService != null)
//				sendMessage("EXIT\n");
//	        return true;
//	    }
//	    return super.onKeyUp(keyCode, event);
//	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		if(connectionType.equals("Bluethooth")){
			// If BT is not on, request that it be enabled.
		
			// setupChat() will then be called during onActivityResult
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
				// Otherwise, setup the chat session
			} else {
				if (mChatService == null)
					setupChat();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}


	public float xPosition_ost = 0.0f, xPosition = 0.0f, xAcceleration,xVelocity = 0.0f;
	public float yPosition_ost = 0.0f, yPosition = 0.0f, yAcceleration,yVelocity = 0.0f;
	public long frameTime, lastTime;
	int licznikBezruchuX = 0;
	int licznikBezruchuY = 0;
	public float lastX = 0, lastY = 0;
	public int licznik = 0, rozmiar = 10;;
	public float przesuniecieX, przesuniecieY, pierwszyPomiarX,pierwszyPomiarY;
	boolean mFirstAfterBreak = true;

	public boolean isMove(float Acceleration) {
		if (Math.abs(Acceleration) > (float) 0.5)
			return true;

		return false;
	}

	float sumax = 0;
	float sumay = 0;

	@Override
	public void onSensorChanged(SensorEvent e) {

	}

}