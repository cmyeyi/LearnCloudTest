package com.aile.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aile.cloud.R;
import com.aile.cloud.AppApplication;
import com.aile.cloud.net.IRequestListener;
import com.aile.cloud.net.RequestManager;
import com.aile.cloud.net.request.LoginParam;
import com.aile.cloud.net.request.LoginRequest;
import com.aile.cloud.net.request.LoginResponse;
import com.aile.www.basesdk.LoginManager;

public class LoginActivity extends Activity implements View.OnClickListener {

    private View mLoadingView;
    private TextView mCommitView;
    private EditText mUserNameView;
    private EditText mUserPwdView;
    private TextView mPwdLostView;
    private String mUserId;
    private String mPassword;
    private String mUserName;

    public static void launch(Activity activity) {
        if (null == activity) {
            return;
        }
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView() {
        mLoadingView = findViewById(R.id.login_net_loading);
        mCommitView = (TextView) findViewById(R.id.login_commit_button);
        mUserNameView = (EditText) findViewById(R.id.login_user_name);
        mUserPwdView = (EditText) findViewById(R.id.login_user_pwd);
        mPwdLostView = (TextView) findViewById(R.id.login_user_pwd_lost);
        mPwdLostView.setVisibility(View.GONE);
        loginAuto();
    }


    private void loginAuto() {
        if(LoginManager.getInstance().isUserLogin()) {
            mUserId = LoginManager.getInstance().getUserInfo().getUserId();
            mPassword = LoginManager.getInstance().getUserInfo().getPassword();
            mUserName = LoginManager.getInstance().getUserInfo().getName();
            MainActivity.launch(LoginActivity.this, mUserId, mPassword);
            finish();
        }
    }

    private void initListener() {
        mCommitView.setOnClickListener(this);
        mPwdLostView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_commit_button:
                String name = mUserNameView.getText().toString().trim();
                String pwd = mUserPwdView.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_user_name_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.login_user_pwd_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                LoginParam param = LoginParam.create(name, pwd);
                LoginRequest request = new LoginRequest(param, new IRequestListener<LoginResponse>() {
                    @Override
                    public void onUIComplete(LoginResponse response) {
                        if(response != null) {
                            if (response.isSuccess()) {
                                mUserId = response.getLoginInfo().getUserId();
                                mPassword = response.getLoginInfo().getPassword();
                                mUserName = response.getLoginInfo().getName();
                                AppApplication.getInstance().setUserName(mUserName);
                                LoginManager.getInstance().setUserInfo(response.getLoginInfo());
                                MainActivity.launch(LoginActivity.this, mUserId, mPassword);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, response.getReturnInfo().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
                RequestManager.getInstance().requestAsync(request);
                break;
        }
    }
}