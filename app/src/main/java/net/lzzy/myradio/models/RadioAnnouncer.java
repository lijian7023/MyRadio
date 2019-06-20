package net.lzzy.myradio.models;

import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.sqllib.JsonConverter;
import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lzzy_gxy on 2019/6/10.
 * Description:
 * 电台播音员
 */
public class RadioAnnouncer implements Jsonable {
    private int id;
    private String userName;
    private String thumb;
    private String weiboName;
    private String weiboId;


    public RadioAnnouncer() {
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getWeiboName() {
        return weiboName;
    }

    public void setWeiboName(String weiboName) {
        this.weiboName = weiboName;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws JSONException {
        id=jsonObject.getInt(ApiConstants.JSON_RADIO_ANNOUNCER_ID);
        userName=jsonObject.getString(ApiConstants.JSON_RADIO_ANNOUNCER_USER_NAME);
        thumb=jsonObject.getString(ApiConstants.JSON_RADIO_ANNOUNCER_THUMB);
        weiboName=jsonObject.getString(ApiConstants.JSON_RADIO_ANNOUNCER_WEIBO_NAME);
        weiboId=jsonObject.getString(ApiConstants.JSON_RADIO_ANNOUNCER_WEIBO_ID);


    }
}
