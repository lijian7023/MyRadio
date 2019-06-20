package net.lzzy.myradio.utils;

import android.os.AsyncTask;


import net.lzzy.myradio.models.PlayList;
import net.lzzy.myradio.network.RadioProgramsService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author SUPERHUMAN GIFTS
 */
public abstract class BaseAsyncTack extends AsyncTask<Integer,Void, List<PlayList>> {
    String day="1";
    protected BaseAsyncTack(){

    }

    @Override
    protected List<PlayList> doInBackground(Integer... integers) {
        day=DateTimeUtils.getDay(new Date());
        try {
            return RadioProgramsService.getRadioPrograms(integers[0],day);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    protected void onPostExecute(List<PlayList> radioPrograms) {
        super.onPostExecute(radioPrograms);
        abstractOnPostExecute(radioPrograms);
    }

    protected abstract void abstractOnPostExecute(List<PlayList> radioPrograms);
}
