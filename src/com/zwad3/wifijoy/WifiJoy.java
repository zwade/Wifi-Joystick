package com.zwad3.wifijoy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;

public class WifiJoy extends InputMethodService implements NeedRecieve,WifiKey {
	private InputConnection ic;
	private Thread asc;
	private WSServ wss;
	//private AsciiSocketController as;
	//final Messenger mMessenger = new Messenger(new IncomingHandler());
    /*ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    int mValue = 0; // Holds last value set by a client.
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_SET_INT_VALUE = 3;
    static final int MSG_SET_STRING_VALUE = 4;
    //private int counter = 0, incrementby = 1;*/
    
	
	private IPReciever iprec;
    //public final int a = 1;
    
	
/*
	class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_REGISTER_CLIENT:
                mClients.add(msg.replyTo);
                break;
            case MSG_UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;
            case MSG_SET_INT_VALUE:
                incrementby = msg.arg1;
                break;
            default:
                super.handleMessage(msg);
            }
        }
    }*/
    
/*	public WifiJoy () {
		//super();
		ic = getCurrentInputConnection();
		print(ic.toString());
	}*/
	private void print(String s) {
		Log.d("IME", s);
	}

	public void recieveKey(int i) {
		Log.d("Wifi Got Key", "Anything");
		ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, 102));
		ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, 102));
		ic.commitText("hi",1);
	}
	@Override public void onCreate() {
		super.onCreate();
		IntentFilter itf = new IntentFilter("com.zwad3.wifijoy.GET_IP");
		Log.d("Wifi Joy", itf.toString());
		iprec = new IPReciever(this);
		this.registerReceiver(iprec, itf);
		//Intent t = getIntent();
	}
   // @Override public IBinder onBind (Intent intent) {
    //    return mMessenger.getBinder();
    //}
//gfsg
	@Override public void onStartInput(EditorInfo attribute,boolean restart) {
		super.onStartInput(attribute, restart);
		ic = getCurrentInputConnection();
	}
	@Override public boolean onKeyDown(int keyCode, KeyEvent e) {
		this.getCurrentInputConnection().sendKeyEvent(e);
		return true;
	}
	@Override public boolean onKeyUp(int keyCode, KeyEvent e) {
		return true;
	}
	@Override public void onDestroy() {
		unregisterReceiver(iprec);
	}
	/*public void intentRecieved(Intent i) {
		Log.d("Wifi Joystick", "IntentRecieved");
		String s = i.getStringExtra("ip");
		if (asc==null) {
			print("creating as");
			as = new AsciiSocketController(s,1234,this); // very interesting
			asc = new Thread(as);
			asc.start();
		} else {
			Log.d("Threads:",""+Thread.activeCount());
			as = new AsciiSocketController(s,1234,this);
			asc = new Thread(as);
			print("what!!!?!?");
			asc.start();
		}
		
	}*/
	public void intentRecieved(Intent i){
		Thread tmp;
		try {
			tmp = new Thread(new WSServ(Integer.parseInt(i.getStringExtra("port")), this));
			//tmp = new ServerSocket(8887);
			
		} catch (Exception e) {
			print("unknown host "+e.toString());
			tmp = null;
		}
		//Socket s = tmp.Accept()
		//print("starting thread");
		//asc = new Thread(wss);
		//asc.start();
		//WebSocket.DEBUG=true;
		tmp.start();
		//print("Wss: "+wss.toString());
	}

}
