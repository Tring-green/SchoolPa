package com.example.schoolpa.lib.Core;

import android.util.Log;

import com.example.schoolpa.lib.Callback.SPChatCallBack;
import com.example.schoolpa.lib.Message.ChatMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ChatRequest {
	private SPChatCallBack callBack;
	private ChatMessage message;

	private String sequence;
	private Map<String, Object> map;

	public ChatRequest(SPChatCallBack callBack, ChatMessage message) {
		super();
		this.callBack = callBack;
		this.message = message;

		map = new HashMap<String, Object>();
		if (message != null) {
			map.putAll(this.message.getMap());
			sequence = (String) map.get("sequence");
		}
	}

	public String getSequence() {
		return sequence;
	}

	public String getTransport() {
		Log.d("", "" + map.toString());
		
		return new Gson().toJson(map);
	}

	public SPChatCallBack getCallBack() {
		return callBack;
	}

	public void setAccount(String account) {
		if (map != null) {
			map.put("account", account);
		}
	}

	public void setToken(String token) {
		if (map != null) {
			map.put("token", token);
		}
	}
}
