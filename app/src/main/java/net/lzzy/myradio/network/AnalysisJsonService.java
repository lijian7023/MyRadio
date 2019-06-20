package net.lzzy.myradio.network;

import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.Location;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.models.RadioCategory;
import net.lzzy.myradio.models.Region;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class AnalysisJsonService {
    public static List<Region> getRegion(String address) throws IOException, JSONException,
            InstantiationException, IllegalAccessException {
        String json=ApiService.okGet(address);
        JsonConverter<Region> converter=new JsonConverter<>(Region.class);
        JSONObject jsonObject=new JSONObject(json);
        return converter.getArray(jsonObject.getString("Data"));
    }
    public static List<RadioCategory> getRadioCategory(String address) throws IOException, JSONException, InstantiationException, IllegalAccessException {
        String json=ApiService.okGet(address);
        JsonConverter<RadioCategory> converter=new JsonConverter<>(RadioCategory.class);
        JSONObject jsonObject=new JSONObject(json);
        return converter.getArray(jsonObject.getString("Data"));
    }
    public static String getLocation(String address) throws IOException, JSONException, InstantiationException, IllegalAccessException {
        String json = ApiService.okGet(address);
        JsonConverter<Location> converter = new JsonConverter<>(Location.class);
        Location location=converter.getSingle(json,"data");
        return location.getRegion();

    }
    public static List<Radio> getSearchRadio(String key) throws IOException, JSONException, InstantiationException, IllegalAccessException {
        String json=ApiService.okGet(ApiConstants.getSearchRadio(key));
        JSONObject jsonObject=new JSONObject(json);
        JsonConverter<Radio> converter = new JsonConverter<>(Radio.class);
        return converter.getArray(jsonObject.getJSONObject("data").getJSONArray("data").
                getJSONObject(0).getString("docs"));
    }

}
