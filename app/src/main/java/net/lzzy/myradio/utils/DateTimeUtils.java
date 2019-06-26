package net.lzzy.myradio.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.math.BigDecimal;
/**
 * Created by lzzy_gxy on 2019/4/24.
 * Description:
 */
public class DateTimeUtils {
    public static final SimpleDateFormat DATE_TIME_FORMAT=
            new SimpleDateFormat("yyyy_MM_dd HH:mm:ss", Locale.CANADA);
    public static final SimpleDateFormat DATE_FORMAT=
            new SimpleDateFormat("yyyy_MM_dd", Locale.CANADA);
    public static final SimpleDateFormat DATE_TIME=
            new SimpleDateFormat("yyyMMdd", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_KAY=
            new SimpleDateFormat("yyyyMMdd", Locale.CHINA);


    public static boolean isPlayTime(String startTime, String endTime) throws ParseException {
        String date = DATE_FORMAT.format(new Date());
        long startDate = DATE_TIME_FORMAT.parse(date + " " + startTime).getTime();
        long thisDate = System.currentTimeMillis();
        long endDate = DATE_TIME_FORMAT.parse(date + " " + endTime).getTime();
        return thisDate >= startDate && thisDate < endDate;
    }
        public static String getDay(Date today){
            Calendar c=Calendar.getInstance();
            c.setTime(today);
            return String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        }
    public static boolean isStartTime(String startTime) throws ParseException {
        String date=DATE_FORMAT.format(new Date());
        long startDate=DATE_TIME_FORMAT.parse(date+" "+startTime).getTime();
        long thisDate=System.currentTimeMillis();
        return thisDate>startDate;
    }
    public static Date getTheDayBefor(Date day, int days){
        if (days<=0){
            return day;
        }
        BigDecimal mm=new BigDecimal(1000*60*60*24);
        BigDecimal sub=mm.multiply(BigDecimal.valueOf(days));
        return new Date(day.getTime()-sub.longValue());
    }
}
