package com.zwad3.wifijoy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IPReciever extends BroadcastReceiver {
	private NeedRecieve nr;
	private static Intent intent;
	private static Context arg0;
	public IPReciever() {
		super();
		Log.d("IPRec", "Empty Const");
	}
	public IPReciever(NeedRecieve nr){
		this.nr = nr;
		if (intent!=null) {
			nr.intentRecieved(intent);
		}
	}
	public void onReceive(Context arg0, Intent arg1) {
		Log.d("Wifi Joystick", ("Recieved "+arg1.getStringExtra("port")));
		intent = arg1;
		this.arg0 = arg0;
		if (nr!=null) {
			nr.intentRecieved(arg1);
		}
	}
	public Intent getIntent(){
		return intent;
	}

}
