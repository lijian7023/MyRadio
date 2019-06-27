package net.lzzy.myradio.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;

import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.lzzy.myradio.R;
import net.lzzy.myradio.activities.SplashActivity;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.FavoriteFactory;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.models.RadioCategory;
import net.lzzy.myradio.models.Region;
import net.lzzy.myradio.models.UserCookies;
import net.lzzy.myradio.network.AnalysisJsonService;
import net.lzzy.myradio.network.ApiService;
import net.lzzy.myradio.utils.AbstractStaticHandler;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.myradio.utils.BaseAsyncTack;
import net.lzzy.myradio.utils.MyDialog;
import net.lzzy.myradio.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.JsonConverter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 电台
 *
 * create an instance of this fragment.
 */
public class FindFragment extends BaseFragment {
    public static final String THIS_REGION = "thisRegion";
    public static final String REGIONS = "regions";
    public static final String RADIO_CATEGORIES = "radioCategories";
    private List<Region> regions=new ArrayList<>();
    private List<RadioCategory> radioCategories=new ArrayList<>();
    private String thisRegion="";
    private int thisPag =1;
    private int pag;
    private GenericAdapter<Radio> genericAdapter;
    private List<Radio> radios=new ArrayList<>();
    private static final int WHAT_GET_RADIO_OK = 1;
    private static final int WHAT_GET_RADIO_ERROR = 2;
    private Radio radio;
    private ImageView imageView;
    private TextView tvRegion;
    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
    private Spinner address;
    private int page = 1;
    private GridView grid;
    private List<String> list=new ArrayList<>();
    private int pos=1;
    private String ip;
    private SearchView search;
    private static MyDialog dialog;


