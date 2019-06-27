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
 *
 * @author lzzy_gxy
 * @date 2019/5/27
 * Description: 电台
 */
public class Radio implements Jsonable, Parcelable {

    private int contentId;
    private String contentType;
    private String cover;
    private String title;
    private String description;
    private Nowplaying nowplaying;
    private int audienceCount;
    private String liveShowId;
    private String updateTime;
    private List<RadioCategory> categories;
    public Radio(){}

    protected Radio(Parcel in) {
        contentId = in.readInt();
        contentType = in.readString();
        cover = in.readString();
        title = in.readString();
        description = in.readString();
        audienceCount = in.readInt();
        liveShowId = in.readString();
        updateTime = in.readString();
        categories = in.createTypedArrayList(RadioCategory.CREATOR);
    }

    public static final Creator<Radio> CREATOR = new Creator<Radio>() {
        @Override
        public Radio createFromParcel(Parcel in) {
            return new Radio(in);
        }

        @Override
        public Radio[] newArray(int size) {
            return new Radio[size];
        }
    };

    public int  getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Nowplaying getNowplaying() {
        return nowplaying;
    }

    public void setNowplaying(Nowplaying nowplaying) {
        this.nowplaying = nowplaying;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(int audienceCount) {
        this.audienceCount = audienceCount;
    }

    public String getLiveShowId() {
        return liveShowId;
    }

    public void setLiveShowId(String liveShowId) {
        this.liveShowId = liveShowId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<RadioCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<RadioCategory> categories) {
        this.categories = categories;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }
    @Override
    public void fromJson(JSONObject jsonObject) throws JSONException {
            contentId=jsonObject.getInt(ApiConstants.JSON_RADIO_CONTENT_ID);
            contentType=jsonObject.getString(ApiConstants.JSON_RADIO_CONTENT_TYPE);
            cover=jsonObject.getString(ApiConstants.JSON_RADIO_COVER);
            title=jsonObject.getString(ApiConstants.JSON_RADIO_TITLE);
            try {
                description=jsonObject.getString(ApiConstants.JSON_RADIO_DESCRIPTION);
            }catch (Exception e){
                description="";
            }
            JsonConverter<RadioCategory> converter = new JsonConverter<>(RadioCategory.class);
        try {
            categories=converter.getArray(jsonObject.getString("categories"));
        } catch (Exception e) {
            e.printStackTrace();
            categories=new ArrayList<>();
        }
        try{
            liveShowId="";
        }catch (Exception e){
            liveShowId="";
        }
        audienceCount=jsonObject.getInt("audience_count");
        updateTime=jsonObject.getString("update_time");
        jsonFromNowPlayingAndBroadcaster(jsonObject);

    }

    /**
     * 解析NowPlaying子类，和Broadcaster子类
     * @param jsonObject
     * @throws JSONException
     */
    private void jsonFromNowPlayingAndBroadcaster(JSONObject jsonObject) throws JSONException {

        JsonConverter<Nowplaying> converter = new JsonConverter<>(Nowplaying.class);
        JsonConverter<Broadcaster> converterB = new JsonConverter<>(Broadcaster.class);
        try {
            nowplaying=converter.getSingle(jsonObject.toString(),"nowplaying");
        } catch (Exception e) {
            e.printStackTrace();
            nowplaying=new Nowplaying();
        }finally {
            try {
                nowplaying.setBroadcasters( converterB.getArray(jsonObject.getJSONObject("nowplaying").getString("broadcasters")));
            } catch (Exception e) {
                e.printStackTrace();
                nowplaying.setBroadcasters(new ArrayList<>());
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contentId);
        dest.writeString(contentType);
        dest.writeString(cover);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(audienceCount);
        dest.writeString(liveShowId);
        dest.writeString(updateTime);
        dest.writeTypedList(categories);
    }
}
