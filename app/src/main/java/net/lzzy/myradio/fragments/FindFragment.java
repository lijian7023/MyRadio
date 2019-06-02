package net.lzzy.myradio.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.lzzy.myradio.R;
import net.lzzy.myradio.activities.SplashActivity;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.models.RadioCategory;
import net.lzzy.myradio.models.Region;
import net.lzzy.myradio.network.AnalysisJsonService;
import net.lzzy.myradio.network.ApiService;
import net.lzzy.myradio.utils.AbstractStaticHandler;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.JsonConverter;
import net.lzzy.sqllib.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
    private GenericAdapter<Radio> genericAdapter;
    private List<Radio> radios=new ArrayList<>();
    private static final int WHAT_GET_RADIO_OK = 1;
    private static final int WHAT_GET_RADIO_ERROR = 2;

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
        TextView tvRegion=find(R.id.fragment_find_tv);
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
                        String regionText = items[which];
                        for (Region region:regions) {
                            if (region.getTitle().equals(regionText)) {
                                tvRegion.setText(regionText);
                                tvRegion.setTag(region.getId());
//                                new GetThisRegionRadioThread<FindFragment>(FindFragment.this,
//                                        region.getId(), 1, 12) {
//                                    @Override
//                                    protected void onPostExecute(List<Radio> radios, FindFragment fmFragment) {
//                                        fmFragment.thisRadioSize =radios.size();
//                                        fmFragment.gvAdaper.addAll(radios);
//                                    }
//                                };
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
                handler.sendMessage(handler.obtainMessage(WHAT_GET_RADIO_OK,json));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /*new GetRadio<FindFragment>(this,regions.get(0).getId(), 1, 12) {
            @Override
            protected void onPostExecute(List<Radio> radios, FindFragment findFragment) {
                findFragment.radios.clear();
                findFragment.radios.addAll(radios);
                findFragment.genericAdapter.notifyDataSetChanged();
            }
        };*/
       gv.setAdapter(genericAdapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_find;
    }

    @Override
    public void search(String kw) {

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
}
