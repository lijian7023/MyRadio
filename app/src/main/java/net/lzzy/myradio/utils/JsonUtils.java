package net.lzzy.myradio.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public static String getQuote(String json) throws JSONException {
        JSONObject object=new JSONObject(json);
        return object.getJSONObject("result").getString("famous_saying");
    }
}
