package net.lzzy.myradio.network;

import net.lzzy.myradio.constants.ApiConstants;

import java.io.IOException;

/**
 * Created by lzzy_gxy on 2019/6/14.
 * Description:
 */
public class RequestDateService {
    public static String getPlayList(Integer contentId, String day) throws IOException {
        return ApiService.okGet(ApiConstants.getRadioPlayList(contentId,day));
    }
}
