package com.hblolj.androidft;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hblolj.androidft.Utils.ToastUtils;
import com.hblolj.androidft.Utils.WifiMgr;
import com.hblolj.androidft.adapter.ReceiverListAdapter;

import java.util.List;

public class ChooseReceiverActivity extends AppCompatActivity {

    private ListView mScanResults;
    private List<ScanResult> scanResults;
    private List<WifiConfiguration> configuredNetworks;
    private ReceiverListAdapter scanResultAdapter;

    private static final int MSG_TO_SHOW_SCAN_RESULT = 0x99;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_TO_SHOW_SCAN_RESULT:
                    getOrUpadateData();
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_receiver);
        initViews();
        getOrUpadateData();
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 1000);
    }

    private void getOrUpadateData() {
        WifiMgr mgr = WifiMgr.getInstance(this);
        if (!mgr.isWifiEnable()){
            mgr.openWifi();
        }
        mgr.startScan();
        scanResults = mgr.getScanResults();
        configuredNetworks = mgr.getConfiguredNetworks();
        if (scanResults != null){
            if (scanResultAdapter == null){
                scanResultAdapter = new ReceiverListAdapter(this, scanResults);
                mScanResults.setAdapter(scanResultAdapter);
            }else {
                scanResultAdapter.updateData(scanResults);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initViews() {
        mScanResults = ((ListView) findViewById(R.id.lv_scan_result));
        View title = LayoutInflater.from(this).inflate(R.layout.header_scanresult_title, null);
        View animation = LayoutInflater.from(this).inflate(R.layout.header_scanresult_animation, mScanResults, false);
        mScanResults.addHeaderView(title);
        mScanResults.addHeaderView(animation);
        mScanResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击应该连接，根据点击的wifi的加密方式，选择是否要填写密码
                ToastUtils.show(ChooseReceiverActivity.this, parent.getAdapter().getItemId(position)+" : " + position);
            }
        });
    }
}
