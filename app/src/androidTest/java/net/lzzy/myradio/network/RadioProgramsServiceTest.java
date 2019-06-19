package net.lzzy.myradio.network;

import net.lzzy.myradio.models.PlayList;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lzzy_gxy on 2019/6/19.
 * Description:
 */
public class RadioProgramsServiceTest {

    @Test
    public void getRadioPrograms() throws IOException, InstantiationException, JSONException, IllegalAccessException {
        List<PlayList> regions=RadioProgramsService.getRadioPrograms(4875,"5");
        assertEquals(14,regions.size());
        assertEquals("00:00:00",regions.get(0).getStartTime());
    }
}