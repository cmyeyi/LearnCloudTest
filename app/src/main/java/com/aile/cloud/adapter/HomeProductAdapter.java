package com.aile.cloud.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aile.cloud.R;
import com.aile.cloud.net.bean.MTProduct;
import com.aile.www.basesdk.utils.DimensionUtils;
import com.aile.www.basesdk.utils.ScreenUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class HomeProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<MTProduct> data;
    private Context mContext;
    private int mImageWidth;

    //自定义监听事件
    interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public HomeProductAdapter(Context context, List<MTProduct> items) {
        this.mContext = context;
        data = items;
        int width = ScreenUtils.getScreenW(context);
        int gap = DimensionUtils.dpToPx(context, 1);
        mImageWidth = width / 2;
    }

    private void bindImage(final ImageView imageView, String imageURl) {
        try {
            ImageRequest imageRequest = new ImageRequest(imageURl,
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View viewRoot = LayoutInflater.from(mContext).inflate(R.layout.layout_product, null);
            ViewHolder holder = new ViewHolder(viewRoot);
            //给布局设置点击和长点击监听
            viewRoot.setOnClickListener(this);
            viewRoot.setOnLongClickListener(this);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            MTProduct p = data.get(position);
            if (null != p) {
                String imageUrl = p.getIcon_url();
                if (!TextUtils.isEmpty(imageUrl)) {
                    ImageView imageView = ((ViewHolder) holder).imageView;
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(mImageWidth, mImageWidth * 2 / 3));
                    bindImage(imageView, imageUrl);
                }

                if (!TextUtils.isEmpty(p.getName())) {
                    TextView desView = ((ViewHolder) holder).desView;
                    desView.setText(p.getName());
                }

                String progressNum = String.valueOf((p.getSellTotal() / p.getTotal()) * 100) + "%";
                if (!TextUtils.isEmpty(progressNum)) {
                    TextView progressView = ((ViewHolder) holder).progressView;
                    progressView.setText(progressNum);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView desView;
        private TextView progressView;


        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.product_image_iv);
            desView = (TextView) view.findViewById(R.id.product_text);
            progressView = (TextView) view.findViewById(R.id.progress_number_tv);
        }
    }
}
