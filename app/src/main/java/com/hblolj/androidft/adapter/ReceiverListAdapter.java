package com.hblolj.androidft.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hblolj.androidft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件接收者的列表适配器(Wifi扫描到的周边的热点)
 * Created by hblolj on 2017/2/27.
 */

public class ReceiverListAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ScanResult> mScanResultList = new ArrayList<>();

    public ReceiverListAdapter(Context context, List<ScanResult> scanResultList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mScanResultList.addAll(scanResultList);
    }

    public void updateData(List<ScanResult> results){
        mScanResultList.clear();
        mScanResultList.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mScanResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ScanResult item = (ScanResult) getItem(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_receiver, null);
            holder = new ViewHolder();
            holder.tv_ssid = (TextView) convertView.findViewById(R.id.tv_ssid);
            holder.tv_bssid = (TextView) convertView.findViewById(R.id.tv_bssid);
            holder.tv_capabilities = (TextView) convertView.findViewById(R.id.tv_capabilities);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_ssid.setText(item.SSID);
        holder.tv_bssid.setText(item.BSSID);
        holder.tv_capabilities.setText(item.capabilities);

        return convertView;
    }

    class ViewHolder{
        TextView tv_ssid;
        TextView tv_bssid;
        TextView tv_capabilities;
    }
}
