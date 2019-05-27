package net.lzzy.myradio.constants;


import net.lzzy.myradio.R;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.sqllib.DbPackager;

/**
 * Created by lzzy_gxy on 2019/4/17.
 * Description:
 */
public class DbConstants {
    private DbConstants(){
        
    }
    private static final String DB_NAME="practices.db";
    private static final int DB_VERSION=1;
    public  static DbPackager packager=DbPackager
            .getInstance(AppUtils.getContext(),DB_NAME,DB_VERSION, R.raw.models);
}
