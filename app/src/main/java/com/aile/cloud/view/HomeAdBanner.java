package com.aile.cloud.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.aile.cloud.R;
import com.aile.cloud.ad.IADBanner;
import com.aile.cloud.net.cache.BitmapCache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class HomeAdBanner implements View.OnClickListener {

    private Context mContext;
    private ViewPager mViewPager;
    private View mContainer;
    private ViewGroup mRootView;
    private BannerPagerAdapter mAdapter;
    private static final long ONE_DAY = 24L * 60 * 60 * 1000;
    private static final long INTERVAL = 4500L;
    private List mData;

    public HomeAdBanner(Context context, ViewGroup rootView) {
        mContext = context;
        mRootView = rootView;

        init(rootView);
    }

    public HomeAdBanner(Context context, ViewGroup rootView, BannerReportTracker tracker) {
        mContext = context;
        mRootView = rootView;
        init(rootView);
    }

    public View getRootView() {
        return mRootView;
    }

    public void reset() {
        mViewPager.removeCallbacks(mAutoPlayTask);
        mRootView.removeView(mContainer);
        mRootView.setVisibility(View.VISIBLE);
        init(mRootView);
    }

    private void init(ViewGroup rootView) {
        mContainer = View.inflate(mContext, R.layout.layout_home_banner, null);
        rootView.addView(mContainer, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        mContainer.setVisibility(View.GONE);
        mViewPager = (ViewPager) mContainer.findViewById(R.id.view_pager);
        mAdapter = new BannerPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        setOnPageChangeListener();
    }

    public void setOnPageChangeListener() {
        if(mViewPager != null) {
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageSelected(int arg0) {
                    if (mAdapter == null || mAdapter.getCount() < 2) {
                        return;
                    }
                    if (mData != null && mData.size() != 1) {
                        mViewPager.removeCallbacks(mAutoPlayTask);
                        mViewPager.postDelayed(mAutoPlayTask, INTERVAL);
                    }
                }
            });
        }
    }

    public void removeOnPageChangeListener() {
        if(mViewPager != null) {
            mViewPager.addOnPageChangeListener(null);
        }
    }

    @Override
    public void onClick(View v) {

    }


    private class BannerPagerAdapter extends PagerAdapter {
        private List<View> views;

        public void setData(List list) {
            if (list == null) {
                views = null;
                return;
            }

            List<View> viewList = new ArrayList<View>();
            for (int i = 0; i < list.size(); i++) {
                IADBanner banner = (IADBanner) list.get(i);
                View itemView = getView(banner);
                viewList.add(itemView);
            }

            if (list.size() != 1) {
                for (int i = 0; i < list.size(); i++) {
                    IADBanner banner = (IADBanner) list.get(i);
                    View itemView = getView(banner);
                    viewList.add(itemView);
                }
            }

            views = viewList;
        }

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            } else if (views.size() == 1) {
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View item = views.get(position % views.size());
            if (item.getParent() != null) {
                container.removeView(item);
            }
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    private View getView(IADBanner banner) {

        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setTag(banner);
        imageView.setOnClickListener(this);
        String imageUrl = banner.getBigImgUrl();
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.ic_launcher);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(imageRequest);

        return imageView;
    }

    private BannerAutoPlayTask mAutoPlayTask = new BannerAutoPlayTask();

    private class BannerAutoPlayTask implements Runnable {

        @Override
        public void run() {
            int nextCount = mViewPager.getCurrentItem() + 1;
            mViewPager.setCurrentItem(nextCount, true);

        }

    }


    public void setData(List data) {
        mData = data;
        if (data == null || data.size() == 0) {
            mContainer.setVisibility(View.GONE);
            mAdapter.setData(null);
        } else {
            mViewPager.removeAllViews();
            mContainer.setVisibility(View.VISIBLE);
            mAdapter.setData(mData);
            mAdapter.notifyDataSetChanged();
            if (mData != null && mData.size() != 1) {
                mViewPager.setCurrentItem(mData.size() * 10, false);
                mViewPager.postDelayed(mAutoPlayTask, INTERVAL);
            }
        }

    }

    public void hideBanner() {
        if (mRootView != null) {
            mRootView.setVisibility(View.GONE);
        }
    }

    public void showBanner() {
        if (mRootView != null) {
            mRootView.setVisibility(View.VISIBLE);
        }
    }

    public interface BannerReportTracker {
        /**
         * 点击
         *
         * @param banner
         */
        void onBannerClick(IADBanner banner);

        /**
         * 第一次展示
         *
         * @param banner
         */
        void onBannerShow(IADBanner banner);
    }

}
