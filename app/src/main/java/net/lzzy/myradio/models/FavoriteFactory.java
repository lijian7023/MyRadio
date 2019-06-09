package net.lzzy.myradio.models;


import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;

import net.lzzy.myradio.R;
import net.lzzy.myradio.constants.DbConstants;
import net.lzzy.myradio.fragments.BaseFragment;
import net.lzzy.myradio.utils.AppUtils;
import net.lzzy.sqllib.SqlRepository;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFactory  extends BaseFragment {
    private static final FavoriteFactory ourInstance = new FavoriteFactory();
    private SqlRepository<Favorite> repository;
    private GridView gv;
    private Adapter adapter;

    public static FavoriteFactory getInstance() {
        return ourInstance;
    }
    private FavoriteFactory() {
        repository=new SqlRepository<>(AppUtils.getContext(), Favorite.class, DbConstants.packager);

    }

    @Override
    protected void populate() {
        gv= find(R.id.fragment_local_gv);
        View empty=find(R.id.fragment_local_empty);
        gv.setEmptyView(empty);
//        adapter= new GenericAdapter<Radio>(getActivity(),R.layout.fragment_local,) {
//            @Override
//            public void populate(ViewHolder viewHolder, Radio radio) {
//
//            }
//
//            @Override
//            public boolean persistInsert(Radio radio) {
//                return false;
//            }
//
//            @Override
//            public boolean persistDelete(Radio radio) {
//                return false;
//            }
//        }



    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_local;
    }

    @Override
    public void search(String kw) {

    }


    private Favorite getByRadio(String radioId){
        try {
            List<Favorite> favorites= repository.getByKeyword(radioId,
                    new String[]{Favorite.COl_RADIO_ID},true);
            if (favorites.size()>0){
                return favorites.get(0);
            }
        } catch (IllegalAccessException|InstantiationException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDeleteString(String radioId){
        Favorite favorite=getByRadio(radioId);
        return favorite==null?null:repository.getDeleteString(favorite);
    }

    public boolean isRadioStarred(String radioId){
        try {
            List<Favorite> favorites = repository.getByKeyword(radioId,
                    new String[]{Favorite.COl_RADIO_ID}, true);
            return favorites.size()>0;
        } catch (IllegalAccessException | InstantiationException | java.lang.InstantiationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void starRadio(int radioId,String title,int audienceCount,String cover){
        Favorite favorite=getByRadio(String.valueOf(radioId));
        if (favorite==null){
            favorite=new Favorite();
            favorite.setQuestionId(radioId);
            favorite.setCover(cover);
            favorite.setAudienceCount(audienceCount);
            favorite.setTitle(title);
            repository.insert(favorite);
        }
    }

    public void cancelStarRadio(int radioId){
        Favorite favorite=getByRadio(String.valueOf(radioId));
        if (favorite!=null){
            repository.delete(favorite);
        }
    }
    public List<Radio> getFavoriteRadio(){
        List<Favorite>favorites=repository.get();
        List<Radio> radios=new ArrayList<>();
        for (Favorite favorite:favorites){
            Radio radio=new Radio();
            radio.setContentId(favorite.getRadioId());
            radio.setAudienceCount(favorite.getAudienceCount());
            radio.setCover(favorite.getCover());
            radio.setTitle(favorite.getTitle());
            radios.add(radio);
        }


        return radios;
    }
}
