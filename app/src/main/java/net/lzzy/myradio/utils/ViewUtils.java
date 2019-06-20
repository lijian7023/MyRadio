package net.lzzy.myradio.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import net.lzzy.myradio.R;
import net.lzzy.myradio.fragments.PlayFragment;
import net.lzzy.myradio.models.Broadcaster;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.models.Radio;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;


/**
 * Created by lzzy_gxy on 2019/4/15.
 * Description:
 */
public  class ViewUtils {
    private static TextView tvBack;
    private static LinearLayout layout;
    private static TextView tvTitle;
    private static ListView lv;
    private static List<PlayList> radioPrograms;
    private static GenericAdapter<PlayList> adapter;
    private static RadioProgramsDialog dialog;
    private static boolean is=false;
    public static int px2dp(int pxValue,Context context){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }
    public static int dp2px(int dpValue,Context context){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale+0.5f);
    }

    public static abstract class AbstractQueryHandler implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            handleQuery(newText);
            return true;
        }

        /**
         * handle query logic
         *
         * @param kw keyword
         * @return end query
         */
        public abstract void handleQuery(String kw);
    }

    public static abstract class AbstractTouchHandler implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return handleTouch(event);
        }

        /**
         * 处理触摸事件
         *
         * @param event 触摸事件对象
         * @return 消费触摸事件吗
         */
        public abstract boolean handleTouch(MotionEvent event);
    }
    public abstract static class AbstractQueryListener implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            handleQuery(newText);
            return true;
        }

        public abstract void handleQuery(String kw);
    }

    public static void showPrograms(Context context, String back, String title, List<PlayList> radioProgramsList){
        dialog = new RadioProgramsDialog(context);
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_play,null);

        tvTitle = view.findViewById(R.id.dialog_radio_programs_tv_title);
        tvBack = view.findViewById(R.id.dialog_radio_programs_tv_region);
        tvBack.setText(back);
        tvTitle.setText(title);
        //返回
        layout = view.findViewById(R.id.dialog_radio_programs_layout);
        layout.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
                dialog =null;
            }
        });
        //列表视图显示
        radioPrograms = radioProgramsList;
        lv = view.findViewById(R.id.fragment_program_lv);
        adapter= new GenericAdapter<PlayList>(context,R.layout.fragment_program,radioPrograms) {


            @Override
            public boolean persistInsert(PlayList playList) {
                return false;
            }

            @Override
            public boolean persistDelete(PlayList playList) {
                return false;
            }

            @Override
            public void populate(ViewHolder viewHolder, PlayList radioPrograms) {
                viewHolder.setTextView(R.id.fragment_program_items_count,"0");
                if (radioPrograms.isPlay()) {
                    is=true;
                    viewHolder.setTextView(R.id.dialog_radio_programs_tv_title,"直播中...【"+radioPrograms.getTitle()+"】");

                    TextView tvTitle = viewHolder.getView(R.id.dialog_radio_programs_tv_title);
                    tvTitle.setTextColor(Color.parseColor("#1afa29"));
                }else if (is){
                    is=false;
                    viewHolder.setTextView(R.id.dialog_radio_programs_tv_title,"稍后直播...【"+radioPrograms.getTitle()+"】");

                    TextView tvTitle = viewHolder.getView(R.id.dialog_radio_programs_tv_title);
                    tvTitle.setTextColor(Color.parseColor("#000000"));
                }else {
                    viewHolder.setTextView(R.id.dialog_radio_programs_tv_title,"【"+radioPrograms.getTitle()+"】");
                    TextView tvTitle = viewHolder.getView(R.id.dialog_radio_programs_tv_title);
                    tvTitle.setTextColor(Color.parseColor("#000000"));
                }
                String broadcasters = "";
                for (Broadcaster broadcaster : radioPrograms.getBroadcasters()) {
                    broadcasters = broadcasters.concat(broadcaster.getUsername()).concat("、");
                }
                viewHolder.setTextView(R.id.fragment_program_anchor_name,
                        broadcasters.length() > 0 ? broadcasters.substring(0, broadcasters.length() - 1) : "");
                viewHolder.setTextView(R.id.fragment_program_broadcast_time,
                        "【"+radioPrograms.getStartTime() + "-" + radioPrograms.getEndTime()+"】");
            }


        };
        lv.setAdapter(adapter);
        dialog.setContentView(view);
        dialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new PlayFragment();
            }
        });
    }

    public static void updateRadioProgramsAdapter(List<PlayList> radioPrograms1) {
        if (dialog != null && dialog.isShowing() && adapter != null) {
            radioPrograms.clear();
            radioPrograms.addAll(radioPrograms1);
            adapter.notifyDataSetChanged();
        }
    }}

