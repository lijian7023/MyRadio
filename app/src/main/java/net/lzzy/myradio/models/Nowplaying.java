package net.lzzy.myradio.models;

import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.sqllib.Jsonable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 正在播放的节目信息
 */
public class Nowplaying implements Jsonable {

    /**
     * id : 1880848
     * title : 警法时空
     * broadcasters : []
     * start_time : 16:00:00
     * duration : 3600
     */

    private int id;
    private String title;
    private String start_time;
    private int duration;
    private List<Broadcaster> broadcasters;

    public Nowplaying() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Broadcaster> getBroadcasters() {
        return broadcasters;
    }

    public void setBroadcasters(List<Broadcaster> broadcasters) {
        this.broadcasters = broadcasters;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {
        id=json.getInt("id");
        title=json.getString("title");
        start_time=json.getString("start_time");
        duration=json.getInt("duration");
        List<Broadcaster> broadcasterList=new ArrayList<>();
        JSONArray jsonArray=json.getJSONArray("broadcasters");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            Broadcaster broadcaster=new Broadcaster();
            broadcaster.setId(jsonObject.getInt("id"));
            broadcaster.setQq_id(jsonObject.getString("qq_id"));
            broadcaster.setQq_name(jsonObject.getString("qq_name"));
            broadcaster.setThumb(jsonObject.getString("thumb"));
            broadcaster.setUsername(jsonObject.getString("username"));
            broadcaster.setWeibo_id(jsonObject.getString("weibo_id"));
            broadcaster.setWeibo_name(jsonObject.getString("weibo_name"));
            broadcasterList.add(broadcaster);
        }
        broadcasters=broadcasterList;
    }
}
