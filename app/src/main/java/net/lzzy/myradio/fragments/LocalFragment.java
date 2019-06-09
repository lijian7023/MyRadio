package net.lzzy.myradio.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.lzzy.myradio.R;
import net.lzzy.myradio.constants.ApiConstants;
import net.lzzy.myradio.models.Favorite;
import net.lzzy.myradio.models.FavoriteFactory;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.network.ApiService;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Radio> radios=FavoriteFactory.getInstance().getFavoriteRadio();
    private GridView gv;
    private GenericAdapter<Radio> adapter;


    private String mParam1;
    private String mParam2;






    private OnFragmentInteractionListener mListener;

    public LocalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected void populate() {
        gv = find(R.id.fragment_local_gv);
        View empty = find(R.id.fragment_local_empty);
        gv.setEmptyView(empty);

        //GenericAdapter<Favorite> adapter = new GenericAdapter<Favorite>(getActivity(), R.layout.fragment_icon,radios) {
        //    @Override
        //    public void populate(ViewHolder viewHolder, Favorite favorite) {

        //    }

         //   @Override
         //   public boolean persistInsert(Favorite favorite) {
         //       return false;
         //   }

          //  @Override
          //  public boolean persistDelete(Favorite favorite) {
           //     return false;
          //         }
       // };
        adapter = new GenericAdapter<Radio>(getContext(),R.layout.fragment_icon,radios) {
            @Override
            public void populate(ViewHolder viewHolder, Radio radio) {
                ImageView imageView=viewHolder.getView(R.id.radio_icon_img);
                Picasso.get().load(radio.getCover())
                        .into(imageView);
                viewHolder.setTextView(R.id.radio_name_tv_title,radio.getTitle());
                viewHolder.setTextView(R.id.radio_listen_tv,"收听:"+(radio.getAudienceCount()));
                ImageView img= viewHolder.getView(R.id.fragment_icon_collect);
                int starId=FavoriteFactory.getInstance().isRadioStarred(String.valueOf(radio.getContentId()))?
                        android.R.drawable.star_big_on:android.R.drawable.star_big_off;
                img.setImageResource(starId);
                boolean is=FavoriteFactory.getInstance().isRadioStarred(String.valueOf(radio.getContentId()));
                /*if (is){
                    img.setImageResource(android.R.drawable.star_big_on);

                }else {
                    img.setImageResource(android.R.drawable.star_big_off);
                }*/
                img.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        if (is) {
                            FavoriteFactory.getInstance().cancelStarRadio(radio.getContentId());
                        } else {
                            FavoriteFactory.getInstance().starRadio(radio.getContentId(),
                                    radio.getTitle(), radio.getContentId(), radio.getCover());
                        }
                        updateFavorite();
                    }

                });
            }

            @Override
            public boolean persistInsert(Radio radio) {
                return false;
            }

            @Override
            public boolean persistDelete(Radio radio) {
                return false;
            }

        };
        gv.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_local;
    }

    @Override
    public void search(String kw) {

    }

    public void onButtonPressed (Uri uri){
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateFavorite() {
        radios.clear();
        radios.addAll(FavoriteFactory.getInstance().getFavoriteRadio());
        adapter.notifyDataSetChanged();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
