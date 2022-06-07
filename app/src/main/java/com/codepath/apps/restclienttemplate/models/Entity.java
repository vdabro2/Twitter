package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Entity {

    public String imageURL;
    public Entity(){

    }
    public static Entity fromJson(JSONObject jsonObject) throws JSONException {
        Entity entity1 = new Entity();
        JSONObject media;
        media = jsonObject.getJSONArray("media").getJSONObject(0);
        try {
            entity1.imageURL = media.getString("media_url");
        } catch (JSONException e) {
            entity1.imageURL = "";
            Log.i("YOU ARE HERE", "ksjnd");
            e.printStackTrace();
        }
        return entity1;
    }
}
