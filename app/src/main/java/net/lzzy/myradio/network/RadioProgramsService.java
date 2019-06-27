package net.lzzy.myradio.network;

import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.utils.DateTimeUtils;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/6/5
 * Description:
 */
public class RadioProgramsService {

    public static String getRadioProgramsFromServer(int contentId,String day) throws IOException {
        return ApiService.okGet(ApiConstants.getRadioPlayList(contentId,day));
    }
    //解析到电台节目列表
    public static List<PlayList> getRadioPrograms(int contentId, String day)
            throws IllegalAccessException, JSONException, InstantiationException, IOException {
        String json=ApiService.okGet(ApiConstants.getRadioPlayList(contentId,day));
        JsonConverter<PlayList> converter = new JsonConverter<>(PlayList.class);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        List<PlayList> radioPrograms=converter.getArray(jsonObject1.getString(day));
        for (PlayList radioPrograms1:radioPrograms){
            try {
                radioPrograms1.setPlay(DateTimeUtils.isPlayTime(radioPrograms1.
                        getStartTime(),radioPrograms1.getEndTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return radioPrograms;
    }

}
