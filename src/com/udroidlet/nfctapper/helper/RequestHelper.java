package com.udroidlet.nfctapper.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.udroidlet.nfctapper.interfaces.UrlLoadedCallback;

import android.os.AsyncTask;

public class RequestHelper extends AsyncTask<String, Void, String> {

	HttpClient httpclient = new DefaultHttpClient();
	HttpResponse response;
	String responseString = null;
	UrlLoadedCallback mUrlLoadedCallback;
	
	
	public RequestHelper(UrlLoadedCallback urlLoadedCallback){
		mUrlLoadedCallback = urlLoadedCallback;
	}
	
	@Override
	protected String doInBackground(String... uri) {
		// TODO Auto-generated method stub

		try {
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}

		} catch (ClientProtocolException e) {
			// TODO Handle problems..
		} catch (IOException e) {
			// TODO Handle problems..
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// Do anything with response..
		mUrlLoadedCallback.onUrlLoaded(responseString);
	}

}
