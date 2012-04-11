package com.Firebird.Drawoid;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/** Class to implement all routines related to bluetooth communication. 
 * 	Task: Initialise bluetooth and establish connection.
 * 		  Send a byte array over the BT channel.
 * 		  Disconnect from the BT device and free the BT channel. 
 */
public class BluetoothComm{
	
	final String tag = "Drawoid";
    
	/** BT related objects. */
	private BluetoothSocket mBluetoothSocket = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private BluetoothDevice mBluetoothDevice = null;
    
    /** UI related objects. */ 
    private final Activity mactivity;
    
    /** Constructor for the class. Copies the 'activity' object for its use.*/
    public BluetoothComm(Activity activity)
    {
    	mactivity = activity;
    }
    
	/** Class for all Bluetooth related functions.
	 *  Task: (1)Acquire a BT socket and connect over that socket.
	 *  	  (2)Establish input and output streams over the socket for data transfer
	 *  Arguments: Null
	 *  Return: True is initialisation was successful, else False. 
	 * @throws Exception
	 */
    public boolean Initialise() throws Exception
	{
		/** Get a handle to the BT hardware. */
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		String add_string;
		/** Get the Address of BT device to be connected with, from the text box on UI. */
		add_string = "00:19:A4:02:C6:B7";
		try {
			/** Link the target BT address to be connected. */
			mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(add_string);
		}catch (IllegalArgumentException e)
		{ 
			/** Exception is thrown if BT address is not valid. Then return false*/
			return false;
		}
		
		//mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("00:25:56:DF:76:85");//jatin pc
        //mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("00:1F:DF:D6:71:A1");//jatin phone
        //mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("80:50:1B:60:E6:D0");//k.l.
        //mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("00:24:2C:C2:C8:66");//k.l.pc
        //mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("00:12:6F:03:72:48");//serial adapter
        
        Method m;
		m= mBluetoothDevice.getClass().getMethod("createRfcommSocket",new Class[] { int.class });
		mBluetoothSocket = (BluetoothSocket)m.invoke(mBluetoothDevice, Integer.valueOf(1));
        Log.d(tag, "Connecting...");
        
        try {
            /** This is a blocking call and will only return on a successful connection or an exception. */
            mBluetoothSocket.connect();
        } catch (IOException e) {
        	/** If target BT device not found or connection refused then return false. */
            try {
                mBluetoothSocket.close();
            } catch (IOException e2) {
                Log.e(tag, "unable to close() socket during connection failure", e2);
            }
            Log.e(tag,"returning false");
            return false;
        } 

        Log.d(tag, "Connected");
        /** Get input and output stream handles for data transfer. */
		mInputStream = mBluetoothSocket.getInputStream();
		mOutputStream = mBluetoothSocket.getOutputStream();
		return true;
	}
	
	/** Function to send data over BT.
	 * Task: (1)To send the byte array over Bluetooth Channel.
	 * Arguments: An array of bytes to be sent.
	 * Return: Null
	 */
    public void BluetoothSend(byte[] write_buffer)
	{
		try {
         	mOutputStream.write(write_buffer);
         }catch (IOException e){Log.e(tag, "Writing on command error");}
         Log.d(tag, "Writing on command successful");
	}
	
    /** Function to close BT connection.
     * Task: (1)Close input and output streams
     * 		 (2)Close Bluetooth socket.
     * Arguments: Null
     * Return: Null
     */
	public void free_channel()
	{
		try {
            if (mInputStream != null) {
            	mInputStream.close();
            }
            if (mOutputStream != null) {
            	mOutputStream.close();
            }
            if (mBluetoothSocket != null) {
            	mBluetoothSocket.close();
            }
            Log.d(tag, "BT Channel free");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	 
}