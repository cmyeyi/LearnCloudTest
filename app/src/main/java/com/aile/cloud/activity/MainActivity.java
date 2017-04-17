package com.aile.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.aile.cloud.R;
import com.aile.cloud.ad.Ads;
import com.aile.cloud.net.request.GsonRequest;
import com.aile.cloud.net.request.URLConfig;
import com.aile.cloud.utils.AnimaUtils;
import com.aile.cloud.view.HomeAdBanner;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    public final static String KEY_LOGIN_UID = "login_uid";
    public final static String KEY_LOGIN_PWD = "login_pwd";
    private RequestQueue mQueue;
    private RelativeLayout mHomeBannerContainer;
    private HomeAdBanner mHomeADBanner;
    private Button mApiButton;

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
        initVolley();
        loadData();
    }

    private void initView() {
        mApiButton = (Button) findViewById(R.id.main_api);
        mHomeBannerContainer = (RelativeLayout) findViewById(R.id.home_main_banner);
        mHomeADBanner = new HomeAdBanner(MainActivity.this, mHomeBannerContainer);
    }

    private void initListener() {
        if (mApiButton != null) {
            mApiButton.setOnClickListener(this);
        }
    }

    private void initVolley() {
        mQueue = Volley.newRequestQueue(this);
    }

    private void loadData() {
        requestHomeBanner();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.main_api:
                requestHomeBanner();
                break;
        }
    }

    private void requestHomeBanner() {
        GsonRequest<Ads> gsonRequest = new GsonRequest<Ads>(
                URLConfig.HOME_BANNER,
                Ads.class,
                new Response.Listener<Ads>() {
                    @Override
                    public void onResponse(Ads ads) {
                        if (ads != null) {
                            List banners = ads.getAds();
                            if (banners != null) {
                                Log.d("YYYY", "banners = " + banners.size());
                                mHomeADBanner.reset();
                                mHomeADBanner.setData(banners);
                                mHomeADBanner.showBanner();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e("YYYY", error.getMessage(), error);
                        }
                    }
                });

        mQueue.add(gsonRequest);
    }


}
