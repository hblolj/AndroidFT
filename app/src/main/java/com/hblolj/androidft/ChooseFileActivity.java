package com.hblolj.androidft;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hblolj.androidft.Utils.AppContext;
import com.hblolj.androidft.bean.FileInfo;
import com.hblolj.androidft.fragment.FileInfoFragment;
import com.hblolj.androidft.receiver.SeletedFileListChangedBroadcastReceiver;
import com.hblolj.androidft.ui.view.ShowSelectedFileInfoDialog;

import java.util.ArrayList;
import java.util.List;

public class ChooseFileActivity extends AppCompatActivity implements View.OnClickListener {

    private List<FileInfoFragment> mFileInfoFragments = new ArrayList<>();
    private FileInfoFragment mApkInfoFragment;
    private FileInfoFragment mJpgInfoFragment;
    private FileInfoFragment mMp3InfoFragment;
    private FileInfoFragment mMp4InfoFragment;
    /**
     * ViewPager相关UI
     */
    private ViewPager mFileViewPager;
    private TabLayout mTabLayout;
    /**
     * ButtomBar相关UI
     */
    private Button btn_selected;
    private Button btn_next;

    /**
     * 显示当前选中的所有文件的对话框
     */
    ShowSelectedFileInfoDialog mShowSelectedFileInfoDialog;

    /**
     *
     */
    SeletedFileListChangedBroadcastReceiver mSeletedFileListChangedBroadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
        initViews();
        initDatas();
    }

    //初始化数据 准备四个FileInfoFragment
    private void initDatas() {
        mApkInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_APK);
        mJpgInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_JPG);
        mMp3InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP3);
        mMp4InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP4);
        mFileInfoFragments.add(mApkInfoFragment);
        mFileInfoFragments.add(mJpgInfoFragment);
        mFileInfoFragments.add(mMp3InfoFragment);
        mFileInfoFragments.add(mMp4InfoFragment);

//        mCurrenInfoFragment = mApkInfoFragment;

        String[] title = getResources().getStringArray(R.array.array_res);
        ResPagerAdapter resPagerAdapter = new ResPagerAdapter(getSupportFragmentManager(), title);
        mFileViewPager.setAdapter(resPagerAdapter);
        mFileViewPager.setOffscreenPageLimit(4);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mFileViewPager);
        setSelectedViewStyle(false);
        mShowSelectedFileInfoDialog = new ShowSelectedFileInfoDialog(this);

        mSeletedFileListChangedBroadcastReceiver = new SeletedFileListChangedBroadcastReceiver() {
            @Override
            public void onSelectedFileListChanged() {
                //更新四个Fragment
                update();
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SeletedFileListChangedBroadcastReceiver.ACTION_CHOOSE_FILE_LIST_CHANGED);
        registerReceiver(mSeletedFileListChangedBroadcastReceiver, intentFilter);
    }

    private void update() {
        if (mApkInfoFragment != null) mApkInfoFragment.updateFileInfoAdapter();
        if (mJpgInfoFragment != null) mJpgInfoFragment.updateFileInfoAdapter();
        if (mMp3InfoFragment != null) mMp3InfoFragment.updateFileInfoAdapter();
        if (mMp4InfoFragment != null) mMp4InfoFragment.updateFileInfoAdapter();
        //更新选中按钮
        getSelectedView();
    }

    @Override
    protected void onDestroy() {
        if (mSeletedFileListChangedBroadcastReceiver != null){
            unregisterReceiver(mSeletedFileListChangedBroadcastReceiver);
            mSeletedFileListChangedBroadcastReceiver = null;
        }
        super.onDestroy();
    }

    private void initViews() {
        mFileViewPager = ((ViewPager) findViewById(R.id.id_vp_file));
        mTabLayout = ((TabLayout) findViewById(R.id.tab_layout));
        btn_selected = ((Button) findViewById(R.id.btn_selected));
        btn_next = ((Button) findViewById(R.id.btn_next));

        btn_selected.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    /**
     * 根据最新数据更新已选按钮内容与style
     * 然后将已选按钮返回 作为Animotion的targetView
     * @return
     */
    public View getSelectedView() {
        if (AppContext.getAppContext().getFileInfoMap() != null &&
                AppContext.getAppContext().getFileInfoMap().size() > 0){
            //如果已选中文件集合中有文件，设置正确的大小给btn_selected
            setSelectedViewStyle(true);
            int size = AppContext.getAppContext().getFileInfoMap().size();
            btn_selected.setText(this.getResources().getString(R.string.str_has_selected_detail, size));
        }else {
            setSelectedViewStyle(false);
            btn_selected.setText(this.getResources().getString(R.string.str_has_selected));
        }
        return btn_selected;
    }

    /**
     * true  可以点击
     * false 不可点击
     * @param b
     */
    private void setSelectedViewStyle(boolean b) {
        btn_selected.setEnabled(b);
        if (b){
            btn_selected.setBackgroundResource(R.drawable.selector_bottom_text_common);
            btn_selected.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else {
            btn_selected.setBackgroundResource(R.drawable.shape_bottom_text_unenable);
            btn_selected.setTextColor(getResources().getColor(R.color.darker_gray));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_selected :
                //已选
                if (mShowSelectedFileInfoDialog != null){
                    mShowSelectedFileInfoDialog.show();
                }
                break;
            case R.id.btn_next :
                //下一步
                Intent intent = new Intent(this, ChooseReceiverActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 资源文件的ViewPager适配器
     */
    class ResPagerAdapter extends FragmentPagerAdapter{

        String[] sTitleArray;

        public ResPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ResPagerAdapter(FragmentManager fm, String[] sTitleArray) {
            super(fm);
            this.sTitleArray = sTitleArray;
        }

        @Override
        public Fragment getItem(int position) {
            return mFileInfoFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFileInfoFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sTitleArray[position];
        }
    }
}
