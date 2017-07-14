package com.hblolj.androidft.Utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Wifi管理类
 *
 * Created by hblolj on 2017/2/19.
 */

public class WifiMgr {

    private static WifiMgr mWifiMgr;
    private Context mContext;
    private WifiManager mWifiManager;

    public static final int WIFICIPHER_NOPASS = 1;
    public static final int WIFICIPHER_WEP = 2;
    public static final int WIFICIPHER_WPA = 3;

    public WifiMgr(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiMgr getInstance(Context context) {
        if (mWifiMgr == null){
            synchronized (WifiMgr.class){
                if (mWifiMgr == null){
                    mWifiMgr = new WifiMgr(context);
                }
            }
        }
        return mWifiMgr;
    }

    /**
     * 打开Wifi
     */
    public void openWifi(){
        if (mWifiManager != null){
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭Wifi
     */
    public void disableWifi(){
        if (mWifiManager != null){
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 判断Wifi是否开启状态
     * @return
     */
    public boolean isWifiEnable(){
        return mWifiManager==null ? false : mWifiManager.isWifiEnabled();
    }

    private List<ScanResult> scanResults;
    private List<WifiConfiguration> configuredNetworks;

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        return configuredNetworks;
    }

    /**
     * Wifi扫描
     */
    public void startScan(){
        if (mWifiManager != null){
            mWifiManager.startScan();
            scanResults = mWifiManager.getScanResults();
            configuredNetworks = mWifiManager.getConfiguredNetworks();
        }
    }

    /**
     *添加到指定Wifi网络/切换到指定Wifi网络
     * @param wf
     * @return
     */
    public boolean addNetWork(WifiConfiguration wf){
        //断开当前连接的WIFI
        disconnectCurrentNetWork();
        //连接新的WIFI
        int netId = mWifiManager.addNetwork(wf);
        boolean enable = mWifiManager.enableNetwork(netId, true);
        return enable;
    }

    /**
     * 断开当前连接的Wifi
     */
    private boolean disconnectCurrentNetWork() {
        if (mWifiManager != null && mWifiManager.isWifiEnabled()){
            int netId = mWifiManager.getConnectionInfo().getNetworkId();
            return mWifiManager.disableNetwork(netId);
        }
        return false;
    }

    /**
     * 创建WifiConfiguration
     * @param ssid
     * @param password
     * @param type
     * @return
     */
    public static WifiConfiguration createWifiCfg(String ssid, String password, int type){

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        config.SSID = "\"" + ssid + "\"";
        if (type == WIFICIPHER_NOPASS){
            //无密码连接
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            无密码连接WIFI时，连接不上wifi，需要注释两行代码
//            config.wepKeys[0] = "";
//            config.wepTxKeyIndex = 0;
        }else if (type == WIFICIPHER_WEP){
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }else if (type == WIFICIPHER_WPA){
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;

    }

    /**
     * 获取当前WifiInfo
     * @return
     */
    public WifiInfo getWifiInfo(){
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return wifiInfo;
    }

    /**
     * 获取当前Wifi所分配的IP地址
     * @return
     */
    public String getCurrentIpAddress(){
        String ipAddress = "";
        int address = mWifiManager.getDhcpInfo().ipAddress;
        ipAddress = ((address & 0xFF)
                + "." + ((address >> 8) & 0xFF)
                + "." + ((address >> 16) & 0xFF)
                + "." + ((address >> 24) & 0xFF));

        return ipAddress;
    }

    /**
     * 设备连接Wifi之后，设备获取Wifi热点的IP地址
     * @return
     */
    public String getIpAddressFromHotspot(){
        // WifiAP ip address is hardcoded in Android.
        /* IP/netmask: 192.168.43.1/255.255.255.0 */
        String ipAddress = "";
        DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
        int address = dhcpInfo.gateway;
        ipAddress = ((address & 0xFF)
                + "." + ((address >> 8) & 0xFF)
                + "." + ((address >> 16) & 0xFF)
                + "." + ((address >> 24) & 0xFF));
        return ipAddress;
    }

    /**
     * 开启热点后，获取自身的热点IP地址
     * @return
     */
    public String getHotspotLocalIpAddress(){
        // WifiAP ip address is hardcoded in Android.
        /* IP/netmask: 192.168.43.1/255.255.255.0 */
        String ipAddress = "192.168.43.1";
        DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
        int address = dhcpInfo.serverAddress;
        ipAddress = ((address & 0xFF)
                + "." + ((address >> 8) & 0xFF)
                + "." + ((address >> 16) & 0xFF)
                + "." + ((address >> 24) & 0xFF));
        return ipAddress;
    }

}
