package com.udroidlet.nfctapper.interfaces;

import org.json.JSONObject;


public interface UrlLoadedCallback  {
	void onUrlLoaded(String response);
	void onJSONParsed(JSONObject response);
}
