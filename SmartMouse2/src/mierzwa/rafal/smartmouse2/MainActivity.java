package mierzwa.rafal.smartmouse2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;

@TargetApi(8)
public class MainActivity extends Activity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.activity_main);
        	   
	    View buttonBluethooth = findViewById(R.id.b_run_blue);
	    buttonBluethooth.setOnClickListener(this);
	   
	    View buttonWifi = findViewById(R.id.b_run_wifi);
	    buttonWifi.setOnClickListener(this);
	    
	    View buttonExit = findViewById(R.id.b_exit);
	    buttonExit.setOnClickListener(this);
        
    }
    public void onClick(View v) {
	    switch (v.getId()) {    
        case R.id.b_run_blue:
        	runBluethooth();
        	break;
        case R.id.b_run_wifi:
        	runWifi();
        	break;
        	
        case R.id.b_exit:
            finish();
            break;
        }
    }
    private void runBluethooth() {
    	Intent i = new Intent(this, MainView.class);
    	i.putExtra("connectionType","Bluethooth");
        startActivity(i);
    }
    private void runWifi() {
    	Intent i = new Intent(this, MainView.class);
    	i.putExtra("connectionType","Wifi");
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
