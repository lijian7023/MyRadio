package net.lzzy.myradio.models;

import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主持人
 */
public class Broadcaster implements Jsonable {

    public Broadcaster() {
    }

    public Broadcaster(int id, String username, String thumb, String weibo_name,
                       String weibo_id, String qq_name, String qq_id) {
        this.id = id;
        this.username = username;
        this.thumb = thumb;
        this.weibo_name = weibo_name;
        this.weibo_id = weibo_id;
        this.qq_name = qq_name;
        this.qq_id = qq_id;
    }

    /**
     * id : 148830
     * username : 英琦
     * thumb :
     * weibo_name :
     * weibo_id :
     * qq_name :
     * qq_id :
     */

    private int id;
    private String username;
    private String thumb;
    private String weibo_name;
    private String weibo_id;
    private String qq_name;
    private String qq_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getWeibo_name() {
        return weibo_name;
    }

    public void setWeibo_name(String weibo_name) {
        this.weibo_name = weibo_name;
    }

    public String getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(String weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getQq_name() {
        return qq_name;
    }

    public void setQq_name(String qq_name) {
        this.qq_name = qq_name;
    }

    public String getQq_id() {
        return qq_id;
    }

    public void setQq_id(String qq_id) {
        this.qq_id = qq_id;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {
        id=json.getInt("id");
        username=json.getString("username");
        thumb=json.getString("thumb");
        weibo_name=json.getString("weibo_name");
        weibo_id=json.getString("weibo_id");
        qq_name=json.getString("qq_name");
        qq_id=json.getString("qq_id");
    }
}
