package com.hblolj.androidft.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hblolj.androidft.R;
import com.hblolj.androidft.Utils.AppContext;
import com.hblolj.androidft.Utils.FileUtils;
import com.hblolj.androidft.adapter.FileInfoSelectedAdapter;
import com.hblolj.androidft.bean.FileInfo;
import com.hblolj.androidft.receiver.SeletedFileListChangedBroadcastReceiver;

import java.util.Map;
import java.util.Set;

/**
 * 显示选中的文件列表对话框
 * Created by hblolj on 2017/2/16.
 */
public class ShowSelectedFileInfoDialog implements View.OnClickListener {

    private final Button btn_opration;//清空按钮
    private final TextView tv_title;
    private final ListView lv_selected_file_info;
    private Context mContext;
    private AlertDialog mAlertDialog;
    private FileInfoSelectedAdapter mFileInfoSelectedAdapter;

    public ShowSelectedFileInfoDialog(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_show_selected_file_info_dialog, null);
        btn_opration = ((Button) view.findViewById(R.id.btn_operation));
        tv_title = ((TextView) view.findViewById(R.id.tv_title));
        lv_selected_file_info = ((ListView) view.findViewById(R.id.lv_selected_file_info));
        String title = getAllSelectedFileDes();
        tv_title.setText(title);

        mFileInfoSelectedAdapter = new FileInfoSelectedAdapter(context);
        mFileInfoSelectedAdapter.setOnDataListChangedListener(new FileInfoSelectedAdapter.onDataListChangedListener() {
            @Override
            public void onDataChanged() {
                if (mFileInfoSelectedAdapter.getCount() == 0){
                    hide();
                }
                tv_title.setText(getAllSelectedFileDes());
                sendUpdateSelectedFileBR();//发送更新选中文件广播
            }
        });
        lv_selected_file_info.setAdapter(mFileInfoSelectedAdapter);

        btn_opration.setOnClickListener(this);

        this.mAlertDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .create();
    }

    private void sendUpdateSelectedFileBR() {
        mContext.sendBroadcast(new Intent(SeletedFileListChangedBroadcastReceiver.ACTION_CHOOSE_FILE_LIST_CHANGED));
    }

    private String getAllSelectedFileDes() {
        String title = "";

        long totalSize = 0;

        //遍历Map
        Set<Map.Entry<String, FileInfo>> entrySet = AppContext.getAppContext().getFileInfoMap().entrySet();
        for (Map.Entry<String, FileInfo> entry : entrySet) {
            FileInfo fileInfo = entry.getValue();
            totalSize += fileInfo.getSize();
        }
        title = mContext.getResources().getString(R.string.str_selected_file_info_detail)
                .replace("{count}", String.valueOf(entrySet.size()))
                .replace("{size}", String.valueOf(FileUtils.getFileSize(totalSize)));

        return title;
    }

    /**
     * 显示
     */
    public void show(){
        if (this.mAlertDialog != null){
            notifyDataSetChanged();
            tv_title.setText(getAllSelectedFileDes());
            this.mAlertDialog.show();
        }
    }

    private void notifyDataSetChanged() {
        if (mFileInfoSelectedAdapter != null){
            mFileInfoSelectedAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 隐藏
     */
    public void hide(){
        if (this.mAlertDialog != null){
            this.mAlertDialog.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_operation :
            //清空按钮的点击事件
                clearAllSelectedFiles();
                sendUpdateSelectedFileBR();
            break;
        }
    }

    /**
     * 清除所有选中列表
     */
    private void clearAllSelectedFiles() {
        AppContext.getAppContext().getFileInfoMap().clear();
        notifyDataSetChanged();
        hide();
    }
}
