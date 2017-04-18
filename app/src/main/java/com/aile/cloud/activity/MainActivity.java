package com.aile.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.aile.cloud.R;
import com.aile.cloud.adapter.TagPagerAdapter;
import com.aile.cloud.net.bean.HomeBanner;
import com.aile.cloud.net.bean.HomeProduct;
import com.aile.cloud.net.request.GsonRequest;
import com.aile.cloud.net.request.URLConfig;
import com.aile.cloud.utils.AnimaUtils;
import com.aile.cloud.view.HomeAdBanner;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    public final static String TAG = "MainActivity";
    public final static String KEY_LOGIN_UID = "login_uid";
    public final static String KEY_LOGIN_PWD = "login_pwd";
    private RequestQueue mQueue;
    private RelativeLayout mHomeBannerContainer;
    private HomeAdBanner mHomeADBanner;

    private Button mRequest;
    private ViewPager mViewPager;
    private View mTagView;
    private TagPagerAdapter pageAdapter;

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
        mHomeBannerContainer = (RelativeLayout) findViewById(R.id.home_main_banner);
        mHomeADBanner = new HomeAdBanner(MainActivity.this, mHomeBannerContainer);
        mViewPager = (ViewPager) findViewById(R.id.home_fragment_vp);
        mRequest = (Button) findViewById(R.id.request);
    }

    private void initListener() {
        mRequest.setOnClickListener(this);
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
            case R.id.request:
                requestHomeProducts();
                break;
        }
    }

    private void requestHomeBanner() {
        GsonRequest<HomeBanner> gsonRequest = new GsonRequest<HomeBanner>(
                URLConfig.HOME_BANNER,
                HomeBanner.class,
                new Response.Listener<HomeBanner>() {
                    @Override
                    public void onResponse(HomeBanner ads) {
                        if (ads != null) {
                            List banners = ads.getAds();
                            if (banners != null) {
                                Log.d(TAG, "banners = " + banners.size());
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
                            Log.e(TAG, error.getMessage(), error);
                        }
                    }
                });

        mQueue.add(gsonRequest);
    }


    private void requestHomeProducts() {
        String pUrl = URLConfig.HOME_PRODUCT_LIST;
        Response.Listener<HomeProduct> pListener = new Response.Listener<HomeProduct>() {
            @Override
            public void onResponse(HomeProduct p) {
                if (p != null) {
                    List ps = p.result.getData();
                    Log.d(TAG, "" + ps.size());
                }
            }
        };
        Response.ErrorListener pErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage(), error);
                }
            }
        };

        GsonRequest<HomeProduct> gsonRequest = new GsonRequest<HomeProduct>(pUrl, HomeProduct.class, pListener, pErrorListener);
        mQueue.add(gsonRequest);
    }

//    private void requestHomeProductDetail() {
//        String productId = "34";
//        String detailUrl = URLConfig.HOME_PRODUCT_DETAIL + productId;
//        Response.Listener<ProductDetail> pDetailListener = new Response.Listener<ProductDetail>() {
//            @Override
//            public void onResponse(ProductDetail p) {
//                if (p != null) {
//                    Log.d(TAG, "msg = " + p.result.msg + ", total = " + p.body.getTotal());
//                    Log.d(TAG, "imgUrl = " + p.body.getRows().getImageList().get(0).imgUrl);
//                }
//            }
//        };
//        Response.ErrorListener pDetailErrorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error != null) {
//                    Log.e(TAG, error.getMessage(), error);
//                }
//            }
//        };
//        GsonRequest<ProductDetail> gsonRequest = new GsonRequest<ProductDetail>(detailUrl, ProductDetail.class, pDetailListener, pDetailErrorListener);
//        mQueue.add(gsonRequest);
//    }

}
