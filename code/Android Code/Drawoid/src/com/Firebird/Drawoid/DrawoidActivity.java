package com.Firebird.Drawoid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DrawoidActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	final String tag = "Drawoid";

	/** UI related objects */
	Button DrawButton;
	Button ManualDrawButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(tag,"Drawoid started..On Create");
        
        DrawButton = (Button) findViewById(R.id.drawButton);
        DrawButton.setOnClickListener((android.view.View.OnClickListener) this);
        
        ManualDrawButton = (Button) findViewById(R.id.manualDrawButton);
        ManualDrawButton.setOnClickListener((android.view.View.OnClickListener) this);
        
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId() == R.id.drawButton){
			
			startActivity(new Intent(this, Draw.class));
			//********************************************
			
			// sending test string...for testing purposes
			/*byte[] write_buffer = new byte[1];
			write_buffer[0] = '8';
			
			write_buffer[1] = '8';
			write_buffer[2] = '8';
			write_buffer[3] = '8';
			write_buffer[4] = '8';
			write_buffer[5] = '8';
			write_buffer[6] = '8';
			write_buffer[7] = '8';
			write_buffer[8] = '8';
			write_buffer[9] = '8';

			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful"); */
			//********************************************
		}
		
		if(v.getId() == R.id.manualDrawButton){
			startActivity(new Intent(this, ManualDraw.class));
		}
		
	}
}