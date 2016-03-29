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

int defMouseSens=50;
boolean defIsSensorEnable=false;
boolean defIsMultiscreen=false;

boolean isSensorEnable;
boolean isMultiscreen;
int mouseSens;
Button buttonOK;
CheckBox checkBoxGyroscope;
CheckBox checkBoxMultiscreen;
SeekBar seekBar;
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
		float fl=getIntent().getFloatExtra("mouseSens", 50);
		mouseSens=Math.round(fl);
		
		checkBoxGyroscope = (CheckBox) findViewById(R.id.gyroscope);
		checkBoxMultiscreen= (CheckBox) findViewById(R.id.multiscreen);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		buttonOK = (Button) findViewById(R.id.buttonOK);
		buttonOK.setOnClickListener(buttonClickListner);
		
		
		checkBoxMultiscreen.setChecked(isMultiscreen);		
		checkBoxGyroscope.setChecked(isSensorEnable);		
		seekBar.setMax(1);
		seekBar.setMax(250);
		seekBar.setProgress(mouseSens);
		
		 
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
				returnIntent.putExtra("mouseSens",seekBar.getProgress());
				setResult(Activity.RESULT_OK,returnIntent);
				finish();
				break;						
			default:
				break;
			}

		}
	};
	
	public void setDefaultValues(View view){
		mouseSens=defMouseSens;
		seekBar.setProgress(defMouseSens);
		
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
