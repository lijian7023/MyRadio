package net.lzzy.myradio.models;

import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 电台类别
 */
public class RadioCategory implements Jsonable {
    private int id;
    private String title;
    private int pid;

    public RadioCategory() {
    }

    public RadioCategory(int id, String title, int pid) {
        this.id = id;
        this.title = title;
        this.pid = pid;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) {
        try {
            id=json.getInt("id");
            title=json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pid=json.getInt("pid");
        } catch (JSONException e) {
            e.printStackTrace();
            pid=-1;
        }

    }
}
