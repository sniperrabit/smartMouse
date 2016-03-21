package mierzwa.rafal.smartmouse2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends Activity{



boolean isSensorEnable;
CheckBox checkBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		isSensorEnable=getIntent().getBooleanExtra("isSensorEnable", false);
		
		checkBox = (CheckBox) findViewById(R.id.gyroscope);
		checkBox.setChecked(isSensorEnable);
	}
	
	public void changeSensorState(View view){
		
		 if(checkBox.isChecked()){
			 Intent returnIntent = new Intent();
			 returnIntent.putExtra("SensorState",true);
			 setResult(Activity.RESULT_OK,returnIntent);
			 finish();
			 
		 }else{
			 Intent returnIntent = new Intent();
			 returnIntent.putExtra("SensorState",false);
			 setResult(Activity.RESULT_OK,returnIntent);
			 finish();
			
		 }

	}
}
