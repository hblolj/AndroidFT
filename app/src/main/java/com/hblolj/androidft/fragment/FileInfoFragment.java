package com.hblolj.androidft.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.hblolj.androidft.ChooseFileActivity;
import com.hblolj.androidft.R;
import com.hblolj.androidft.Utils.AnimationUtils;
import com.hblolj.androidft.Utils.AppContext;
import com.hblolj.androidft.Utils.FileUtils;
import com.hblolj.androidft.Utils.ToastUtils;
import com.hblolj.androidft.adapter.FileInfoAdapter;
import com.hblolj.androidft.bean.FileInfo;

import java.util.List;

/**
 * Created by hblolj on 2017/2/13.
 */

@SuppressLint("ValidFragment")
public class FileInfoFragment extends Fragment{

    //当前FileInfoFragment的文件类型
    private int mType = FileInfo.TYPE_APK;
    private GridView gv;
    private ProgressBar pb;
    private List<FileInfo> mFileInfos;
    private FileInfoAdapter mFileInfoAdapter;

    public FileInfoFragment() {
    }

    public FileInfoFragment(int type) {
        this.mType = type;
    }

    public static FileInfoFragment newInstance(int type) {
        FileInfoFragment fragment = new FileInfoFragment(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        gv = (GridView) rootView.findViewById(R.id.gv);
        pb = (ProgressBar) rootView.findViewById(R.id.pb);
        switch (mType){
            case FileInfo.TYPE_APK:
                gv.setNumColumns(4);//应用
                break;
            case FileInfo.TYPE_JPG:
                gv.setNumColumns(3);//图片
                break;
            case FileInfo.TYPE_MP3:
                gv.setNumColumns(1);//音乐
                break;
            case FileInfo.TYPE_MP4:
                gv.setNumColumns(1);//视频
                break;
        }
        init();//初始化界面
        return rootView;
    }

    private void init() {
        //异步操作获取指定类型的文件集合
        new getFileInfoListTask(mType, getContext()).executeOnExecutor(AppContext.MAIN_EXECUTOR);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo info = mFileInfos.get(position);
                if (AppContext.getAppContext().isExist(info)){
                    AppContext.getAppContext().delFileInfo(info);
                    updateSelectedView();
                }else {
                    //1.添加任务
                    AppContext.getAppContext().addFileInfo(info);
                    //2.添加动画
                    View startView = null;
                    View targetView = null;

                    startView = view.findViewById(R.id.iv_shortcut);
                    if (getActivity() != null && (getActivity() instanceof ChooseFileActivity)){
                        ChooseFileActivity activity = (ChooseFileActivity) getActivity();
                        targetView = activity.getSelectedView();
                    }
                    AnimationUtils.setAddTaskAnimation(getActivity(), startView, targetView, null);
                }
                mFileInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 更新ChooseActivity的选中按钮
     */
    private void updateSelectedView() {
        if (getActivity() != null && (getActivity() instanceof ChooseFileActivity)){
            ChooseFileActivity activity = (ChooseFileActivity) getActivity();
            activity.getSelectedView();
        }
    }

    @Override
    public void onResume() {
        updateFileInfoAdapter();
        super.onResume();
    }

    public void updateFileInfoAdapter() {
        if (mFileInfoAdapter != null){
            mFileInfoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class getFileInfoListTask extends AsyncTask<String, Integer, List<FileInfo>>{

        int sType = FileInfo.TYPE_APK;
        Context sContext = null;
        List<FileInfo> sFileInfoList = null;

        public getFileInfoListTask(int sType, Context sContext) {
            this.sType = sType;
            this.sContext = sContext;
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
            super.onPreExecute();
        }

        @Override
        protected List<FileInfo> doInBackground(String... params) {
            //getSpecificTypeFiles() 只获取了存储路径、文件名(不包括后缀)、文件大小(单位是bit)
            //getDetailFileInfo() 获取了文件的缩略图和SizeDesc
            if (sType == FileInfo.TYPE_APK){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{FileInfo.EXTEND_APK});
                sFileInfoList = FileUtils.getDetailFileInfo(sContext, sFileInfoList, sType);
            }else if (sType == FileInfo.TYPE_JPG){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{FileInfo.EXTEND_JPEG, FileInfo.EXTEND_JPG});
                sFileInfoList = FileUtils.getDetailFileInfo(sContext, sFileInfoList, sType);
            }else if (sType == FileInfo.TYPE_MP3){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{FileInfo.EXTEND_MP3});
                sFileInfoList = FileUtils.getDetailFileInfo(sContext, sFileInfoList, sType);
            }else if (sType == FileInfo.TYPE_MP4){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{FileInfo.EXTEND_MP4});
                sFileInfoList = FileUtils.getDetailFileInfo(sContext, sFileInfoList, sType);
            }
            mFileInfos = sFileInfoList;
            return sFileInfoList;
        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfos) {
            hideProgressBar();
            //根据类型，将拿到的文件集合设置给设配器，适配器设置给GridView
            mFileInfoAdapter = new FileInfoAdapter(mFileInfos, sContext, mType);
            gv.setAdapter(mFileInfoAdapter);
        }
    }

    /**
     * 显示Loading
     */
    private void showProgressBar() {
        if (pb != null){
            pb.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏Loading
     */
    private void hideProgressBar(){
        if (pb != null && pb.isShown()){
            pb.setVisibility(View.GONE);
        }
    }
}
