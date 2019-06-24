package net.lzzy.myradio.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import net.lzzy.myradio.R;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

/**
 * @author 菜鸡
 */
public class MyDialog extends AlertDialog {

        Context mContext;


    public MyDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.mContext=context;
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void show() {
            super.show();
            /**
             * 设置宽度全屏，要设置在show的后面
             */
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags=FLAG_FULLSCREEN;
            layoutParams.flags=FLAG_LAYOUT_IN_SCREEN;
            layoutParams.gravity= Gravity.BOTTOM;
            layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height= WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().getDecorView().setPadding(0, 40, 0, 0);
            getWindow().setAttributes(layoutParams);



        }
    }