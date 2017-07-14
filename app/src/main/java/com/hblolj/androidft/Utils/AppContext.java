package com.hblolj.androidft.Utils;

import android.app.Application;

import com.hblolj.androidft.bean.FileInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 全局的ApplicationContext
 * Created by hblolj on 2017/2/15.
 */

public class AppContext extends Application {

    /**
     * 主要的线程池
     */
    public static Executor MAIN_EXECUTOR = Executors.newFixedThreadPool(5);

    /**
     * 全局应用的上下文
     */
    static AppContext mAppContext;

    //保存要发送的文件
    private Map<String, FileInfo> mFileInfoMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppContext = this;
    }

    /**
     * 获取全局AppContext
     * @return
     */
    public static AppContext getAppContext(){
        return mAppContext;
    }

    //---------------------------------------------------------------------------------------
    //                                      发送方
    //---------------------------------------------------------------------------------------

    /**
     * 添加文件到发送列表中
     * @param fileInfo
     */
    public void addFileInfo(FileInfo fileInfo){
        //如果没包含就添加
        if (!mFileInfoMap.containsKey(fileInfo.getFilePath())){
            mFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
        }
    }

    /**
     * 移除文件从发送列表中
     * @param fileInfo
     */
    public void delFileInfo(FileInfo fileInfo){
        //如果包含就移除
        if (mFileInfoMap.containsKey(fileInfo.getFilePath())){
            mFileInfoMap.remove(fileInfo.getFilePath());
        }
    }

    /**
     * 更新FileInfo
     * @param fileInfo
     */
    public void updateFileInfo(FileInfo fileInfo){
        mFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
    }

    /**
     * 是否存在FileInfo
     * @param fileInfo
     * @return
     */
    public boolean isExist(FileInfo fileInfo){
        if (fileInfo == null) return false;
        return mFileInfoMap.containsKey(fileInfo.getFilePath());
    }

    /**
     * 获取全局AppContext
     * @return
     */
    public Map<String, FileInfo> getFileInfoMap(){
        return mFileInfoMap;
    }
}
