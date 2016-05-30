package com.testing.testfunction.request;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by admin on 2016/5/29.
 */
public class AuthRequest implements Request {

    private Map<String, String> map = new HashMap<>();

    public AuthRequest(String sender, String token) {
        map.put("type", "request");
        map.put("sequence", UUID.randomUUID().toString());
        map.put("action", "auth");
        map.put("sender", sender);
        map.put("token", token);
    }

    @Override
    public String getData() {
        return new Gson().toJson(map);
    }
}
