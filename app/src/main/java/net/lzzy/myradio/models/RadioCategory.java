package net.lzzy.myradio.models;

import android.os.Parcel;
import android.os.Parcelable;

import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 电台类别
 */
public class RadioCategory implements Jsonable, Parcelable {
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

    protected RadioCategory(Parcel in) {
        id = in.readInt();
        title = in.readString();
        pid = in.readInt();
    }

    public static final Creator<RadioCategory> CREATOR = new Creator<RadioCategory>() {
        @Override
        public RadioCategory createFromParcel(Parcel in) {
            return new RadioCategory(in);
        }

        @Override
        public RadioCategory[] newArray(int size) {
            return new RadioCategory[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(pid);
    }
}
