package com.zwad3.wifijoy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

/**
 * 
 * @author zacharywade
 * Main activity of my project
 * REAAAALY needs some work
 * TODO: change name from "settings" to anything else
 */
public class settings extends Activity {
	
	/*
	 * Private fields
	 */
	private String ip;
	private FileServer fs;
	private InputMethodManager ims;
    private Messenger mService;
	
	/*
	 * Public constants
	 */
	public static final String packagename = "com.zwad3.wifijoy.WifiJoy";

	/**
	 * Helper method to print stuff
	 * @param s string to print
	 */
    public void print(String s) {
    	Log.d("Wifi Joystick",s);
    }
    
    
    //Overriden methods from Activity:
    //<------------------------------------------------------------------------>
    
    /**
     * called when created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//print("hello");
    	//print(LoadPreferences("ip"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    /**
     * called when started
     */
    @Override 
    public void onStart() {
    	super.onStart();
    	print("Started");
    }
    /**
     * called when resumed
     */
    @Override 
    public void onResume() {
    	super.onResume();
    	update();
    	print("Resumed");
    }
    /**
     * not really sure what the does or when it gets called
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        displayText();
        //fillText();
        //KeyEvent k = new KeyEvent(KeyEvent.ACTION_UP,102);
        WifiJoy wj = new WifiJoy();
        return true;
    }
    
    
    //Events
    //<------------------------------------------------------------------------>
    
    /**
     * This is called when the main button is activated. It will chose whether 
     * to do a setup or run the servers
     *  
     * @param v View
     */
    public void buttonConnect(View v) {
    	print("Button Connected");
    	EditText port = (EditText)findViewById(R.id.port);
    	int a;
    	try {
    		a = Integer.parseInt(port.getText().toString());
    	} catch (Exception e) {
    		a = 8887;
    		print(e.toString());
    		failIp(-1);
    	}
    	SavePreferences("port",""+a);
    	ims = ((InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE));
    	if (!IMEisEnabled()) {
    		Toast.makeText(this, "Please Enable Wifi Joystick", Toast.LENGTH_LONG).show();
    		startActivityForResult(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS),1);
    	}
    	if (!IMEisEnabled()) {
    		print("hello");
    	}
    	if (!IMEisSelected()) {
    		Toast.makeText(this, "Please Select Wifi Joystick", Toast.LENGTH_LONG).show();
    		ims.showInputMethodPicker();
    	}
    	
    	
    	//openSocket(ip);hi
    	Intent i = new Intent("com.zwad3.wifijoy.GET_IP");
    	i.putExtra("port",""+a);
    	//i.setAction("com.zwad3.wifi")
    	sendBroadcast(i);
    }
    public void httpChange(View v) {
    	print("Changing HTTP state");
    	CheckBox cb = (CheckBox)findViewById(R.id.http);
    	if (cb.isChecked()) {
    		if (fs==null) {
    			try {
    				fs = new FileServer(8081, new File("file:///android_asset/"),this);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	else if(true){
    		fs.stop();
    		fs = null;
    	}
    }

    /**
     * Called (supposedly) when the intent is finished. Haven't gotten it to work, however 
     * @param request
     * @param result
     * @param data
     */
    public void onResult(int request, int result, Intent data) {
    	print (request+":"+result+":"+data);
    }

    
    //Helper Methods:
    //<------------------------------------------------------------------------>
    
    /**
     * Dunno...
     * @param i
     */
    private void failIp(int i) {
    	print("Bad ip address. Please fill in all spaces");
    }
    
    /**
     * Displays the ip address to the notification view thingy
     */
    public void displayText() {
    	TextView editText = (TextView) findViewById(R.id.ipaddr);
    	WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();

    	int ipAddress = wifiInfo.getIpAddress();

    	String ip = null;

    	ip = String.format("%d.%d.%d.%d",
    	(ipAddress & 0xff),
    	(ipAddress >> 8 & 0xff),
    	(ipAddress >> 16 & 0xff),
    	(ipAddress >> 24 & 0xff));
    	//print(ip);
    	if (!(ip.equals("0.0.0.0"))) {
    		editText.setText(ip);
    	} else {
    		editText.setText("Not Connected to the Interwebs");
    	}
    }
    
    /**
     * Just updates all info on the main activity (Dynamic stuff)
     */
    public void update() {
    	//Button b = (Button)findViewById(R.id.)
    	displayText();
    	if (!IMEisEnabled()) {
    		
    	}
    	
    }
    
    /**
     * Lets you know if the ime is active and inputting
     * @return if the ime is active
     */
    public boolean IMEisSelected() {
    	boolean isSelected = false;
       	for (InputMethodInfo imo: ims.getInputMethodList()) {
    		if (imo.getServiceName().equals(packagename)) {
    			//print("enabled");
    			isSelected = true;
    		}
    	}
       	return isSelected;
    }
    
    /**
     * Lets you know whether or not the ime is capable of being activated
     * @return if the ime is enabled
     */
    public boolean IMEisEnabled() {
    	boolean isEnabled = false;
    	for (InputMethodInfo imo: ims.getEnabledInputMethodList()) {
    		if (imo.getServiceName().equals(packagename)) {
    			//print("enabled");
    			isEnabled = true;
    		}
    	}
    	return isEnabled;
    }
    
    /**
     * Helper method for working with perma-storage
     * @param key Key to be mapped
     * @param value value to be mapped
     */
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getPreferences(0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    /**
     * Helper method for getting things out of perma storage
     * @param mem value to be received
     * @param def default if mem is unavailable
     * @return the value mapped to mem
     */
    private String LoadPreferences(String mem, String def) {
    	try {
    		SharedPreferences sharedPreferences = getPreferences(0);
    		String s = sharedPreferences.getString(mem, def);
    		print(s);
    		print("1");
    		return s;
    	} catch (Exception e) {
    		print("2");
    		return def;
    	}
    }
}


/*
//The Place where old code goes to die

private void fillText() {
if (LoadPreferences("port","8887")==""&&true&&!false) {
	return;
}
String lp = LoadPreferences("port","8887").toString().trim();
EditText et = (EditText)findViewById(R.id.port);
et.setText(""+Integer.parseInt(lp));

lp = LoadPreferences("http","8081").toString().trim();
et = (EditText)findViewById(R.id.porthttp);
et.setText(""+Integer.parseInt(lp));

//Needs to be integrated into button connect
 * 
 * 
 * 
public void httpChange(View v) {
	print("Changing HTTP state");
	CheckBox cb = (CheckBox)findViewById(R.id.http);
	if (cb.isChecked()) {
		if (fs==null) {
			try {
				fs = new FileServer(8081, new File("file:///android_asset/"),this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	else if(true){
		fs.stop();
		fs = null;
	}
}
}

//These guys were never even used

private ServiceConnection mConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder service) {
        mService = new Messenger(service);
        //textStatus.setText("Attached.");
        try {
            Message msg = new Message();//Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            //msg.replyTo = mMessenger;
            mService.send(msg);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
        }
    }
    public void onServiceDisconnected(ComponentName className) {
        // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
        mService = null;
        //textStatus.setText("Disconnected.");
    }
};

private void doBindService() {
    bindService(new Intent(this, WifiJoy.class), mConnection, Context.BIND_AUTO_CREATE);
    //mIsBound = true;
    //textStatus.setText("Binding.");;;;
}

//Don't know about this guy
private void failIp(int i,EditText e) {
    print("Bad ip address: "+i);
   	e.setText("");
}


//Old old old stuff
 
 
/*private void openSocket(String s) {
	ip = s;
	//new Thread(new AsciiSocketController(ip,1234,new LogShell((TextView)findViewById(R.id.loger)))).start();
}*/


