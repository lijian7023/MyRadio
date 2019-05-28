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
