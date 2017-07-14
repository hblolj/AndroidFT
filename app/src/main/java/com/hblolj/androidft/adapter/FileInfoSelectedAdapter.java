package com.hblolj.androidft.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hblolj.androidft.R;
import com.hblolj.androidft.Utils.AppContext;
import com.hblolj.androidft.Utils.Constant;
import com.hblolj.androidft.Utils.FileUtils;
import com.hblolj.androidft.bean.FileInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hblolj on 2017/2/16.
 */

public class FileInfoSelectedAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private Map<String, FileInfo> mDataHashMap;
    private List<Map.Entry<String, FileInfo>> fileInfoMapList;
    private onDataListChangedListener mOnDataListChangedListener;

    public void setOnDataListChangedListener(onDataListChangedListener onDataListChangedListener) {
        mOnDataListChangedListener = onDataListChangedListener;
    }

    public FileInfoSelectedAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataHashMap = AppContext.getAppContext().getFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
    }

    @Override
    public void notifyDataSetChanged() {
        mDataHashMap = AppContext.getAppContext().getFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fileInfoMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileInfoMapList.get(position).getValue();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = (FileInfo) getItem(position);
        FileSenderHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new FileSenderHolder();
            convertView = mInflater.inflate(R.layout.item_transfer, null);
            viewHolder.iv_shortcut = (ImageView) convertView.findViewById(R.id.iv_shortcut);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            viewHolder.pb_file = (ProgressBar) convertView.findViewById(R.id.pb_file);
            viewHolder.btn_operation = (Button) convertView.findViewById(R.id.btn_operation);
            viewHolder.iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (FileSenderHolder) convertView.getTag();
        }
        if (fileInfo != null){
            viewHolder.pb_file.setVisibility(View.INVISIBLE);
            viewHolder.btn_operation.setVisibility(View.INVISIBLE);
            viewHolder.iv_tick.setVisibility(View.VISIBLE);
            viewHolder.iv_tick.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                    R.mipmap.icon_del));

            if (FileUtils.isApkFile(fileInfo.getFilePath()) || FileUtils.isMp4File(fileInfo.getFilePath())){
                //需要缩略图
                viewHolder.iv_shortcut.setImageBitmap(fileInfo.getBitmap());
            }else if (FileUtils.isJpgFile(fileInfo.getFilePath())){
                //图片格式
                Glide.with(mContext)
                        .load(fileInfo.getFilePath())
                        .centerCrop()
                        .placeholder(R.mipmap.icon_jpg)
                        .crossFade()
                        .into(viewHolder.iv_shortcut);
            }else if (FileUtils.isMp3File(fileInfo.getFilePath())){
                //音乐
                viewHolder.iv_shortcut.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_mp3));
            }
            viewHolder.tv_name.setText(fileInfo.getFilePath());
            viewHolder.tv_progress.setText(fileInfo.getSizeDesc());
            viewHolder.iv_tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消按钮的点击实现
                    AppContext.getAppContext().getFileInfoMap().remove(fileInfo.getFilePath());
                    notifyDataSetChanged();
                    if (mOnDataListChangedListener != null){
                        mOnDataListChangedListener.onDataChanged();
                    }
                }
            });
        }
        return convertView;
    }

    static class FileSenderHolder{
        ImageView iv_shortcut;
        TextView tv_name;
        TextView tv_progress;
        ProgressBar pb_file;

        Button btn_operation;
        ImageView iv_tick;
    }

    public interface onDataListChangedListener {
        void onDataChanged();
    }
}
