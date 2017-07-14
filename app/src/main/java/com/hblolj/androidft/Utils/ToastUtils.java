package com.hblolj.androidft.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 *
 * Created by hblolj on 2017/2/15.
 */

public class ToastUtils {

    static Toast toast = null;

    public static void show(Context context, String text){
        if (toast != null) {
            toast.setText(text);
        }else{
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
