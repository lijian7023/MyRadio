package net.lzzy.myradio.models;

import android.content.Context;
import android.content.SharedPreferences;


import androidx.core.util.Pair;

import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.myradio.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserCookies {
    private SharedPreferences spAppCount;
    private SharedPreferences spAppmonthName;
    private SharedPreferences spAppmonthCount;
    private SharedPreferences spRegions;
    private SharedPreferences spRegionsName;
    private SharedPreferences spRadio;
    private SharedPreferences spRadioName;
    private SharedPreferences spRadioPrograms;
    private SharedPreferences spRadioProgramsName;
    private SharedPreferences spCategory;
    private SharedPreferences spCategoryName;



    private static final String REFRESH_SP_APP_VISIT_COUNT = "refresh_sp_app_visit_count";
    private static final String REFRESH_SP_REGION_VISIT_COUNT = "refresh_sp_region_visit_count";
    private SharedPreferences spAppVisitCount;
    private SharedPreferences spRegionVisitCount;
    private static final UserCookies INSTANCE = new UserCookies();



    private UserCookies() {
        spAppCount = AppUtils.getContext()
                .getSharedPreferences("sp_app_count", Context.MODE_PRIVATE);
        spAppmonthName = AppUtils.getContext()
                .getSharedPreferences("sp_app_month_name", Context.MODE_PRIVATE);
        spAppmonthCount = AppUtils.getContext()
                .getSharedPreferences("sp_app_month_count", Context.MODE_PRIVATE);
        spRegions = AppUtils.getContext()
                .getSharedPreferences("sp_Regions", Context.MODE_PRIVATE);
        spRegionsName = AppUtils.getContext()
                .getSharedPreferences("sp_Regions_name", Context.MODE_PRIVATE);
        spRadio = AppUtils.getContext()
                .getSharedPreferences("sp_radio_count", Context.MODE_PRIVATE);
        spRadioName = AppUtils.getContext()
                .getSharedPreferences("sp_radio_name", Context.MODE_PRIVATE);
        spRadioPrograms = AppUtils.getContext()
                .getSharedPreferences("sp_radio_programs", Context.MODE_PRIVATE);
        spRadioProgramsName = AppUtils.getContext()
                .getSharedPreferences("sp_radio_programs_name", Context.MODE_PRIVATE);
        spCategory = AppUtils.getContext()
                .getSharedPreferences("sp_category", Context.MODE_PRIVATE);
        spCategoryName = AppUtils.getContext()
                .getSharedPreferences("sp_category_name", Context.MODE_PRIVATE);


        spAppVisitCount = AppUtils.getContext()
                .getSharedPreferences(REFRESH_SP_APP_VISIT_COUNT, Context.MODE_PRIVATE);
        spRegionVisitCount= AppUtils.getContext()
                .getSharedPreferences(REFRESH_SP_REGION_VISIT_COUNT, Context.MODE_PRIVATE);
    }

    public static UserCookies getInstance() {
        return INSTANCE;
    }

    private String getKey(String id, Date date) {
        return id + "," + DateTimeUtils.DATE_FORMAT_KAY.format(date);
    }

    //region APP访问量分析

    /**
     * 获取使用量
     **/
    public int getAppCount(String times) {
        return spAppCount.getInt(times, 0);
    }

    /**
     * 保存使用量
     **/
    public void updateReadCount() {
        String time = DateTimeUtils.DATE_FORMAT.format(new Date());
        int count = getAppCount(time) + 1;
        spAppCount.edit().putInt(time, count).apply();
    }
    //endregion

    //region 地区访问量分析

    /** 保存地区名称 **/
    public void updateReadRegionsName(String id,String name){
        spRegionsName.edit().putString(id,name).apply();
    }

    /** 获取地区访问量**/
    public int getRegionsCount(String id,Date date){
        return spRegions.getInt(getKey(id,date),0);
    }

    /** 保存地区访问量 **/
    public void updateReadRegionsCount(String id,Date date){
        int count=getRegionsCount(id,date)+1;
        spRegions.edit().putInt(getKey(id,date),count).apply();
    }

    /** 根据天数获取地区访问量 **/
    private int getRegionsSumOfDays(String id,Date date,int days){
        int sum=0;
        for (int i=0;i<=days;i++){
            Date day=DateTimeUtils.getTheDayBefor(date,i);
            sum+=spRegions.getInt(getKey(id,day),0);
        }
        return sum;
    }

    /** 获取地区名称与访问次数 **/
    public List<Pair<String,Integer>> getRegionsFrequency(Date date,int days){
        List<Pair<String,Integer>> frequencies=new ArrayList<>();
        Map<String,?> map=spRegionsName.getAll();
        for (Map.Entry<String, ?> entry:map.entrySet()){
            String id=((Map.Entry)entry).getKey().toString();
            String name=((Map.Entry)entry).getValue().toString();
            int count= getRegionsSumOfDays(id,date,days);
            frequencies.add(new Pair<>(name,count));
        }
        return frequencies;

    }

    //endregion

    //region 电台访问量分析

    /** 保存电台名称 **/
    public void updateRadioName(String id,String name){
        spRadioName.edit().putString(id,name).apply();
    }

    /** 获取电台访问次数 **/
    public int getRadio(String id,Date date){
        return spRadio.getInt(getKey(id,date),0);
    }

    /** 保存电台访问次数 **/
    public void updateRadio(String id,Date date){
        int count=getRadio(id,date)+1;
        spRadio.edit().putInt(getKey(id,date),count).apply();
    }
    /** 根据天数获取电台访问量 **/
    private int getRadioSumOfDays(String id, Date date, int days){
        int sum=0;
        for (int i=0;i<=days;i++){
            Date day=DateTimeUtils.getTheDayBefor(date,i);
            sum+=spRadio.getInt(getKey(id,day),0);
        }
        return sum;
    }

    /** 获取电台名称与访问次数 **/
    public List<Pair<String,Integer>> getRadioFrequency(Date to, int days){
        List<Pair<String,Integer>> frequencies=new ArrayList<>();
        Map<String,?> map=spRadioName.getAll();
        for (Map.Entry<String, ?> entry:map.entrySet()){
            String id=((Map.Entry)entry).getKey().toString();
            String name=((Map.Entry)entry).getValue().toString();
            int count= getRadioSumOfDays(id,to,days);
            frequencies.add(new Pair<>(name,count));
        }
        return frequencies;

    }
    //endregion

    //region 电台节目访问量分析


    /** 保存节目名称 **/
    public void updateRadioProgramsName(String id,String name){
        spRadioProgramsName.edit().putString(id,name).apply();
    }

    /** 获取节目访问次数 **/
    public int getRadioPrograms(String id,Date date){
        return spRadioPrograms.getInt(getKey(id,date),0);
    }

    /** 保存节目访问次数 **/
    public void updateRadioPrograms(String id,Date date){
        int count=getRadioPrograms(id,date)+1;
        spRadioPrograms.edit().putInt(getKey(id,date),count).apply();
    }

    /** 根据天数获取节目访问量 **/
    private int getProgramsSumOfDays(String id, Date date, int days){
        int sum=0;
        for (int i=0;i<=days;i++){
            Date day=DateTimeUtils.getTheDayBefor(date,i);
            sum+=spRadioPrograms.getInt(getKey(id,day),0);
        }
        return sum;
    }


    /** 获取电台节目名称与访问次数 **/
    public List<Pair<String,Integer>> getProgramsFrequency(Date to, int days){
        List<Pair<String,Integer>> frequencies=new ArrayList<>();
        Map<String,?> map=spRadioProgramsName.getAll();
        for (Map.Entry<String, ?> entry:map.entrySet()){
            String id=((Map.Entry)entry).getKey().toString();
            String name=((Map.Entry)entry).getValue().toString();
            int count= getProgramsSumOfDays(id,to,days);
            frequencies.add(new Pair<>(name,count));
        }
        return frequencies;

    }

    //endregion



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
