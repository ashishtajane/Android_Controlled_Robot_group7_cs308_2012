package com.Firebird.Drawoid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ManualDraw extends Activity implements OnClickListener{
	
	final String tag = "Drawoid";
	
	/** UI related objects */
	Button ForwardButton;
	Button BackwardButton;
	Button LeftButton;
	Button RightButton;
	Button PenupButton;
	Button PendownButton;
	
	/** Bluetooth related objects. */
	private BluetoothComm mBluetoothComm = null;
	private static final int REQUEST_ENABLE_BT = 2;
	private BluetoothAdapter mAdapter = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manualdraw);
        
      //About to add code
        mAdapter = BluetoothAdapter.getDefaultAdapter(); /** Get the Bluetooth hardware and create a handle for it. */
		if (mAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available. Closing Application", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		Log.d(tag,"Got bluetooth adapter..");

        // Set full screen view
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        
        ForwardButton = (Button) findViewById(R.id.forward);
        ForwardButton.setOnClickListener((android.view.View.OnClickListener) this);

        BackwardButton = (Button) findViewById(R.id.backward);
        BackwardButton.setOnClickListener((android.view.View.OnClickListener) this);

        LeftButton = (Button) findViewById(R.id.left);
        LeftButton.setOnClickListener((android.view.View.OnClickListener) this);

        RightButton = (Button) findViewById(R.id.right);
        RightButton.setOnClickListener((android.view.View.OnClickListener) this);

        PenupButton = (Button) findViewById(R.id.penup);
        PenupButton.setOnClickListener((android.view.View.OnClickListener) this);

        PendownButton = (Button) findViewById(R.id.pendown);
        PendownButton.setOnClickListener((android.view.View.OnClickListener) this);


        
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.forward){
			byte[] write_buffer = new byte[4];
			write_buffer[0] = 'F';
			write_buffer[1] = '5';
			write_buffer[2] = '0';
			write_buffer[3] = '$';
			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful");
		}
		
		if(v.getId() == R.id.backward){
			byte[] write_buffer = new byte[4];
			write_buffer[0] = 'B';
			write_buffer[1] = '5';
			write_buffer[2] = '0';
			write_buffer[3] = '$';
			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful");
		}
		
		if(v.getId() == R.id.left){
			byte[] write_buffer = new byte[4];
			write_buffer[0] = 'L';
			write_buffer[1] = '2';
			write_buffer[2] = '0';
			write_buffer[3] = '$';
			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful");
		}
		
		if(v.getId() == R.id.right){
			byte[] write_buffer = new byte[4];
			write_buffer[0] = 'R';
			write_buffer[1] = '2';
			write_buffer[2] = '0';
			write_buffer[3] = '$';
			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful");
		}

		if(v.getId() == R.id.penup){
			byte[] write_buffer = new byte[2];
			write_buffer[0] = 'U';
			write_buffer[1] = '$';
			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful");
		}

		if(v.getId() == R.id.pendown){
			byte[] write_buffer = new byte[2];
			write_buffer[0] = 'D';
			write_buffer[1] = '$';
			try {
				mBluetoothComm.BluetoothSend(write_buffer);   
				Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
			}
				catch (Exception e){e.printStackTrace();
				Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Write on button press successful");

		}
	}
	
	/** Called when the activity starts. Gives a request to turn ON the Bluetooth id OFF*/
	@Override
	public void onStart() {
		super.onStart();
		Log.d(tag, "++ ON START ++");
		/** If bluetooth is not enabled, ask for user permission to turn on bluetooth. */
		if (!mAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			//if(mAccelerometerReader == null) startup();
			startup();
		}
	}
	
	/** Called when the activity is aborted. 
	 * Stops the BT channel */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(tag,"On destroy..");
		if(mBluetoothComm != null){
			mBluetoothComm.free_channel();}
		Log.e(tag, "--- ON DESTROY ---");
	}
	
	/** Called when the activity resumes after prompting user to turn ON the bluetooth. 
	 * If turned ON, goes ahead with application, else closes the connection and stops application.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d(tag, "onActivityResult " + resultCode);
		if (requestCode == REQUEST_ENABLE_BT) 
		{
			/** When the request to enable Bluetooth returns. */
			if (resultCode == Activity.RESULT_OK) {
				Log.d(tag,"BT Enabled");
				// Bluetooth is now enabled
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(tag, "BT not enabled");
				Toast.makeText(this, "Bluetooth was not enabled. Closing application..", Toast.LENGTH_LONG).show();
				finish();  /** Terminate the activity and close application. */
				return;
			}
		}
	}
	
	private void startup(){
		Log.d(tag,"Establishing buetooth connection..");
		mBluetoothComm = new BluetoothComm(this);
		Toast.makeText(this, "Connecting...", Toast.LENGTH_LONG).show();
		try {
			Log.d(tag, "Initialisation Started...");
			
			/** Bluetooth initialise function returns true if connection is succesful, else false. */
			if(mBluetoothComm.Initialise() == false) 
			{
				Toast.makeText(this, " No connection established ", Toast.LENGTH_SHORT).show();
				return;
			}
			else 
			{
				Toast.makeText(this, " Connection established ", Toast.LENGTH_SHORT).show();
			}
			Log.d(tag, "Initialisation Successful");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, "Initialisation Failed");
		}
	}
}
