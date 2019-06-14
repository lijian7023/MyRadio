package net.lzzy.myradio.network;

import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.Location;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.models.RadioCategory;
import net.lzzy.myradio.models.Region;
import net.lzzy.myradio.utils.DateTimeUtils;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
  public static List<PlayList> getPlayList(Integer contentId,String day) throws IOException, JSONException {
      String json = RequestDateService.getPlayList(contentId, day);
      List<PlayList> playLists = new ArrayList<>();
      JSONObject jsonObject = new JSONObject(json);
      JSONArray jsonArray = jsonObject.getJSONObject("date").getJSONArray(day);
//      Gson gson=new Gson();
//      for (int i = 0; i<jsonArray.length();i++){
//          JSONObject object =jsonArray.getJSONObject(i);
//          PlayList playList=gson.fromJson(object.toString(),playLists.class);
//          playList.setPlayIng(DateTimeUtils.playIngf(playList.getStart_time(),playList.getEnd_time()));
 // }
      return playLists;
  }
}
