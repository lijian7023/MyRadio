package net.lzzy.myradio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import android.os.Parcelable;
import android.view.Window;
import android.widget.TextView;

import net.lzzy.myradio.R;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.models.RadioCategory;
import net.lzzy.myradio.models.Region;
import net.lzzy.myradio.models.UserCookies;
import net.lzzy.myradio.network.AnalysisJsonService;
import net.lzzy.myradio.network.ApiService;
import net.lzzy.myradio.utils.AbstractStaticHandler;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.myradio.utils.DateTimeUtils;
import net.lzzy.myradio.utils.JsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class SplashActivity extends AppCompatActivity {
    public static final int WHAT_COUNTING = 0;
    public static final int WHAT_EXCEPTION = 1;
    public static final int WHAT_COUNT_DONE = 2;
    public static final int WHAT_GET_QUOTE_OK = 3;
    public static final int WHAT_GET_QUOTE_O = 4;
    public static final String REGIONS = "regions";
    public static final String RADIO_CATEGORIES = "radioCategories";
    public static final String THIS_REGION = "thisRegion";
    public static final int WHAT_GET_LOCA_OK = 10;

    private TextView time;
    private TextView hint;
    private int seconds = 3;
    private SplashHandler handler = new SplashHandler(this);
    private Message message;
    private List<Region> regions = new ArrayList<>();
    private List<RadioCategory> radioCategories = new ArrayList<>();
    private String thisRenion = "";

    private static class SplashHandler extends AbstractStaticHandler<SplashActivity> {
        public SplashHandler(SplashActivity context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg, SplashActivity activity) {
            switch (msg.what) {
                case WHAT_COUNTING:
                    //倒计时中：
                    //1：显示倒计时
                    String display = msg.obj.toString() + "秒";
                    activity.time.setText(display);
                    break;
                case WHAT_COUNT_DONE:
                    //倒计时完成跳转到主页面（带数据put到主页面）
                    activity.gotoMain();
                    break;
                case WHAT_GET_QUOTE_OK:
                    //1:获取到名人名言
                    String quoteJSON = msg.obj.toString();
                    try {
                        //2:显示名人名言
                        activity.hint.setText(JsonUtils.getQuote(quoteJSON));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


                default:
                    break;
            }
        }

    }

    /**
     * 跳转到主页面
     */
    public void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra(REGIONS, (ArrayList<? extends Parcelable>) regions);
        intent.putParcelableArrayListExtra(RADIO_CATEGORIES, (ArrayList<? extends Parcelable>) radioCategories);
        intent.putExtra(THIS_REGION, thisRenion);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        AppUtils.addActivity(this);
        initView();
        ThreadPoolExecutor executor = AppUtils.getExecutor();
        //倒计时线程
        executor.execute(this::countDown);
        //名人名言线程
        executor.execute(this::getQuoteDown);
        //执行获取所有的地区线程
        new GetRegion(this).execute();
        //执行获取所有电台类别线程
        new GetRadioCategory(this).execute();
        //执行获取所在地区线程
        new GetLocation(this).execute();
    }

    /**
     * 获取名人名言
     */
    private void getQuoteDown() {
        try {
            String json = ApiService.okGet(ApiConstants.GET_QUOTE);
            Message message = handler.obtainMessage(WHAT_GET_QUOTE_OK, json);
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(5);
        }

    }

    private void countDown() {
        while (seconds >= 0) {
            handler.sendMessage(handler.obtainMessage(WHAT_COUNTING, seconds));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                handler.sendMessage(handler.obtainMessage(WHAT_EXCEPTION, e.getMessage()));
            }
            seconds--;
        }
        handler.sendEmptyMessage(WHAT_COUNT_DONE);
    }

    private void initView() {
        time = findViewById(R.id.activity_start_tv_time);
        hint = findViewById(R.id.activity_start_tv_hint);
    }

    /**
     * 获取所有地区线程
     * AsyncTask<Void, Void, List<Region>>
     * 1：入参；
     * 2：入参；
     * 3：返回的参数
     */
    static class GetRegion extends AsyncTask<Void, Void, List<Region>> {
        WeakReference<SplashActivity> activity;

        GetRegion(SplashActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        /**
         * 执行线程前
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * 执行时
         *
         * @param voids
         * @return
         */
        @Override
        protected List<Region> doInBackground(Void... voids) {
            try {
                return AnalysisJsonService.getRegion(ApiConstants.GET_REGION);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        /**
         * 获取到数据后
         *
         * @param regionList 获取到的数据
         */
        @Override
        protected void onPostExecute(List<Region> regionList) {
            super.onPostExecute(regionList);
            activity.get().regions.clear();
            activity.get().regions.addAll(regionList);
        }
    }

    /**
     * 获取所有电台类别线程
     */
    static class GetRadioCategory extends AsyncTask<Void, Void, List<RadioCategory>> {
        WeakReference<SplashActivity> activity;

        GetRadioCategory(SplashActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected List<RadioCategory> doInBackground(Void... voids) {
            try {
                return AnalysisJsonService.getRadioCategory(ApiConstants.GET_ALL_RADIO_CATEGORY);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        /**
         * 获取到数据后
         *
         * @param radioCategoryList 获取到的数据
         */
        @Override
        protected void onPostExecute(List<RadioCategory> radioCategoryList) {
            super.onPostExecute(radioCategoryList);
            activity.get().radioCategories.clear();
            activity.get().radioCategories.addAll(radioCategoryList);
        }
    }

    /**
     * 获取所在地区线程
     */
    static class GetLocation extends AsyncTask<Void, Void, String> {
        WeakReference<SplashActivity> activity;

        GetLocation(SplashActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return AnalysisJsonService.getLocation(ApiConstants.GET_THIS_REGION);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            activity.get().thisRenion = s;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserCookies.getInstance().UpdateAppVisitCount(1,
                DateTimeUtils.DATE_FORMAT.format(new Date()));
    }

}

