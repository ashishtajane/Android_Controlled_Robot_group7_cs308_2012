package com.Firebird.Drawoid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrawingCanvas extends Activity{ 
	
	//Button StartButton;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.drawingcanvas);
        //StartButton = (Button) findViewById(R.id.drawButton);
        //StartButton.setOnClickListener((android.view.View.OnClickListener) this);
	}
	/*public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId() == R.id.startButton){
			
		}
	}*/

}
