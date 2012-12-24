package com.itstand.me;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {
	
	public static JSONObject getJSONfromURL(String url) {
		InputStream inputStream = null;
		String result = "";
		JSONObject jArray = null;
		
	    try {
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(url);
	        HttpResponse response = httpclient.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        inputStream = entity.getContent();
	    } catch(Exception e) {
	    	Log.e("log_tag", "Error in http connection " + e.toString());
	    }
	    
	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	        	sb.append(line + "\n");
	        }
	        inputStream.close();
	        result = sb.toString();
	    } catch(Exception e) {
	    	Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    
	    try {
	    	jArray = new JSONObject(result);            
	    } catch(JSONException e) {
	    	Log.e("log_tag", "Error parsing data "+e.toString());
	    }
    
	    return jArray;
	}

}
