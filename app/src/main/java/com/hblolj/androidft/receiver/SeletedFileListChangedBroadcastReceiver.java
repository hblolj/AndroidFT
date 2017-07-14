package com.hblolj.androidft.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hblolj on 2017/2/17.
 */

public abstract class SeletedFileListChangedBroadcastReceiver extends BroadcastReceiver {

    /**
     * 更新选择文件列表的Action
     */
    public static final String ACTION_CHOOSE_FILE_LIST_CHANGED = "ACTION_CHOOSE_FILE_LIST_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_CHOOSE_FILE_LIST_CHANGED)){
            onSelectedFileListChanged();
        }
    }

    public abstract void onSelectedFileListChanged();
}
