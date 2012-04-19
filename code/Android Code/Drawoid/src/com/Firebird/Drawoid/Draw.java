package com.Firebird.Drawoid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Draw extends Activity{
	DrawView drawView;
	
	final String tag = "Drawoid";
	
	/** Bluetooth related objects. */
	private BluetoothComm mBluetoothComm = null;
	private static final int REQUEST_ENABLE_BT = 2;
	private BluetoothAdapter mAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.drawingcanvas);
        
        drawView = new DrawView(this);
        setContentView(drawView);
        drawView.requestFocus();
        
      //About to add code
        mAdapter = BluetoothAdapter.getDefaultAdapter(); /** Get the Bluetooth hardware and create a handle for it. */
		if (mAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available. Closing Application", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.drawmenu, menu);
	    return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.drawitem:
                autoDraw();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void autoDraw(){
    	Log.d(tag,"##### STARTING AUTODRAW #######");
    	//Ashish Tajane's Code
    	List<Point> points = new ArrayList<Point>();
    	points = drawView.points;
    	points = cleanup1(points);
    	points = cleanup2(points);
    	byte[] result = encode(points);	
    	try {
			mBluetoothComm.BluetoothSend(result);   
			Toast.makeText(this, " Message Sent !! ", Toast.LENGTH_SHORT).show();
		}
			catch (Exception e){e.printStackTrace();
			Toast.makeText(this, " Message Not Sent !! ", Toast.LENGTH_SHORT).show();
		}
		Log.d(tag, "Write on button press successful");
    }
    
    //FUNCTIONS FOR AUTODRAW
    
    private static int scale = 5;
    
    //cleanup of points closer to each other
    public static List<Point> cleanup1(List<Point> points){
        int i;
        float d;//distance
        Point x, y, r;
        float t = (float) 10;//define threshold distance here
        for(i = 0; i < points.size() - 1; ){
            x = points.get(i);
            y = points.get(i + 1);
            d = x.length(y);
            if (d < t){
                r= points.remove(i + 1);
                if (r != y){
                    System.out.println("ERROR : wrong point deleted");
                }
            }
            else {
                i++;
            }
        }
        
        return points;
    }
    
    //cleanup of points in almost straight lines
    public static List<Point> cleanup2(List<Point> points){
        int i;
        int theta;//angle
        Point x, y, z, r;
        int t = 150;//define threshold angle here
        for(i = 0; i < points.size() - 2; ){
            x = points.get(i);
            y = points.get(i + 1);
            z = points.get(i + 2);
            theta = y.angle(x, z);
            if (theta > t){
                 r= points.remove(i + 1);
                if (r != y){
                    System.out.println("ERROR : wrong point deleted");
                }
            }
            else {
                i++;
            }
        }
        
        return points;
    }
    
    //function to round off the float number to given(Rpl) decimal places
    public static float Round(float Rval, int Rpl) {
        float p = (float)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float)tmp/p;
    }
    
    //final encoding to character array
    public static byte[] encode(List<Point> points){
        int n = points.size();
        Point x, y, z;
        int i, degree;
        float d;
        int d_int;
        String dir, degree_str, d_str, temp, result = "";
        //char[] result1 = new char[1000]; // n-2 turns and n-1 straight pen down movements
        
        x = points.get(0);
        y = points.get(1);
        d = x.length(y) * scale;
        d = Round(d, 2);
        d_int =  (int)d;
        
        d_str = String.valueOf(d_int);
        temp = "D"+"$"+"F"+d_str+"$";
        result = result.concat(temp);
        
        for (i = 1; i < n-1; i++){
            x = points.get(i - 1);
            y = points.get(i);
            z = points.get(i + 1);
            dir = y.direction(x, z);
            degree = y.angle(x, z);
            degree = 180 - degree;
            degree_str = String.valueOf(degree);
            d = y.length(z) * scale;
            d = Round(d, 2);
            d_int =  (int)d;
            
            d_str = String.valueOf(d_int);
            temp = dir+degree_str+"$"+"F"+d_str+"$";
            result = result.concat(temp);
        }
        
        //char[] result1 = result.toCharArray();
        byte[] result1 = result.getBytes();
        return result1;
    }
    
    //BLUETOOTH FUNCTIONS
    
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
