package mierzwa.rafal.smartmouse2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends Activity {

int defMouseSensTouch=50;
int defMouseSensGyro=50;
boolean defIsSensorEnable=false;
boolean defIsMultiscreen=false;

boolean isSensorEnable;
boolean isMultiscreen;
int mouseSensTouch,mouseSensGyro;
Button buttonOK;
CheckBox checkBoxGyroscope;
CheckBox checkBoxMultiscreen;
SeekBar seekBarTouchMouse,seekBarGyroMouse;
Intent returnIntent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.settings);
		isSensorEnable=getIntent().getBooleanExtra("isSensorEnable", false);
		isMultiscreen=getIntent().getBooleanExtra("isMultiscreen", false);
		float fl=getIntent().getFloatExtra("mouseSensTouch", 50);
		mouseSensTouch=Math.round(fl);
		float f2=getIntent().getFloatExtra("mouseSensGyro", 50);
		mouseSensGyro=Math.round(f2);
		
		checkBoxGyroscope = (CheckBox) findViewById(R.id.gyroscope);
		checkBoxMultiscreen= (CheckBox) findViewById(R.id.multiscreen);
		seekBarTouchMouse = (SeekBar) findViewById(R.id.seekBarTouchMouse);
		seekBarGyroMouse = (SeekBar) findViewById(R.id.seekBarGyroMouse);
		buttonOK = (Button) findViewById(R.id.buttonOK);
		buttonOK.setOnClickListener(buttonClickListner);
		
		
		checkBoxMultiscreen.setChecked(isMultiscreen);		
		checkBoxGyroscope.setChecked(isSensorEnable);	
		
		seekBarTouchMouse.setMax(1);
		seekBarTouchMouse.setMax(250);
		seekBarTouchMouse.setProgress(mouseSensTouch);
		
		seekBarGyroMouse.setMax(1);
		seekBarGyroMouse.setMax(250);
		seekBarGyroMouse.setProgress(mouseSensGyro);
		 
		returnIntent = new Intent();
		
	}
	
	private OnClickListener buttonClickListner = new OnClickListener() {
		@Override
		public void onClick(View v) {
		
			Button b = (Button) v;
			int buttonId = b.getId();
			switch (buttonId) {
			case R.id.buttonOK:	
				returnIntent.putExtra("isSensorEnable",isSensorEnable);		
				returnIntent.putExtra("isMultiscreen",isMultiscreen);
				returnIntent.putExtra("mouseSensTouch",seekBarTouchMouse.getProgress());
				returnIntent.putExtra("mouseSensGyro",seekBarGyroMouse.getProgress());
				setResult(Activity.RESULT_OK,returnIntent);
				finish();
				break;						
			default:
				break;
			}

		}
	};
	
	public void setDefaultValues(View view){
		mouseSensTouch=defMouseSensTouch;
		seekBarTouchMouse.setProgress(defMouseSensTouch);
		
		mouseSensGyro=defMouseSensGyro;
		seekBarGyroMouse.setProgress(defMouseSensGyro);
		
		isSensorEnable=defIsSensorEnable;
		checkBoxGyroscope.setChecked(isSensorEnable);		
		
		isMultiscreen=defIsMultiscreen;
		checkBoxMultiscreen.setChecked(isMultiscreen);		
	}
	
	public void changeSensorState(View view){
		 if(checkBoxGyroscope.isChecked()){			
			 isSensorEnable=true;		
		 }else{	
			 isSensorEnable=false;								
		 }

	}
	
	public void setIsMultiscreen(View view){
		 if(checkBoxMultiscreen.isChecked()){			
			 isMultiscreen	=true;
		 }else{	
			 isMultiscreen	=false;							
		 }

	}
	
}
