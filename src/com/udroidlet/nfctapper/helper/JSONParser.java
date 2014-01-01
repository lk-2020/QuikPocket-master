package com.udroidlet.nfctapper.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.udroidlet.nfctapper.interfaces.UrlLoadedCallback;

public class JSONParser extends AsyncTask<JSONObject, Void, JSONObject> {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	UrlLoadedCallback mUrlLoadedCallback;
	String API_ROOT = "http://thawing-journey-1328.herokuapp.com/isValid";

	// constructor
	public JSONParser(UrlLoadedCallback urlLoadedCallback) {
		mUrlLoadedCallback = urlLoadedCallback;
	}

	protected JSONObject doInBackground(JSONObject... jsonObjects) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
			HttpResponse httpResponse;
			HttpPost httpPost = new HttpPost(API_ROOT);
            StringEntity se = new StringEntity( jsonObjects[0].toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            
			httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	protected void onPostExecute(JSONObject result) {
		mUrlLoadedCallback.onJSONParsed(result);
	}

}