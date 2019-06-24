package net.lzzy.myradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import net.lzzy.myradio.R;
import net.lzzy.myradio.models.Broadcaster;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.utils.AppUtils;

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
    public static final String QUESTION_RESULT = "questionResult";
    public static final String RADIO_HINT = "radioHint";
    public static final String ARG_PLAYLISTS = "ARG_PLAYLISTS";
    public static final String ARG_URLS = "ARG_URLS";
    public static final String PLAY_POSITION = "PLAY_POSITION";
    private List<PlayList> playLists;
    public static final String SSS = "sss";
    public static final String TITLE = "title";
    public static final String PROGRAM_LISTS = "programLists";
    public static final String COVER = "cover";
    private IjkMediaPlayer player;
    private TextView tvStart;
    private TextView tvEnd;
    private SeekBar bar;
    private ImageView imgPrevious;
    private ImageView imgPlay;
    private ImageView imgNext;
    private List<String> address;
    private int position;
    private Radio radioFavorite;
    private List<String> urls;

    private String playUrl=null;
    private List<PlayList> questionResults;
    private String cover;
    private String[] urls1;
    private String titleUrl;
    private TextView tvTitle;
    private TextView tvRadioTitle;
    private String radioTitle;
    private TextView tvAnchor;
    private String[] anchor1;
    private String anchorUrl;
    private ImageView imageView;
    private ImageView fan;
    private static boolean b;

    private FindFragment.OnFragmentInteractionListener mListener;
    private OnRemovePlayListener listener;


