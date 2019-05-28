package net.lzzy.myradio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import android.view.Window;
import android.widget.TextView;

import net.lzzy.myradio.R;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.RadioCategory;
import net.lzzy.myradio.models.Region;
import net.lzzy.myradio.network.AnalysisJsonService;
import net.lzzy.myradio.network.ApiService;
import net.lzzy.myradio.utils.AbstractStaticHandler;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.myradio.utils.JsonUtil;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class SplashActivity extends AppCompatActivity {
    public static final int WHAT_COUNTING = 0;
    public static final int WHAT_EXCEPTION = 1;
    public static final int WHAT_COUNT_DONE= 2;
    public static final int WHAT_GET_QUOTE_OK=3;

    private TextView time;
    private TextView hint;
    private int seconds = 3;
    private SplashHandler handler = new SplashHandler(this);
    private Message message;
    private  List<Region> regions=new ArrayList<>();
    private List<RadioCategory> radioCategories=new ArrayList<>();

    private static class SplashHandler extends AbstractStaticHandler<SplashActivity> {
        public SplashHandler(SplashActivity context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg, SplashActivity activity) {
            switch (msg.what){
                case WHAT_COUNTING:
                    String display = msg.obj.toString() + "秒";
                    activity.time.setText(display);
                    break;
                case WHAT_COUNT_DONE:
                    break;
                case WHAT_GET_QUOTE_OK:
                    String quoteJSON = msg.obj.toString();
                    try {
                        activity.hint.setText(JsonUtil.getQuote(quoteJSON));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initView();
        ThreadPoolExecutor executor = AppUtils.getExecutor();
        executor.execute(this::countDown);
        executor.execute(this::getQuoteDown);

        //执行获取所有的地区线程
        new GetRegion(this).execute();
        //执行获取所有电台类别线程
        new GetRadioCategory(this).execute();
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
        while (seconds >= 0 ){
            handler.sendMessage(handler.obtainMessage(WHAT_COUNTING,seconds));
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                handler.sendMessage(handler.obtainMessage(WHAT_EXCEPTION,e.getMessage()));
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
     *  AsyncTask<Void, Void, List<Region>>
     *      1：入参；
     *      2：入参；
     *      3：返回的参数
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
         * @param regionList 获取到的数据
         */
        @Override
        protected void onPostExecute(List<Region> regionList) {
            super.onPostExecute(regionList);
            activity.get().regions .clear();
            activity.get().regions.addAll(regionList);
        }
    }

    /**
     * 获取所有电台类别线程
     */
    static class GetRadioCategory extends AsyncTask<Void,Void,List<RadioCategory>> {
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
         * @param radioCategoryList 获取到的数据
         */
        @Override
        protected void onPostExecute(List<RadioCategory> radioCategoryList) {
            super.onPostExecute(radioCategoryList);
            activity.get().radioCategories .clear();
            activity.get().radioCategories.addAll(radioCategoryList);
        }
    }
}
