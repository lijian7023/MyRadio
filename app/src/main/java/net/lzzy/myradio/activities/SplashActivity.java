package net.lzzy.myradio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;

import android.view.Window;
import android.widget.TextView;

import net.lzzy.myradio.R;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.network.ApiService;
import net.lzzy.myradio.utils.AbstractStaticHandler;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.myradio.utils.JsonUtil;

import org.json.JSONException;

import java.io.IOException;
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
    }

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

}
