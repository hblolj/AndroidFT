package com.hblolj.androidft.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.hblolj.androidft.R;
import com.hblolj.androidft.bean.FileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hblolj on 2017/2/12.
 */

public class FileUtils {

    /**
     * 小数的格式化
     */
    public static final DecimalFormat FORMAT_TWO = new DecimalFormat("####.##");
    public static final DecimalFormat FORMAT_ONE = new DecimalFormat("####.#");

    public static void getAllSongs(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        System.out.println(externalContentUri.toString());
        Cursor cursor = contentResolver.query(externalContentUri,
                new String[]{MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,}, MediaStore.Audio.Media.MIME_TYPE + "=? or "
                + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);
        if (cursor.moveToFirst()){
            System.out.println("----------------------------");
            while (cursor.moveToNext()){
                System.out.println("ID: " + cursor.getString(0));
                System.out.println("文件名: " + cursor.getString(1));
                System.out.println("歌曲名: " + cursor.getString(2));
                System.out.println("时长: " + cursor.getString(3));
                System.out.println("歌手名: " + cursor.getString(4));
                System.out.println("专辑名: " + cursor.getString(5));
                System.out.println("年代: " + cursor.getString(6));
                System.out.println("歌曲格式: " + cursor.getString(7));
                System.out.println("文件大小: " + cursor.getString(8));
                System.out.println("文件路径: " + cursor.getString(9));
            }
        }
    }


    /**
     * 获取内存卡中指定类型文件
     * 该方法只获取文件的存储地址与大小、文件名三个属性
     * @param context
     * @param extension 后缀名集合
     */
    public static List<FileInfo> getSpecificTypeFiles(Context context, String[] extension){
        List<FileInfo> fileInfoList = new ArrayList<>();
        //内存卡文件的Uri
        Uri fileUri = MediaStore.Files.getContentUri("external");
        //查询的列名集合
        String[] projection = new String[]{MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.SIZE};
        //查询条件 where 条件应该是传递进来的extension(后缀匹配)
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            String suffix = extension[i];
            if (i != 0){
                selection += " OR ";
            }
            selection += MediaStore.Files.FileColumns.DATA + " LIKE '%" + suffix + "'";
        }

        //按时间降序条件
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;

        Cursor cursor = context.getContentResolver().query(fileUri, projection, selection,
                null, sortOrder);

        if (cursor != null){
//            int num = 0;
            while (cursor.moveToNext()){
                FileInfo info = new FileInfo();
                String path = cursor.getString(0);
                String name = cursor.getString(1);
                long size = cursor.getLong(2);
//                File file = new File(path);
//                long size2 = file.length();
                info.setFilePath(path);
                info.setName(name);
                info.setSize(size);
                fileInfoList.add(info);
//                System.out.println("path: " + path);
//                System.out.println("name: " + name);
//                System.out.println("size1: " + size);
//                System.out.println("size2: " + size2);
//                num += 1;
//                System.out.println("------------------------------");
            }
//            System.out.println("Total : " + num);
            cursor.close();
        }
        return fileInfoList;
    }

    /**
     * 转化成完整的FileInfo
     * @param context
     * @param sFileInfoList
     * @param sType
     */
    public static List<FileInfo> getDetailFileInfo(Context context, List<FileInfo> sFileInfoList, int sType) {
        //Bitmap缩略图
        //文件名 不包括后缀的
        //文件大小 满1024自动进位
        if (sFileInfoList == null || sFileInfoList.size() <= 0){
            return sFileInfoList;
        }
        for (FileInfo info : sFileInfoList) {
            info.setSizeDesc(getFileSize(info.getSize()));
            switch (sType){
                case FileInfo.TYPE_APK:
                    info.setBitmap(drawableToBitmap(getApkThumbnail(context, info.getFilePath())));
                    break;
                case FileInfo.TYPE_JPG:
                    //不需要缩略图 Glide直接加载图片
                    break;
                case FileInfo.TYPE_MP3:
                    //不需要缩略图
                    break;
                case FileInfo.TYPE_MP4:
                    info.setBitmap(getScreenshotBitmap(context, info.getFilePath(), sType));
                    break;
            }
            info.setFileType(sType);
        }
        return sFileInfoList;
    }

    /**
     * 获取缩略图的Bitmap
     * @param context
     * @param filePath
     * @param sType
     * @return
     */
    private static Bitmap getScreenshotBitmap(Context context, String filePath, int sType) {
        Bitmap bitmap = null;
        switch (sType){
            case FileInfo.TYPE_APK :
                Drawable drawable = getApkThumbnail(context, filePath);
                if (drawable != null){
                    bitmap = drawableToBitmap(drawable);
                }else {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                }
                break;
            case FileInfo.TYPE_JPG :
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(filePath)));
                } catch (FileNotFoundException e) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_jpg);
                    e.printStackTrace();
                }
                bitmap = ScreenshotUtils.extractThumbnail(bitmap, 100, 100);
                break;
            case FileInfo.TYPE_MP3 :
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_mp3);
                bitmap = ScreenshotUtils.extractThumbnail(bitmap, 100, 100);
                break;
            case FileInfo.TYPE_MP4 :
                try {
                    bitmap = ScreenshotUtils.createVideoThumbnail(filePath);
                } catch (Exception e) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_mp4);
                }
                bitmap = ScreenshotUtils.extractThumbnail(bitmap, 100, 100);
                break;
        }
        return bitmap;
    }

    /**
     * Drawable 转 Bitmap
     * @param drawable
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null){
            return null;
        }

        //获取drawable的宽高
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //获取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        //建立对应的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //建立画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0 ,0, width, height);
        //把drawable画到画布上
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过Apk文件获取Log图标
     * @param context
     * @param filePath
     * @return
     */
    private static Drawable getApkThumbnail(Context context, String filePath) {
        if (context == null){
            return null;
        }

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            /**
             * 获取apk图标
             */
            applicationInfo.sourceDir = filePath;
            applicationInfo.publicSourceDir = filePath;
            if (applicationInfo != null){
                Drawable apk_icon = applicationInfo.loadIcon(pm);
                return apk_icon;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileSize(long size) {
        if (size <= 0){
            return "0B";
        }

        double value = 0f;

        if (size / 1024 < 1 ){
            return size + "B";
        }else if ((size / (1024 * 1024)) < 1){
            value = size / 1024f;
            return FORMAT_TWO.format(value) + "KB";
        }else if (size / (1024 * 1024 * 1024) < 1){
            value = size / (1024 * 1024);
            return FORMAT_TWO.format(value) + "MB";
        }else {
            value = size / (1024 * 1024 * 1024);
            return FORMAT_TWO.format(value) + "GB";
        }
    }

    /**
     * 判断文件是否是Apk文件
     * @param filePath
     * @return
     */
    public static boolean isApkFile(String filePath) {
        if (TextUtils.isEmpty(filePath)){
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_APK) > 0){
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否是jpg或者jpeg
     * @param filePath
     * @return
     */
    public static boolean isJpgFile(String filePath) {
        if (TextUtils.isEmpty(filePath)){
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_JPEG) > 0 || filePath.lastIndexOf(FileInfo.EXTEND_JPG) > 0){
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否是mp3
     * @param filePath
     * @return
     */
    public static boolean isMp3File(String filePath) {
        if (TextUtils.isEmpty(filePath)){
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_MP3) > 0){
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否是mp4
     * @param filePath
     * @return
     */
    public static boolean isMp4File(String filePath) {
        if (TextUtils.isEmpty(filePath)){
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_MP4) > 0){
            return true;
        }
        return false;
    }
}
