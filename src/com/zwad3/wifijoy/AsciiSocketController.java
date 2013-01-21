package com.zwad3.wifijoy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

public class AsciiSocketController implements Runnable {
	private String ip;
	private int port;
	PrintWriter out;
	BufferedReader in;
	public boolean read = true;
	WifiJoy wk;
	Socket testConn;
	IntReceiver iprec;
	
	public AsciiSocketController(String ip, int port,WifiJoy wk) {
		this.ip = ip;
		this.port = port;
		this.wk = wk;
		print("created");
		//IntentFilter itf = new IntentFilter("com.zwad3.wifijoy.GET_IP");
		//Log.d("Wifi Joy", itf.toString());
		//iprec = new IntReceiver(this);
		//this.registerReceiver(iprec, itf);
	}
	public AsciiSocketController(String ip) {
		this.ip = ip;
		this.port = 1234;
	}
	private void print(String s) {
    	//t.print(s);
		Log.d("Wifi Joystick ASC",s);
	}
	public void run() {
		try {
			testConn = new Socket(ip,port);
			out = new PrintWriter(testConn.getOutputStream(), true);
			out.println("hi - i need something less ambiguous..");
			in = new BufferedReader(new InputStreamReader(testConn.getInputStream()));
			print("working");
			reader();
		} catch (Exception e) {
			print(e.toString());
		}
	}
	public void kill() {
		in = null;
		read = false;
		try {
		testConn.close();
		} catch (Exception e) {
			print(e.toString());
		}

	}
	public void reconn(String s) {
		try {
			print("trying to reconnect");
			testConn = new Socket(s,port);
			out = new PrintWriter(testConn.getOutputStream(), true);
			out.println("hi - you have sufficiently reconnected");
			in = new BufferedReader(new InputStreamReader(testConn.getInputStream()));
			print("working - lol");
			reader();
		} catch (Exception e) {
			print(e.toString());
		}
	}
	private void reader() {
		try {
			while(read) {
				String s = in.readLine();
				int i;
				int a;
				char cmd = s.charAt(0);
				char arg = s.charAt(1);
				char type = s.charAt(2);
				s = s.substring(3);
				InputConnection ic = wk.getCurrentInputConnection(); 
				//wk.recieveKey(Integer.parseInt(s))
				if (cmd=='K') {
					try {
						int tmp1 = Integer.parseInt(s);
						print("Sent + "+tmp1);
						int tmp2 = KeycodeConverter.convertKey(tmp1);
						print("Received + "+tmp2);
						char tmp3 = (Character.toChars(tmp2)[0]);
						print("Translated as (c) + "+tmp3);
						String tmp4 = String.valueOf(tmp3);
						print("Translated as (s) + "+tmp4);
						i = tmp2;
						//i = Character.toChars(KeycodeConverter.convertKey(Integer.parseInt(s))).toString();
						//a = Integer.parseInt(s);
					} catch (NumberFormatException e) {
						i = KeycodeConverter.convertKey((int)s.charAt(0));
					//a = (int)i.charAt(0);
					}
					if (arg=='D') {
						ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, i));
						print("Key Down");
					}
					if (arg=='U') {
						ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, i));
						print("Key Up");
					}
				}
				//ic.commitText(i,1);
				//ic.commitText(String.valueOf((char)KeyEvent.KEYCODE_A), 1);
				//print("recieved + "+s);
				//Intendsadsaddst i = new Intent("com.zwad3.wifijoy.GET_IP");;
				//i.putExtra("ip", s);
				//act.sendBroadcast(i);
			}
		} catch (Exception e) {
			print("Disconnected");
		}
	}
	public void send(String s) {
		try {
			out.println(s);
		} catch (Exception e) {
			print(e.toString());
		}
	}
	public void intentRecieved(Intent i) {
		// TODO Auto-generated method stub
		
	}
}
