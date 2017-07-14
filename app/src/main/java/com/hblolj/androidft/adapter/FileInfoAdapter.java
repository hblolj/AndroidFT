package com.hblolj.androidft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hblolj.androidft.R;
import com.hblolj.androidft.Utils.AppContext;
import com.hblolj.androidft.bean.FileInfo;

import java.util.List;

/**
 * Created by hblolj on 2017/2/14.
 * FileInfoFragment中GridView的适配器
 * 用来显示获取的文件集合
 */

public class FileInfoAdapter extends BaseAdapter {

    private List<FileInfo> mFileInfoList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mType;

    public FileInfoAdapter(List<FileInfo> fileInfoList, Context context, int type) {
        mFileInfoList = fileInfoList;
        mContext = context;
        mType = type;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mFileInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileInfo info = mFileInfoList.get(position);
        switch (mType){
            case FileInfo.TYPE_APK :
                ApkViewHolder apkViewHolder = null;
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.item_apk, null);
                    apkViewHolder = new ApkViewHolder();
                    apkViewHolder.iv_shortcut = ((ImageView) convertView.findViewById(R.id.iv_shortcut));
                    apkViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    apkViewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
                    apkViewHolder.iv_ok_tick = (ImageView) convertView.findViewById(R.id.iv_ok_tick);
                    convertView.setTag(apkViewHolder);
                }else {
                    apkViewHolder = (ApkViewHolder) convertView.getTag();
                }
                apkViewHolder.iv_shortcut.setImageBitmap(info.getBitmap());
                apkViewHolder.tv_name.setText(info.getName() == null ? "" : info.getName());
                apkViewHolder.tv_size.setText(info.getSizeDesc() == null ? "" : info.getSizeDesc());

                if (AppContext.getAppContext().isExist(info)){
                    apkViewHolder.iv_ok_tick.setVisibility(View.VISIBLE);
                }else {
                    apkViewHolder.iv_ok_tick.setVisibility(View.GONE);
                }
                break;
            case FileInfo.TYPE_JPG :
                PictureViewHolder pictureViewHolder = null;
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.item_picture, null);
                    pictureViewHolder = new PictureViewHolder();
                    pictureViewHolder.iv_shortcut = ((ImageView) convertView.findViewById(R.id.iv_shortcut));
                    pictureViewHolder.iv_ok_tick = (ImageView) convertView.findViewById(R.id.iv_ok_tick);
                    convertView.setTag(pictureViewHolder);
                }else {
                    pictureViewHolder = (PictureViewHolder) convertView.getTag();
                }
                Glide
                    .with(mContext)
                    .load(info.getFilePath())
                    .centerCrop()
                    .placeholder(R.mipmap.icon_jpg)
                    .crossFade()
                    .into(pictureViewHolder.iv_shortcut);

//                pictureViewHolder.iv_shortcut.setImageBitmap(info.getBitmap());
                if (AppContext.getAppContext().isExist(info)){
                    pictureViewHolder.iv_ok_tick.setVisibility(View.VISIBLE);
                }else {
                    pictureViewHolder.iv_ok_tick.setVisibility(View.GONE);
                }
                break;
            case FileInfo.TYPE_MP3 :
                Mp3ViewHolder viewHolder = null;
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.item_mp3, null);
                    viewHolder = new Mp3ViewHolder();
                    viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
                    viewHolder.iv_ok_tick = (ImageView) convertView.findViewById(R.id.iv_ok_tick);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (Mp3ViewHolder) convertView.getTag();
                }
                viewHolder.tv_name.setText(info.getName() == null ? "" : info.getName());
                viewHolder.tv_size.setText(info.getSizeDesc() == null ? "" : info.getSizeDesc());

                if (AppContext.getAppContext().isExist(info)){
                    viewHolder.iv_ok_tick.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iv_ok_tick.setVisibility(View.GONE);
                }
                break;
            case FileInfo.TYPE_MP4 :
                Mp4ViewHolder mp4ViewHolder = null;
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.item_mp4, null);
                    mp4ViewHolder = new Mp4ViewHolder();
                    mp4ViewHolder.iv_shortcut = ((ImageView) convertView.findViewById(R.id.iv_shortcut));
                    mp4ViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    mp4ViewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
                    mp4ViewHolder.iv_ok_tick = (ImageView) convertView.findViewById(R.id.iv_ok_tick);
                    convertView.setTag(mp4ViewHolder);
                }else {
                    mp4ViewHolder = (Mp4ViewHolder) convertView.getTag();
                }
                mp4ViewHolder.iv_shortcut.setImageBitmap(info.getBitmap());
                mp4ViewHolder.tv_name.setText(info.getName() == null ? "" : info.getName());
                mp4ViewHolder.tv_size.setText(info.getSizeDesc() == null ? "" : info.getSizeDesc());

                if (AppContext.getAppContext().isExist(info)){
                    mp4ViewHolder.iv_ok_tick.setVisibility(View.VISIBLE);
                }else {
                    mp4ViewHolder.iv_ok_tick.setVisibility(View.GONE);
                }
                break;
        }
        return convertView;
    }

    class ApkViewHolder{
        ImageView iv_shortcut;
        ImageView iv_ok_tick;
        TextView tv_name;
        TextView tv_size;
    }

    class PictureViewHolder{
        ImageView iv_shortcut;
        ImageView iv_ok_tick;
    }

    class Mp3ViewHolder{
        ImageView iv_ok_tick;
        TextView tv_name;
        TextView tv_size;
    }

    class Mp4ViewHolder{
        ImageView iv_shortcut;
        ImageView iv_ok_tick;
        TextView tv_name;
        TextView tv_size;
    }
}
