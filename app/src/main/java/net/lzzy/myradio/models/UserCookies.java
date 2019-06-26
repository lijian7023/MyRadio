package net.lzzy.myradio.models;

import android.content.Context;
import android.content.SharedPreferences;

import net.lzzy.myradio.utils.AppUtils;

public class UserCookies {

    private static final String REFRESH_SP_APP_VISIT_COUNT = "refresh_sp_app_visit_count";
    private static final String REFRESH_SP_REGION_VISIT_COUNT = "refresh_sp_region_visit_count";
    private SharedPreferences spAppVisitCount;
    private SharedPreferences spRegionVisitCount;
    private static final UserCookies INSTANCE = new UserCookies();
    private SharedPreferences spRadioProgramsName;


    private UserCookies() {
        spAppVisitCount = AppUtils.getContext()
                .getSharedPreferences(REFRESH_SP_APP_VISIT_COUNT, Context.MODE_PRIVATE);
        spRegionVisitCount= AppUtils.getContext()
                .getSharedPreferences(REFRESH_SP_REGION_VISIT_COUNT, Context.MODE_PRIVATE);

    }

    public static UserCookies getInstance() {
        return INSTANCE;
    }

    /**
     * 更新app访问数量
     * @param count 数量
     * @param date 当前日期
     */
    public void UpdateAppVisitCount(int count, String date){
        int thisVisitCount= spAppVisitCount.getInt(date,0);
        spAppVisitCount.edit().putInt(date,thisVisitCount+count).apply();
    }

    /**
     * 获取app访问数量
     * @param date 查询日期
     * @return app访问数量
     */
    public int GetAppVisitCount(String date){
        return spAppVisitCount.getInt(date,0);
    }

    /**
     * 更新地区访问数量
     * @param count 数量
     * @param regionName 地区名字
     */
    public void  UpdateRegionVisitCount(int count,String regionName){
        int thisVisitCount= spRegionVisitCount.getInt(regionName,0);
        spRegionVisitCount.edit().putInt(regionName,thisVisitCount+count).apply();
    }

    /**
     * 获取地区访问数量
     * @param regionName  查询地区名字
     * @return 地区访问数量
     */
    public int GetRegionVisitCount(String regionName){
        return spRegionVisitCount.getInt(regionName,0);
    }
}
