package com.zwad3.wifijoy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
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

/**
 * 
 * @author zacharywade
 * This is a custom FileServer extending NanoHTTPD
 * It serves up files based on uri and platform.
 *
 */
public class FileServer extends NanoHTTPD {
	
	Context c;
	private static Map<String, String> fileNames = new HashMap<String, String>();

	/**
	 * 
	 * @param port The port to be used
	 * @param rt (Not really Used - Any file will work)
	 * @param c The context of the app
	 * @throws IOException Have no idea what this is, the example used it so I do too
	 */
	public FileServer(int port, File rt, Context c) throws IOException {
		super(port, rt);
		this.c= c; 
		fileNames.put("/pizza", "svgtest.html");
		fileNames.put("/mobile", "websocket_mobile.html");
		fileNames.put("/home", "websocket.html");
		fileNames.put("/jquery_mobile", "jquery_mobile.js");
		fileNames.put("/desktop_selector", "desktop_selector.html");
		//fileNames.put("/test.svg", "test.svg");
		
	}
	/**
	 * 
	 * @param s Pretty much just a log method
	 * Nice and boring!
	 */
	private void print(String s){ 
		Log.d("FileServer",s);
	}
	/**
	 * Yeah I have no idea what this really is, it gets called, and stuff happens
	 * 
	 */
	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{
		print("Header: "+header);
		print("Params: "+parms);
		print("Method: "+method);
		print("URI: "+uri);
		UserAgent ua = UserAgent.parseUserAgentString((String)header.get("user-agent"));
		print("Is mobile? "+ua.getOperatingSystem().isMobileDevice());
		String f;
		String m = MIME_HTML;
		String n;
		
		if (fileNames.containsKey(uri)) {
			f = parse(fileNames.get(uri));
			n = fileNames.get(uri);
		} else if (uri.equals("/")) { 
			if (ua.getOperatingSystem().isMobileDevice()) {
				f = parse("websocket_mobile.html");//Changed
				n = "websocket_mobile.html";
			} else {
				f = parse("websocket.html");
				n = "websocket.html";
			}
		} else {
			if (uri.charAt(0)=='/') {
				uri = uri.substring(1);
			}
			f = parse(uri);
			n = uri;
		}
		String[] tmp = n.split(Pattern.quote("."));
		Log.d("I'M HEEEERE",n+" "+tmp+" "+tmp.length); 	
		String extension = tmp[tmp.length-1];
		//Serves up a mime type of HTML if it is..... .html
		print("Extension: "+tmp);
		m = getMime(extension);
		return new NanoHTTPD.Response( HTTP_OK, m,f);
	}
	
	/**
	 * 
	 * @param uri - Uri of file
	 * @return a String representation of the file found at URI
	 * 
	 * Parses out:
	 *     {{ipaddr}} - Replaced with the ipaddress and port
	 */
	private String parse(String uri) {
		try {
			Resources am = c.getResources();
			BufferedReader s =  new BufferedReader(new InputStreamReader(am.getAssets().open(uri)));
			String t = "";
			while (s.ready()) {
				String l = s.readLine();
				String[] splitted = l.split(Pattern.quote("{{ipaddr}}"));
				/**
				 * Parses out the ip address
				 */
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
	
	/**
	 * 
	 * @param ext - The file extension
	 * @return the mime type
	 */
	private String getMime(String ext) {
		try {
			mimes cur = mimes.valueOf(ext.toLowerCase());
			switch (cur) {
				case html: return "text/html";
				case js: return "application/javascript";
				case css: return "text/css";
				
				case jpeg:
				case jpg: return "image/jpeg";
				case png: return "image/png";
				case gif: return "image/gif";
				case svg: return "image/svg+xml";
				case ico: return "image/vnd.microsoft.icon";
				
				case txt: return "text/plain";
	
				case mp4: return "video/mp4";
				case webm: return "video/webm";
				
				default: return "text/html";
			}
		} catch (Exception e) {
			print(e.toString());
			return "text/html";
		}
	}
	
	/**
	 * 
	 * @author zacharywade
	 *List of MIME Types
	 */
	private enum mimes {
		html, //Language
		js,
		css,
		
		jpg, //Images
		jpeg,
		png,
		gif,
		svg,
		ico,
		
		txt, //Aux

		mp4, //Video
		webm,
		
		//TODO: Figure out how to differentiate between audio and video
	}

}
