package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {
    public String name;
    public String username;
    public Profile(){}
    public static Profile fromJson(JSONObject jsonObject) throws JSONException {
        Profile profile = new Profile();
        profile.name = jsonObject.getString("name");
        profile.username = jsonObject.getString("username");
        return profile;
    }
}
