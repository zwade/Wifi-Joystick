package com.zwad3.wifijoy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

public class WSServ extends WebSocketServer
{
	private WifiJoy wj;
	int port;
	
	public WSServ(int paramInt, WifiJoy wj) throws UnknownHostException
	{
		super(new InetSocketAddress("0.0.0.0",paramInt));
		print("starting on "+paramInt);
		this.wj = wj;
		port = paramInt;
	}
	private void print(String s) {
		Log.d("WSS", s);
	}
	public WSServ(InetSocketAddress paramInetSocketAddress)
	{
		super(paramInetSocketAddress);
		port = 0;
	}

	public void onOpen(WebSocket paramWebSocket, ClientHandshake paramClientHandshake)
	{
		print("Opened");
		sendToAll("new connection: " + paramClientHandshake.getResourceDescriptor());
		System.out.println(paramWebSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
	}

	public void onClose(WebSocket paramWebSocket, int paramInt, String paramString, boolean paramBoolean)
	{
		sendToAll(paramWebSocket + " has left the room!");
		System.out.println(paramWebSocket + " has left the room!");
	}

	public void onMessage(WebSocket paramWebSocket, String s)
	{
		//sendToAll(paramString);
		//System.out.println(paramWebSocket + ": " + paramString);
		int i;
		int a;
		char cmd = s.charAt(0);
		char arg = s.charAt(1);
		char type = s.charAt(2);
		s = s.substring(4);
		InputConnection ic = wj.getCurrentInputConnection(); 
		//wk.recieveKey(Integer.parseInt(s))
		print(""+cmd+" "+arg+" "+type);
		if (cmd=='K') {
			try {
				print("da crap");
				int tmp1 = Integer.parseInt(s);
				print("Sent + "+tmp1);
				int tmp2 = KeycodeConverter.convertKey(tmp1);
				print("Received + "+tmp2);
				//char tmp3 = (Character.toChars(tmp2)[0]);
				//print("Translated as (c) + "+tmp3);
				//String tmp4 = String.valueOf(tmp3);
				//print("Translated as (s) + "+tmp4);
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
	}
	public String toString() {
		return (super.toString()+" : "+port);
	}
	//public static void main(String[] paramArrayOfString)
	///	throws InterruptedException, IOException
	//{
		//WebSocket.DEBUG = true;
		//int i = 8887;
		//try
		//{
		//	i = Integer.parseInt(paramArrayOfString[0]);
		//}
		///catch (Exception localException)
		//{
		//}
		//WSServ localChatServer = new WSServ(i);
		//localChatServer.start();
		//System.out.println("ChatServer started on port: " + localChatServer.getPort());
		//BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
		//while (true)
		//{
		//	String str = localBufferedReader.readLine();
		//	localChatServer.sendToAll(str);
		//}
	//}
	/*
	@Override
	public void run() {
		ServerSocket ss;
		print("Starting to Socket");
		try {
			ss = new ServerSocket(8887);
			print(ss.accept().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	public void onError(WebSocket paramWebSocket, Exception paramException)
	{
		paramException.printStackTrace();
		if (paramWebSocket != null);
	}

	public void sendToAll(String paramString)
	{
		Collection localCollection = connections();
		synchronized (localCollection)
		{
			Iterator localIterator = localCollection.iterator();
			while (localIterator.hasNext())
			{
				WebSocket localWebSocket = (WebSocket)localIterator.next();
				localWebSocket.send(paramString);
			}
		}
	}
}
