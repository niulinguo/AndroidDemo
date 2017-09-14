package com.negro.apksigndemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.joker.api.Permissions4M;

/**
 * Created by Negro
 * Date 2017/9/14
 * Email niulinguo@163.com
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
