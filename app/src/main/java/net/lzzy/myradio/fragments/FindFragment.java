package net.lzzy.myradio.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lzzy.myradio.R;

/**
 * 电台
 *
 * create an instance of this fragment.
 */
public class FindFragment extends BaseFragment {


    @Override
    protected void populate() {

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
}
