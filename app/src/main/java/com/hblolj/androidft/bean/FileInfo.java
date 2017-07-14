package com.hblolj.androidft.bean;

import android.graphics.Bitmap;

/**
 * Created by hblolj on 2017/2/13.
 */

public class FileInfo {

    /**
     * 常见文件拓展名
     */
    public static final String EXTEND_APK = ".apk";
    public static final String EXTEND_JPEG = ".jpeg";
    public static final String EXTEND_JPG = ".jpg";
    public static final String EXTEND_PNG = ".png";
    public static final String EXTEND_MP3 = ".mp3";
    public static final String EXTEND_MP4 = ".mp4";

    /**
     * 自定义文件类型
     */
    public static final int TYPE_APK = 1;
    public static final int TYPE_JPG = 2;
    public static final int TYPE_MP3 = 3;
    public static final int TYPE_MP4 = 4;


    //必要属性
    /**
     * 文件存储路径
     */
    private String filePath;
    /**
     * 文件类型
     */
    private int fileType;
    /**
     * 文件大小 单位bit eg 40544544
     */
    private long size;
    /**
     * 文件大小描述 eg:1MB 2GB.....
     */
    private String sizeDesc;

    //非必要属性
    /**
     * 文件显示名字  不带后缀
     */
    private String name;
    /**
     * 文件缩略图
     */
    private Bitmap bitmap;

    public String getSizeDesc() {
        return sizeDesc;
    }

    public void setSizeDesc(String sizeDesc) {
        this.sizeDesc = sizeDesc;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
