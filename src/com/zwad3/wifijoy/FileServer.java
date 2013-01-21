package com.zwad3.wifijoy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import nl.bitwalker.useragentutils.UserAgent;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.EditText;


public class FileServer extends NanoHTTPD {
	
	Context c;

	public FileServer(int port, File rt, Context c) throws IOException {
		super(port, rt);
		this.c= c; 
		
	}
	public void print(String s){ 
		Log.d("FileServer",s);
	}
	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{
		print("Header: "+header);
		print("Params: "+parms);
		print("Method: "+method);
		print("URI: "+uri);
		UserAgent ua = UserAgent.parseUserAgentString((String)header.get("user-agent"));
		print("Is mobile? "+ua.getOperatingSystem().isMobileDevice());
		String f;
		
		if (ua.getOperatingSystem().isMobileDevice()) {
			f = parse("websocket_mobile.html");//Change
		} else {
			f = parse("websocket.html");
		}
		
		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML,f);
	}
	
	private String parse(String uri) {
		try {
			Resources am = c.getResources();
			BufferedReader s =  new BufferedReader(new InputStreamReader(am.getAssets().open(uri)));
			String t = "";
			while (s.ready()) {
				String l = s.readLine();
				String[] splitted = l.split(Pattern.quote("{{ipaddr}}"));
			
				if (splitted.length == 2) {
					int ipAddress = ((WifiManager)c.getSystemService(c.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
					

			    	String ip = null;

			    	ip = String.format("%d.%d.%d.%d",
			    	(ipAddress & 0xff),
			    	(ipAddress >> 8 & 0xff),
			    	(ipAddress >> 16 & 0xff),
			    	(ipAddress >> 24 & 0xff));
					l = splitted[0]+ip+":"+((EditText)((Activity)c).findViewById(R.id.port)).getText()+splitted[1];
					
				}
				t+=l+"\n	";
			}
			return t;
		} catch (IOException e) {
			print(e.toString());
			return "Unfortunately, there has been an internal error. Please contact the developer.";
		}
	}

}
