package net.lzzy.myradio.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.myradio.utils.AppUtils;


/**
 * Created by lzzy_gxy on 2019/4/11.
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutRes());
        AppUtils.addActivity(this);
        manager= getSupportFragmentManager();
        fragment = manager.findFragmentById(getContainerId());
        if (fragment ==null){
            fragment =createFragment();
            manager.beginTransaction().add(getContainerId(), fragment).commit();
        }
    }

      public Fragment getFragment(){return fragment;}

      protected FragmentManager getManager(){
        return manager;
      }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.remoreActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunningActivity(getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.setStopped(getLocalClassName());
    }

    public abstract int getLayoutRes();

    protected abstract int getContainerId();

    /**
     * 生成托管的Fragment对象
     * @return fragment
     */

    protected abstract Fragment createFragment();

}