//    private String[] urls = {"http://lcache.qingting.fm/cache/20190531/4875/4875_20190531_000000_010000_24_0.aac",
//            "http://lcache.qingting.fm/cache/20190531/4875/4875_20190531_060000_073000_24_0.aac",

    /**
     * GridFragment静态工厂类（创建一个GridFragment）
     *
     * @param
     * @return GridFragment
     */
    public static PlayerFragment newInstance(int item, String radioTitle, String cover,List<PlayList> questionResults) {
        //寄存器（用来暂存数据）
        Bundle arge = new Bundle();
        //创建一个GridFragment对象
        PlayerFragment playerFragment = new PlayerFragment();
        //往寄存器存数据
        arge.putParcelableArrayList(QUESTION_RESULT, (ArrayList<? extends Parcelable>) questionResults);
        arge.putInt(SSS, item);
        arge.putString(TITLE, radioTitle);
        arge.putString(COVER, cover);
        //将储存器保存到当前GridFragment对象
        playerFragment.setArguments(arge);
        return playerFragment;
    }


    private SeekBarHandler handler = new SeekBarHandler();


    private class SeekBarHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (player != null){
                long pos = player.getCurrentPosition();
                setSeekBar(pos);
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    }
    //销毁
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            questionResults=getArguments().getParcelable(QUESTION_RESULT);
            position = getArguments().getInt(SSS);
            playLists = getArguments().getParcelableArrayList(PROGRAM_LISTS);
            radioTitle = getArguments().getString(TITLE);
            cover = getArguments().getString(COVER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        player = AppUtils.getPlayer();
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        initViews(view);
        configPlayer();
        playMusic();
        return view;
    }
    private void setSeekBar(long position){
        bar.setProgress((int) position);
        String pastTime = formatPlayerTime((int) position);
        tvStart.setText(pastTime);
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
                switchMusic(true);
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
    private void switchMusic(boolean backward) {
        if (backward) {
            if (position > 0) {
                position--;
            } else {
                Toast.makeText(getContext(), "no previous music!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (position < urls.size() - 1) {
                position++;
            } else {
                Toast.makeText(getContext(), "这已经是最后的音乐", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        playUrl = urls1[position];


        playMusic();
        titleUrl = urls1[position];
        anchorUrl = anchor1[position];
        tvTitle.setText(titleUrl);
        tvAnchor.setText(anchorUrl);
    }

    public static void exit(boolean back) {
        if (back) {
            back = true;
        } else {
            back = false;
        }
    }

    private void initViews(View view) {
        tvStart = view.findViewById(R.id.tv_start);
        tvEnd = view.findViewById(R.id.tv_end);
        bar = view.findViewById(R.id.bar);
        imgPrevious = view.findViewById(R.id.img_previous);
        imgPlay = view.findViewById(R.id.img_play);
        imgNext = view.findViewById(R.id.img_next);
    }

    @Override
    protected void populate() {
        player = AppUtils.getPlayer();
        initViews();
        getData();
        configPlayer();
        playMusic();


    }

    private void initViews() {

        tvStart = find(R.id.tv_start);
        tvEnd = find(R.id.tv_end);
        bar = find(R.id.bar);
        tvRadioTitle = find(R.id.fragment_play_title);
        tvRadioTitle.setText(radioTitle);
        fan = find(R.id.fragment_play_fan);
        fan.setOnClickListener(view -> {
            if (b) {
                listener.onRemovePlay();
                FindFragment.showDialog();
            } else {
                listener.onRemovePlay();
                FindFragment.hideDialog();
            }
        });
        imgPrevious = find(R.id.img_previous);
        imgPlay = find(R.id.img_play);
        imgNext = find(R.id.img_next);
        tvTitle = find(R.id.fragment_play_tv_title);
        tvAnchor = find(R.id.fragment_play_tv_zhobo);
        imageView = find(R.id.fragment_play_iv);
        if (cover != null) {
            Picasso.get().load(cover).into(imageView);
        } else {
            Picasso.get().load("https://sss.qingting.fm/neo/default_album.png").into(imageView);
        }
    }

    private void getData() {
        address=new ArrayList<>();
        List<String> title = new ArrayList<>();
        List<String> anchor= new ArrayList<>();
        for (PlayList playList : playLists){
            String titl= playList.getTitle();
            String anchors;
            List<Broadcaster> broadcasters = playList.getBroadcasters();
            if (broadcasters.size()>0){
                for (  Broadcaster broadcaster :broadcasters){
                    anchors="主播"+broadcaster.getUsername();
                    anchor.add(anchors);
                    
                }
            }else {
                anchors="主播：未知";
                anchor.add(anchors);

            }
            title.add(titl);
            String data = "yyyyMMdd";
            String channelId = String.valueOf(playList.getChannelId());
            String start = playList.getStartTime();
            String end = playList.getEndTime();
            String newTime = "HH:mm:ss";
            String time1 = getPathTime(start, newTime);
            String time2 = getPathTime(end, newTime);
            int thisTime, startTime, endTime = 0;
            thisTime = Integer.valueOf(getNewTime("HHmmss"));
            startTime = Integer.valueOf(time1);
            endTime = Integer.valueOf(time2);
            String s;
            if (thisTime >= startTime && thisTime <= endTime) {
                s = "http://lhttp.qingting.fm/live/" + channelId + "/64k.mp3";
                address.add(s);
            } else if (thisTime < startTime) {
            } else {
                s = "http://lcache.qingting.fm/cache/" + getNewTime(data).concat("/").concat(channelId).concat("/")
                        .concat(channelId).concat("_").concat(getNewTime(data)).concat("_").concat(time1).concat("_").concat(time2)
                        .concat("_24_0.aac");
                address.add(s);
            }
        }
        urls1 = get(title.size(), title);
        urls = Arrays.asList(get(address.size(), address));
        anchor1 = get(anchor.size(), anchor);
        playUrl = urls.get(position);
        titleUrl = urls1[position];
        anchorUrl = anchor1[position];
        tvTitle.setText(titleUrl);
        tvAnchor.setText(anchorUrl);
    }

    private String[] get(int size, List<String> title) {
        String[] s = new String[size];
        s = title.toArray(s);
        return s;
    }

        private String getNewTime(String g) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(g);
            String dateString = simpleDateFormat.format(new Date());
            return dateString;
        }


    private String getPathTime(String g, String newTime) {
        SimpleDateFormat formatter1 = new SimpleDateFormat(g);
        String a = null;
        try {
            Date date1 = formatter1.parse(newTime);
            a = formatter1.format(date1).replace(":", "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a;
    }







    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_music;
    }

    @Override
    public void search(String kw) {

    }
    public interface OnRemovePlayListener {
        /**
         * 点击题目跳转
         *
         * @param
         */
        void onRemovePlay();
    }
}
