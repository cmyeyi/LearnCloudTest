package com.aile.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.aile.cloud.R;
import com.aile.cloud.adapter.TagPagerAdapter;
import com.aile.cloud.net.bean.HomeBanner;
import com.aile.cloud.net.bean.MTProduct;
import com.aile.cloud.net.bean.ProductLatest;
import com.aile.cloud.net.bean.ProductPopularity;
import com.aile.cloud.net.bean.PageProducts;
import com.aile.cloud.net.bean.ProductDetail;
import com.aile.cloud.net.bean.ProductSchedule;
import com.aile.cloud.net.bean.ProductTotal;
import com.aile.cloud.net.request.GSONRequest;
import com.aile.cloud.net.request.URLConfig;
import com.aile.cloud.utils.AnimaUtils;
import com.aile.cloud.view.HomeAdBanner;
import com.aile.www.basesdk.view.SimpleViewPagerIndicator;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public final static String TAG = "MainActivity";
    public final static String KEY_LOGIN_UID = "login_uid";
    public final static String KEY_LOGIN_PWD = "login_pwd";
    private RequestQueue mQueue;
    private RelativeLayout mHomeBannerContainer;
    private HomeAdBanner mHomeADBanner;

    private PageProducts mPageProducts;
    private ViewPager mViewPager;
    private View mTagView;
    private TagPagerAdapter mPpageAdapter;

    private Button mRequest;

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
        mPpageAdapter = new TagPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPpageAdapter);

        mRequest = (Button) findViewById(R.id.request);
    }

    private void initListener() {
        mRequest.setOnClickListener(this);
    }

    private void initVolley() {
        mQueue = Volley.newRequestQueue(this);
    }

    private void loadData() {
        mPageProducts = new PageProducts();
        requestHomeBanner();
    }

    private static int PAGE_COUNT = 4;

    private void setPages(PageProducts pageProducts) {
        SparseArray<List<MTProduct>> pageData = new SparseArray<>(PAGE_COUNT);
        List<String> tags = new ArrayList<>();
        int index = 0;

        if(pageProducts != null) {

            if(pageProducts.getProductPopularity()!=null && pageProducts.getProductPopularity().size() > 0) {
                tags.add(index, getString(R.string.home_tag_popularity));
                pageData.put(index, pageProducts.getProductPopularity());
                index++;
            }
            if(pageProducts.getProductSchedule()!=null && pageProducts.getProductSchedule().size() > 0) {
                tags.add(index, getString(R.string.home_tag_schedule));
                pageData.put(index, pageProducts.getProductSchedule());
                index++;
            }
            if(pageProducts.getProductLatest()!=null && pageProducts.getProductLatest().size() > 0) {
                tags.add(index, getString(R.string.home_tag_latest));
                pageData.put(index, pageProducts.getProductLatest());
                index++;
            }
            if(pageProducts.getProductTotal()!=null && pageProducts.getProductTotal().size() > 0) {
                tags.add(index, getString(R.string.home_tag_total));
                pageData.put(index, pageProducts.getProductTotal());
            }
        }

        mPpageAdapter.setData(pageData, tags);
        initIndicator();
    }

    private void initIndicator() {
        SimpleViewPagerIndicator indicator = (SimpleViewPagerIndicator) findViewById(R.id.home_fragment_indicator);
        indicator.setTexTSizeSP(14);
        indicator.setGravityType(Gravity.CENTER);
        indicator.setTextColor(getResources().getColor(R.color.new_black_3), getResources().getColor(R.color.new_black_1));
        float indicatorHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        indicator.setIndicatorColorAndHeight(getResources().getColor(R.color.new_orange_3), indicatorHeight);
        indicator.setViewPager(mViewPager);
        indicator.setTvTitleColor(0);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.request:
                requestProductPopularity();
                requestProductSchedule();
                requestProductsLatest();
                requestProductSchedule();
                break;
        }
    }

    private void requestHomeBanner() {
        GSONRequest<HomeBanner> GSONRequest = new GSONRequest<HomeBanner>(
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

        mQueue.add(GSONRequest);
    }


    private void requestProductPopularity() {
        String pUrl = URLConfig.HOME_PRODUCT_LIST;
        Response.Listener<ProductPopularity> pListener = new Response.Listener<ProductPopularity>() {
            @Override
            public void onResponse(ProductPopularity p) {
                if (p != null) {
                    List ps = p.result.getData();
                    Log.d(TAG, "" + ps.size());
                    mPageProducts.setProductPopularity(ps);
                    setPages(mPageProducts);
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

        GSONRequest<ProductPopularity> GSONRequest = new GSONRequest<ProductPopularity>(pUrl, ProductPopularity.class, pListener, pErrorListener);
        mQueue.add(GSONRequest);
    }

    private void requestProductSchedule() {
        String pUrl = URLConfig.HOME_PRODUCT_LIST;
        Response.Listener<ProductSchedule> pListener = new Response.Listener<ProductSchedule>() {
            @Override
            public void onResponse(ProductSchedule p) {
                if (p != null) {
                    List ps = p.result.getData();
                    Log.d(TAG, "" + ps.size());
                    mPageProducts.setProductSchedule(ps);
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

        GSONRequest<ProductSchedule> GSONRequest = new GSONRequest<ProductSchedule>(pUrl, ProductSchedule.class, pListener, pErrorListener);
        mQueue.add(GSONRequest);
    }

    private void requestProductsLatest() {
        String pUrl = URLConfig.HOME_PRODUCT_LIST;
        Response.Listener<ProductLatest> pListener = new Response.Listener<ProductLatest>() {
            @Override
            public void onResponse(ProductLatest p) {
                if (p != null) {
                    List ps = p.result.getData();
                    Log.d(TAG, "" + ps.size());
                    mPageProducts.setProductLatest(ps);
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

        GSONRequest<ProductLatest> GSONRequest = new GSONRequest<ProductLatest>(pUrl, ProductLatest.class, pListener, pErrorListener);
        mQueue.add(GSONRequest);
    }
    private void requestProductsTotal() {
        String pUrl = URLConfig.HOME_PRODUCT_LIST;
        Response.Listener<ProductTotal> pListener = new Response.Listener<ProductTotal>() {
            @Override
            public void onResponse(ProductTotal p) {
                if (p != null) {
                    List ps = p.result.getData();
                    Log.d(TAG, "" + ps.size());
                    mPageProducts.setProductTotal(ps);
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

        GSONRequest<ProductTotal> GSONRequest = new GSONRequest<ProductTotal>(pUrl, ProductTotal.class, pListener, pErrorListener);
        mQueue.add(GSONRequest);
    }



    private void requestHomeProductDetail() {
        String productId = "34";
        String detailUrl = URLConfig.HOME_PRODUCT_DETAIL + productId;
        Response.Listener<ProductDetail> pDetailListener = new Response.Listener<ProductDetail>() {
            @Override
            public void onResponse(ProductDetail p) {
                if (p != null) {
                    Log.d(TAG, "msg = " + p.result.msg + ", total = " + p.body.getTotal());
                    Log.d(TAG, "imgUrl = " + p.body.getRows().getImageList().get(0).imgUrl);
                }
            }
        };
        Response.ErrorListener pDetailErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage(), error);
                }
            }
        };
        GSONRequest<ProductDetail> GSONRequest = new GSONRequest<ProductDetail>(detailUrl, ProductDetail.class, pDetailListener, pDetailErrorListener);
        mQueue.add(GSONRequest);
    }

}
