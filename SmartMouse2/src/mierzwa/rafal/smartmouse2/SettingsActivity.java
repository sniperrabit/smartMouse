package mierzwa.rafal.smartmouse2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends Activity {



boolean isSensorEnable;
int mouseSens;
Button buttonOK;
CheckBox checkBox;
SeekBar seekBar;
Intent returnIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		isSensorEnable=getIntent().getBooleanExtra("isSensorEnable", false);
		float fl=getIntent().getFloatExtra("mouseSens", 50);
		mouseSens=Math.round(fl);
		checkBox = (CheckBox) findViewById(R.id.gyroscope);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		buttonOK = (Button) findViewById(R.id.buttonOK);
		buttonOK.setOnClickListener(buttonClickListner);
		
		
		checkBox.setChecked(isSensorEnable);		
		seekBar.setMax(1);
		seekBar.setMax(100);
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
				returnIntent.putExtra("mouseSens",seekBar.getProgress());
				setResult(Activity.RESULT_OK,returnIntent);
				finish();
				break;						
			default:
				break;
			}

		}
	};
	
	public void changeSensorState(View view){
		 if(checkBox.isChecked()){			
			 returnIntent.putExtra("SensorState",true);		
		 }else{	
			 returnIntent.putExtra("SensorState",false);								
		 }

	}
}
