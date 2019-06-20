package net.lzzy.myradio.models;

import android.os.Parcel;
import android.os.Parcelable;

import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.sqllib.JsonConverter;
import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/6/14.
 * Description:
 */
public class PlayList implements Jsonable, Parcelable {
    private int id;
    private String startTime;
    private String endTime;
    /**  用于判断时间 **/
    private boolean isPlay;

    protected PlayList(Parcel in) {
        id = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        isPlay = in.readByte() != 0;
        duration = in.readInt();
        resId = in.readInt();
        day = in.readInt();
        channelId = in.readInt();
        programId = in.readInt();
        title = in.readString();
    }

    public static final Parcelable.Creator<PlayList> CREATOR = new Parcelable.Creator<PlayList>() {

        @Override
        public PlayList createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[0];
        }
    };

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    private int duration;
    private int resId;
    private int day;
    private int channelId;
    private int programId;
    private String title;
    private List<Broadcaster> broadcasters;

    public PlayList(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public void fromJson(JSONObject jsonObject) throws JSONException {
        id= jsonObject.getInt(ApiConstants.JSON_RADIO_PLAY_ID);
        startTime=jsonObject.getString(ApiConstants.JSON_RADIO_PLAY_START_TIME);
        endTime=jsonObject.getString(ApiConstants.JSON_RADIO_PLAY_END_TIME);
        duration=jsonObject.getInt(ApiConstants.JSON_RADIO_PLAY_DURATION);
        resId=jsonObject.getInt(ApiConstants.JSON_RADIO_PLAY_RES_ID);
        day=jsonObject.getInt(ApiConstants.JSON_RADIO_PLAY_DAY);
        channelId=jsonObject.getInt(ApiConstants.JSON_RADIO_PLAY_CHANNEL_ID);
        programId=jsonObject.getInt(ApiConstants.JSON_RADIO_PLAY_PROGRAM_ID);
        title=jsonObject.getString(ApiConstants.JSON_RADIO_PLAY_TITLE);
        isPlay=false;
        JsonConverter<Broadcaster> converter = new JsonConverter<>(Broadcaster.class);
        try {
            broadcasters=converter.getArray(jsonObject.getString("broadcasters"));
        } catch (Exception e) {
            e.printStackTrace();
            broadcasters=new ArrayList<>();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeByte((byte) (isPlay ? 1 : 0));
        dest.writeInt(duration);
        dest.writeInt(resId);
        dest.writeInt(day);
        dest.writeInt(channelId);
        dest.writeInt(programId);
        dest.writeString(title);
    }
}



