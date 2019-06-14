package net.lzzy.myradio.constants;

import net.lzzy.myradio.utils.AppUtils;


/**
 * Created by lzzy_gxy on 2019/4/15.
 * Description:
 */
public class ApiConstants {
    public static final String GET_QUOTE="http://api.avatardata.cn/MingRenMingYan/Random?key=13c39d3354c5442496fb44f53542ac6c";
    /**
     * 获取所有地区
     */
    public static final String GET_REGION="https://rapi.qingting.fm/regions";

    /**
     * 获取所有电台类别
     */
    public static final String GET_ALL_RADIO_CATEGORY ="https://rapi.qingting.fm/categories?type=channel";


    /**
     * 获取当前的所在地区
     */
    public static final String GET_THIS_REGION="https://ip.qingting.fm/ip";
    public static final String JSON_RADIO_CONTENT_ID = "content_id";
    public static final String JSON_RADIO_CONTENT_TYPE = "content_type";
    public static final String JSON_RADIO_COVER = "cover";
    public static final String JSON_RADIO_TITLE = "title";
    public static final String JSON_RADIO_DESCRIPTION = "description";
    /**
     * 获取到电台节目列表
     */
    private static final String GET_RADIO_PLAYLIST_A="https://rapi.qingting.fm/v2/channels";


    private static final String GET_RADIO_PLAYLIST_B="/playbills?day=";
    public  static  String getRadioPlayList(int contenId, String day){
        return GET_RADIO_PLAYLIST_A+contenId+GET_RADIO_PLAYLIST_B+day;
    }
    public static final String JSON_RADIO_PLAY_ID="id";
    public static final String JSON_RADIO_PLAY_START_TIME = "start_time";
    public static final String JSON_RADIO_PLAY_END_TIME = "end_time";
    public static final String JSON_RADIO_PLAY_DURATION = "duration";
    public static final String JSON_RADIO_PLAY_RES_ID = "res_id";
    public static final String JSON_RADIO_PLAY_DAY = "day";
    public static final String JSON_RADIO_PLAY_CHANNEL_ID = "channel_id";
    public static final String JSON_RADIO_PLAY_PROGRAM_ID = "program_id";
    public static final String JSON_RADIO_PLAY_TITLE = "title";
    public static final String JSON_RADIO_PLAY_PLAY_ING="playIng";
    /**
     * 播音员
     */
    public static final String JSON_RADIO_ANNOUNCER_ID = "id";
    public static final String JSON_RADIO_ANNOUNCER_USER_NAME = "userName";
    public static final String JSON_RADIO_ANNOUNCER_THUMB = "thumb";
    public static final String JSON_RADIO_ANNOUNCER_WEIBO_NAME = "weiboName";
    public static final String JSON_RADIO_ANNOUNCER_WEIBO_ID = "weiboId";


    /**
     * 获取当前地区的电台
     */
    private static final String GET_RADIO_A="https://rapi.qingting.fm/categories/";
    private static final String GET_RADIO_B="/channels?with_total=true&page=";
    private static final String GET_RADIO_C="&pagesize=";
    public static String getRadio(int regionId,int page,int pagesize){
        return GET_RADIO_A+regionId+GET_RADIO_B+page+GET_RADIO_C+pagesize;
    }



}
