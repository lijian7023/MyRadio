package net.lzzy.myradio.models;

import net.lzzy.sqllib.AsPrimaryKey;
import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;

import java.util.UUID;

public class Favorite implements Sqlitable {

    @Ignored
    public static final String COl_RADIO_ID="radioId";
    private int radioId;
    private String title;
    private int audienceCount;
    private String cover;

    public int getRadioId() {
        return radioId;
    }

    public void setRadioId(int radioId) {
        this.radioId = radioId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(int audienceCount) {
        this.audienceCount = audienceCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Favorite() {

    }
    public Favorite(int radioId) {
        this.radioId = radioId;

    }

    public int getQuestionId() {
        return radioId;
    }

    public void setQuestionId(int radioId) {
        this.radioId = radioId;
    }

    @Override
    public Object getIdentityValue() {
        return null;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }
}
