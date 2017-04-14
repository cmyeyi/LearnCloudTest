package com.aile.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aile.cloud.R;
import com.aile.cloud.utils.AnimaUtils;

public class MainActivity extends Activity implements View.OnClickListener {
    public final static String KEY_LOGIN_UID = "login_uid";
    public final static String KEY_LOGIN_PWD = "login_pwd";
    private Button mDropButton;

    private ImageView mImageView;

    public static void launch(Activity activity, String userId, String pwd) {
        Intent i = new Intent(activity, MainActivity.class);
        i.putExtra(KEY_LOGIN_UID, userId);
        i.putExtra(KEY_LOGIN_PWD, pwd);
        AnimaUtils.showUp(activity, i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        mDropButton = (Button) findViewById(R.id.main_drop);
        mImageView = (ImageView) findViewById(R.id.main_iv);
    }

    private void initListener() {
        if (mDropButton != null) {
            mDropButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.main_drop:
                break;
        }
    }


}
