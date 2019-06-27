package net.lzzy.myradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import net.lzzy.myradio.R;
import net.lzzy.myradio.activities.CircleImageView;
import net.lzzy.myradio.activities.MainActivity;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.Broadcaster;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.myradio.utils.DateTimeUtils;
import net.lzzy.myradio.utils.ViewUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.provider.MediaStore.MediaColumns.TITLE;

/**
 * 播放器Fragment
 */
public class PlayerFragment extends BaseFragment {
    public static final String RADIO_PROGRAMS = "radioPrograms";
    public static final String TIME = "time";
    public static final String TITLE = "title";
    public static final String RADIO = "radio";
    public static final String REGIONS = "regions";
    private IjkMediaPlayer player;
    private CircleImageView imgRadio;
    private TextView tvRadio;
    private TextView tvStart;
    private SeekBar bar;
    private TextView tvEnd;
    private ImageView imgPrevious;
    private ImageView imgPlay;
    private ImageView imgNext;
    private List<PlayList> radioPrograms;
    private String[] urls;
    private String[] urlsTitle;
    private String[] urlsBroadcaster;
    private int position=0;
    private String playUrl;
    private SeekBarHandler handler = new SeekBarHandler();
    private LinearLayout layout;
    private TextView tvRegion;
    private String title;
    private Radio radio;
    private int time;
    private String broadcaster;
    private TextView tvBroadcaster;



//    private String[] urls = {"http://lcache.qingting.fm/cache/20190531/4875/4875_20190531_000000_010000_24_0.aac",
//            "http://lcache.qingting.fm/cache/20190531/4875/4875_20190531_060000_073000_24_0.aac",

    /**
     * GridFragment静态工厂类（创建一个GridFragment）
     *
     * @param
     * @return GridFragment
     */
    public static PlayerFragment newInstance(int time, String title, Radio radio, List<PlayList> radioPrograms){
        PlayerFragment fragment=new PlayerFragment();
        Bundle args= new Bundle();
        args.putInt(TIME,time);
        args.putString(TITLE,title);
        args.putParcelable(RADIO,radio);
        args.putParcelableArrayList(RADIO_PROGRAMS, (ArrayList<? extends Parcelable>) radioPrograms);
        fragment.setArguments(args);
        return fragment;
    }

    private class SeekBarHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if (player != null){
                long pos = player.getCurrentPosition();
                setSeekBar(pos);
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            time=getArguments().getInt(TIME);
            title=getArguments().getString(TITLE);
            radio=getArguments().getParcelable(RADIO);
            radioPrograms=getArguments().getParcelableArrayList(RADIO_PROGRAMS);
        }

    }
    @Override
    protected void populate() {
        player = AppUtils.getPlayer();
        intiView();
        getPrograms();
        configPlayer();
        playMusic();


    }
    private void setSeekBar(long position){
        bar.setProgress((int) position);
        String pastTime = formatPlayerTime((int) position);
        tvStart.setText(pastTime);
    }

    private void getPrograms() {
        List<String> programsList=new ArrayList<>();
        List<String> titleList=new ArrayList<>();
        List<String> broadcasterList=new ArrayList<>();
        for (PlayList programs:radioPrograms){
            String title=programs.getTitle();
            List<Broadcaster> broadcasters=programs.getBroadcasters();
            String userName;
            if (broadcasters.size()>0){
                for (Broadcaster broadcaster:broadcasters){
                    userName= "主播:"+"hhhhhhh";
                    broadcasterList.add(userName);
                }
            }else {
                userName="主播:未知";
                broadcasterList.add(userName);
            }
            titleList.add(title);
            String startTime=programs.getStartTime();
            String endTime=programs.getEndTime();
            int channelId= programs.getChannelId();
            String programAddress;
            try {
                if (DateTimeUtils.isPlayTime(startTime,endTime)){
                    programAddress=ApiConstants.getPlayimjUrl(channelId);
                    programsList.add(programAddress);
                }else{
                    programAddress= ApiConstants.getRadioProgramsPlay(channelId,startTime,endTime);
                    programsList.add(programAddress);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        tvRegion.setText(radio.getTitle());
        Picasso.get().load(radio.getCover())
                .into(imgRadio);
        urlsTitle=get(titleList.size(),titleList);
        title=urlsTitle[time];
        tvRadio.setText(title);
        urlsBroadcaster=get(broadcasterList.size(),broadcasterList);
        broadcaster=urlsBroadcaster[time];
        tvBroadcaster.setText(broadcaster);
        urls=get(programsList.size(),programsList);
        playUrl=urls[time];

    }
    private String[] get(int size, List<String> title) {
        String[] s = new String[size];
        s = title.toArray(s);
        return s;
    }



    private void configPlayer() {
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = playOrPause() ? android.R.drawable.ic_media_play
                        : android.R.drawable.ic_media_pause;
                imgPlay.setImageResource(res);
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMusic(false);
            }
        });
        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMusic(false);
            }
        });
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && player != null) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void switchMusic(boolean backward) {
        if (backward) {
            if (position > 0) {
                position--;
            } else {
                Toast.makeText(getContext(), "no previous music!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (position < urls.length - 1) {
                position++;
            } else {
                Toast.makeText(getContext(), "it's already the last music", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        playUrl = urls[position];
        playMusic();
        title=urlsTitle[position];
        tvRadio.setText(title);
        broadcaster=urlsBroadcaster[position];
        tvBroadcaster.setText(broadcaster);

    }
    private void playMusic() {
        try {
            player.reset();
            player.setDataSource(playUrl);
            player.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    switchMusic(false);
                }
            });
            player.prepareAsync();
            player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer p) {
                    Toast.makeText(getContext(), "playing", Toast.LENGTH_SHORT).show();
                    p.start();
                    int max = (int) p.getDuration();
                    if (max > 0) {
                        bar.setMax(max);
                    }
                    String allTime = formatPlayerTime(max);
                    tvEnd.setText(allTime);
                    handler.sendEmptyMessage(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void intiView() {
        layout = find(R.id.fragment_play_layout);
        tvRegion = find(R.id.fragment_play_tv_region);
        imgRadio = find(R.id.fragment_play_img_radio);
        tvRadio = find(R.id.fragment_play_tv_radio);
        tvBroadcaster = find(R.id.fragment_play_tv_broadcaster);
        tvStart = find(R.id.fragment_play_tv_start);
        bar = find(R.id.fragment_play_bar);
        tvEnd = find(R.id.fragment_play_tv_end);
        imgPrevious = find(R.id.fragment_play_img_previous);
        imgPlay = find(R.id.fragment_play_img_play);
        imgNext = find(R.id.fragment_play_img_next);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewUtils.isShow()) {
                    ViewUtils.getshow();
                }
                MainActivity.showPlayImage();
                MainActivity.hinePlay();
            }
        });

    }

    private String formatPlayerTime(int max) {
        max /= 1000;
        int min = max / 60;
        int seconds = max % 60;
        return String.format(Locale.CHINA, "%d:%02d", min, seconds);
    }

    private boolean playOrPause() {
        if (player.isPlaying()) {
            player.pause();
            return true;
        } else {
            player.start();
            return false;
        }
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_music;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


}
