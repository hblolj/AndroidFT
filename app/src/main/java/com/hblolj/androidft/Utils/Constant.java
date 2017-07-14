package com.hblolj.androidft.Utils;

import com.hblolj.androidft.bean.FileInfo;

import java.util.Comparator;
import java.util.Map;

/**
 * 常量类
 * 
 * Created by hblolj on 2017/2/15.
 */

public class Constant {

    //FileInfoMap 默认的Comparator
    public static final Comparator<? super Map.Entry<String,FileInfo>> DEFAULT_COMPARATOR =
            new Comparator<Map.Entry<String, FileInfo>>() {
        @Override
        public int compare(Map.Entry<String, FileInfo> o1, Map.Entry<String, FileInfo> o2) {
            if (o1.getValue().getFileType() > o2.getValue().getFileType()){
                return 1;
            }else if (o1.getValue().getFileType() < o2.getValue().getFileType()){
                return -1;
            }else {
                return 0;
            }
        }
    };
}
