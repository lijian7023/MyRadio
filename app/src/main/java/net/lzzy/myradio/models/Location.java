package net.lzzy.myradio.models;

import android.os.Parcel;
import android.os.Parcelable;

import net.lzzy.sqllib.Jsonable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzzy_gxy on 2019/5/29.
 * Description:
 * 获取所在地区
 */
public class Location implements Jsonable, Parcelable {


    /**
     * isp : 电信
     * county_id : xx
     * isp_id : 100017
     * area :
     * county : XX
     * city_id : 450200
     * region : 广西
     * ip : 222.217.36.113
     * area_id :
     * country_id : CN
     * city : 柳州
     * country : 中国
     * region_id : 450000
     */

    private String isp;
    private String county_id;
    private String isp_id;
    private String area;
    private String county;
    private String city_id;
    private String region;
    private String ip;
    private String area_id;
    private String country_id;
    private String city;
    private String country;
    private String region_id;

    protected Location(Parcel in) {
        isp = in.readString();
        county_id = in.readString();
        isp_id = in.readString();
        area = in.readString();
        county = in.readString();
        city_id = in.readString();
        region = in.readString();
        ip = in.readString();
        area_id = in.readString();
        country_id = in.readString();
        city = in.readString();
        country = in.readString();
        region_id = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public JSONObject toJson() throws JSONException  {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {
        isp=json.getString("isp");
        county_id=json.getString("county_id");
        isp_id=json.getString("isp_id");
        area=json.getString("area");
        county=json.getString("county");
        city_id=json.getString("city_id");
        region=json.getString("region");
        ip =json.getString("ip");
        area_id =json.getString("area_id");
        country_id =json.getString("country_id");
        city =json.getString("city");
        country=json.getString("country");
        region_id=json.getString("region_id");



    }

    public Location() {
    }

    public Location(String isp, String county_id, String isp_id, String area, String county, String city_id, String region, String ip, String area_id, String country_id, String city, String country, String region_id) {
        this.isp = isp;
        this.county_id = county_id;
        this.isp_id = isp_id;
        this.area = area;
        this.county = county;
        this.city_id = city_id;
        this.region = region;
        this.ip = ip;
        this.area_id = area_id;
        this.country_id = country_id;
        this.city = city;
        this.country = country;
        this.region_id = region_id;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getCounty_id() {
        return county_id;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }

    public String getIsp_id() {
        return isp_id;
    }

    public void setIsp_id(String isp_id) {
        this.isp_id = isp_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isp);
        dest.writeString(county_id);
        dest.writeString(isp_id);
        dest.writeString(area);
        dest.writeString(county);
        dest.writeString(city_id);
        dest.writeString(region);
        dest.writeString(ip);
        dest.writeString(area_id);
        dest.writeString(country_id);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(region_id);
    }
}
