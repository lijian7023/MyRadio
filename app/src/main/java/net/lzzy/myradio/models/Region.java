package net.lzzy.myradio.models;

import android.os.Parcel;
import android.os.Parcelable;

import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 地区
 */
public class Region implements Jsonable, Parcelable {
    private int id;
    private String title;

    public Region(){

    }

    public Region(int id, String title) {
        this.id = id;
        this.title = title;
    }

    protected Region(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel in) {
            return new Region(in);
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };

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

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {
            id=json.getInt("id");
            title=json.getString("title");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
    }
}