    public static FindFragment newInstance(List<Region> regions,List<RadioCategory> radioCategories,
                                           String thisRegion){
        FindFragment fragment=new FindFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(REGIONS, (ArrayList<? extends Parcelable>) regions);
        args.putParcelableArrayList(RADIO_CATEGORIES, (ArrayList<? extends Parcelable>) radioCategories);
        args.putString(THIS_REGION,thisRegion);
        fragment.setArguments(args);
        return fragment;
    }
public static void hideDialog(){dialog.hide();}
    public static void showDialog() {

        if (dialog!=null){
            dialog.show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (getArguments()!=null){
            regions=getArguments().getParcelableArrayList(REGIONS);
            radioCategories=getArguments().getParcelableArrayList(RADIO_CATEGORIES);
            thisRegion= getArguments().getString(THIS_REGION);
        }
    }
    @Override
    protected void populate() {
        search = find(R.id.fragment_find_searchView);
        search.setQueryHint("请输入关键词搜索");
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public void handleQuery(String kw) {
                if ("".equals(kw)) {
                    AppUtils.getExecutor().execute(() -> {
                        try {
                            String json=ApiService.okGet(ApiConstants.getRadio((int) tvRegion.getTag(), 1, 12));
                            handler.sendMessage(handler.obtainMessage(WHAT_GET_RADIO_OK,json));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    new SearchRadioThread<FindFragment>(FindFragment.this) {

                        @Override
                        protected void onPostExecute(List<Radio> radios, FindFragment findFragment) {
                            findFragment.radios.clear();
                            findFragment.radios.addAll(radios);
                            findFragment.genericAdapter.notifyDataSetChanged();
                        }
                    }.execute(kw);
                }
            }
        });
        tvRegion = find(R.id.fragment_find_tv);
        GridView gv=find(R.id.fragment_find_gv);



        View empty=find(R.id.no_network);
        gv.setEmptyView(empty);
        //设置默认地区
        for (int i = 0; i < regions.size(); i++) {
            Region region=regions.get(i);
            if (region.getTitle().equals(thisRegion)){
                regions.remove(region);
                regions.add(0,region);
                tvRegion.setText(thisRegion);
                tvRegion.setTag(region.getId());
                break;
            }
        }
        LinearLayout onRegion=find(R.id.fragment_find_on_region);
        onRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.location);
                builder.setTitle("选择地区");
                final String[] items = new String[regions.size()];
                for (int i = 0; i < regions.size(); i++) {
                    items[i] = regions.get(i).getTitle();
                }
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        page=1;
                        String regionText = items[which];
                       for (Region region:regions) {
                            if (region.getTitle().equals(regionText)) {
                                UserCookies.getInstance().updateReadRegionsCount(String.valueOf(region.getId()),new Date());
                                UserCookies.getInstance().updateReadRegionsName(String.valueOf(region.getId()),regionText);

                                tvRegion.setText(regionText);
                                tvRegion.setTag(region.getId());
                                AppUtils.getExecutor().execute(() -> {
                                    try {
                                        String json=ApiService.okGet(ApiConstants.getRadio(region.getId(), 1, 12));
                                        Message message=handler.obtainMessage(WHAT_GET_RADIO_OK,json);
                                        message.arg1=1;
                                        handler.sendMessage(message);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });
        genericAdapter = new GenericAdapter<Radio>(getContext(),R.layout.fragment_icon,radios) {
            @Override
                public void populate(ViewHolder viewHolder, Radio radio) {
                ImageView imageView = viewHolder.getView(R.id.radio_icon_img);
                Picasso.get().load(radio.getCover())
                        .into(imageView);
                viewHolder.setTextView(R.id.radio_name_tv_title, radio.getTitle());
                long count = radio.getAudienceCount();
                viewHolder.setTextView(R.id.radio_listen_tv, "收听：" + count);
                ImageView imageFavorite = viewHolder.getView(R.id.fragment_icon_collect);
               //设置收藏
                int starId = FavoriteFactory.getInstance().isRadioStarred(String.valueOf(radio.getContentId()))?
                        android.R.drawable.star_big_on :android.R.drawable.star_big_off;
                imageFavorite.setImageResource(starId);
                imageFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean collect = FavoriteFactory.getInstance().isRadioStarred(String.valueOf(radio.getContentId()));
                        if (collect) {
                            FavoriteFactory.getInstance().cancelStarRadio(radio.getContentId());
                            imageFavorite.setImageResource(android.R.drawable.star_big_off);
                        } else {
                            FavoriteFactory.getInstance().starRadio(radio.getContentId(),radio.getTitle(),radio.getAudienceCount(),radio.getCover());
                            imageFavorite.setImageResource(android.R.drawable.star_big_on);
                        }}
                });
                FavoriteFactory favoriteFactory = FavoriteFactory.getInstance();
                if (favoriteFactory.isRadioStarred(String.valueOf(radio.getContentId()))){
                    imageFavorite.setBackgroundResource(android.R.drawable.star_big_on);
                } else {
                    imageFavorite.setBackgroundResource(android.R.drawable.star_big_off);
                }



            }

            @Override
            public boolean persistInsert(Radio radio) {
                /*genericAdapter.add(radio);*/
                return false;
            }

            @Override
            public boolean persistDelete(Radio radio) {
                return false;
            }
        };
        AppUtils.getExecutor().execute(() -> {
            try {
                String json=ApiService.okGet(ApiConstants.getRadio(regions.get(0).getId(), 1, 12));
               Message message=handler.obtainMessage(WHAT_GET_RADIO_OK,json);
               message.arg1=1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gv.setAdapter(genericAdapter);
        //滚动加载电台
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        View v = view.getChildAt(view.getChildCount() - 1);
                        int[] location = new int[2];
                        //获取在整个屏幕内的绝对坐标
                        v.getLocationOnScreen(location);
                        int y = location[1];
                        //拖动加载数据
                        if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y) {
                            getLastVisiblePosition = view.getLastVisiblePosition();
                            lastVisiblePositionY = y;

                            return;
                        }else if (view.getLastVisiblePosition()== getLastVisiblePosition && lastVisiblePositionY ==y)
                        {
                            page++;
                            AppUtils.getExecutor().execute(() -> {
                                try {
                                    String json=ApiService.okGet(ApiConstants.getRadio((Integer) tvRegion.getTag(), page, 12));
                                    Message message=handler.obtainMessage(WHAT_GET_RADIO_OK,json);
                                    message.arg1=0;
                                    handler.sendMessage(message);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                    }
                    //未滚动到底部，第二次拖至底部都初始化
                    getLastVisiblePosition = 0;
                    lastVisiblePositionY = 0;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //region 点击电台进入节目列表
        gv.setOnItemClickListener((parent, view, position, id) -> {
            AppUtils.setRadio(radios.get(position));
            UserCookies.getInstance().updateRadioName(String.valueOf
                    (radios.get(position).getContentId()),radios.get(position).getTitle());
            UserCookies.getInstance().updateRadio(String.valueOf
                    (radios.get(position).getContentId()),new Date());
            ViewUtils.showPrograms(getContext(),tvRegion.getText().toString()
                    ,radios.get(position).getTitle(),new ArrayList<>());
            new BaseAsyncTack(){
                @Override
                protected void abstractOnPostExecute(List<PlayList> radioPrograms) {
                    ViewUtils.updateRadioProgramsAdapter(radioPrograms);
                }
            }.execute(radios.get(position).getContentId());
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_find;
    }

    @Override
    public void search(String kw) {

    }

    public void updateFavorite() {
        AppUtils.getExecutor().execute(() -> {
            try {
                String json=ApiService.okGet(ApiConstants.getRadio((Integer) tvRegion.getTag(), 1, 12));
                Message message=handler.obtainMessage(WHAT_GET_RADIO_OK,json);
                message.arg1=1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public interface OnFragmentInteractionListener {
    }
    /**
     * 获取当前地区电台线程
     * AsyncTask<Integer, Void, List<Region>>
     * 1：入参；地区id
     * 2：入参；
     * 3：返回的参数
     */
    abstract static class GetRadio<T> extends AsyncTask<Void,Void,List<Radio>> {
        WeakReference<T> context;
        int regionId,page,pagesize;
        protected GetRadio(T context,int regionId,int page,int pagesize){
            this.context = new WeakReference<>(context);
            this.regionId=regionId;
            this.page=page;
            this.pagesize=pagesize;
        }
        /**
         * 执行线程前
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Radio> doInBackground(Void... voids) {
            try {
                String json=ApiService.okGet(ApiConstants.getRadio(regionId,page,page));
                JSONObject object=new JSONObject(json);
                String jsonArray=object.getJSONObject("Data").getString("items");

                JsonConverter<Radio> converter=new JsonConverter<>(Radio.class);
                List<Radio> radios = new ArrayList<>(converter.getArray(jsonArray));

                return radios;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        /**
         * 获取到数据后
         *
         */
        @Override
        protected void onPostExecute(List<Radio> radios) {
            super.onPostExecute(radios);
            T t = context.get();
            onPostExecute(radios,t);
        }
        protected abstract void onPostExecute(List<Radio> radios, T t);
    }
    private FindHandler handler = new FindHandler(this);
    private static class FindHandler extends AbstractStaticHandler<FindFragment> {
        public FindHandler(FindFragment context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg, FindFragment fragment) {
            switch (msg.what){
                case WHAT_GET_RADIO_OK:
                    String json=msg.obj.toString() + "秒";
                    JSONObject object= null;
                    try {
                        object = new JSONObject(json);
                        String jsonArray=object.getJSONObject("Data").getString("items");
                        JsonConverter<Radio> converter=new JsonConverter<>(Radio.class);
                        List<Radio> radios = new ArrayList<>(converter.getArray(jsonArray));
                        if (msg.arg1==1){
                            fragment.radios.clear();
                        }
                        fragment.radios.addAll(radios);
                        fragment.genericAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                case WHAT_GET_RADIO_ERROR:

                    break;
                default:
                    break;
            }
        }
    }
    abstract class SearchRadioThread<T> extends AsyncTask<String,Void,List<Radio>> {
         WeakReference<T> context;
        protected SearchRadioThread(T context){
            this.context = new WeakReference<>(context);
        }
        /**
         * 执行线程前
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Radio> doInBackground(String... strings) {
            try {
                return AnalysisJsonService.getSearchRadio(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        /**
         * 获取到数据后
         *
         */
        @Override
        protected void onPostExecute(List<Radio> radios) {
            super.onPostExecute(radios);
            T t = context.get();
            onPostExecute(radios,t);
        }
        protected abstract void onPostExecute(List<Radio> radios, T t);
    }

}
