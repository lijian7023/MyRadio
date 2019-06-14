package net.lzzy.myradio.fragments;

import android.os.AsyncTask;

import net.lzzy.myradio.R;
import net.lzzy.myradio.activities.MainActivity;
import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.network.AnalysisJsonService;
import net.lzzy.myradio.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/6/14.
 * Description:
 */
public class PlayFragment extends BaseFragment {
    @Override
    protected void populate() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_play;
    }

    @Override
    public void search(String kw) {

    }
    /**
     * 获取节目线程
     */
    static abstract class GetRadioPlayList extends AsyncTask<Integer, Void, List<PlayList>> {

        String day="1";
        protected  GetRadioPlayList(){}
        @Override
        protected List<PlayList> doInBackground(Integer... integers) {
            day= DateTimeUtils.getDay(new Date());
            try {
                return AnalysisJsonService.getPlayList(integers[0],day);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }

        }

        @Override
        protected void onPostExecute(List<PlayList> playLists) {
            super.onPostExecute(playLists);
            abstractPostExecute(playLists);
        }

        protected abstract void abstractPostExecute(List<PlayList> playLists);


    }


}
