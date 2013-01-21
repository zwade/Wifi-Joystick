package com.zwad3.wifijoy;

import android.util.Log;
import android.widget.TextView;

public class LogShell {
	private TextView t;
	public LogShell(TextView t) {
		this.t = t;
	}
	public void print(String s) {
		//t.setText(s+"\n"+t.getText());
		Log.d("Wifi Joystick", s);
	}
	
}
