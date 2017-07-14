package com.hblolj.androidft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hblolj.androidft.Utils.FileUtils;
import com.hblolj.androidft.base.BaseActivity;
import com.hblolj.androidft.bean.FileInfo;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button send_file;
    private Button receiver_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send_file = ((Button) findViewById(R.id.btn_sned_file));
        receiver_file = ((Button) findViewById(R.id.btn_receiver_file));
        send_file.setOnClickListener(this);
        receiver_file.setOnClickListener(this);
//        FileUtils.getSpecificTypeFiles(this, new String[]{FileInfo.EXTEND_JPEG, FileInfo.EXTEND_JPG});

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sned_file:
                //发送文件
                Intent intent = new Intent(this, ChooseFileActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_receiver_file:
                //接收文件
                break;
        }
    }
}
