package com.aile.cloud.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aile.cloud.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button mDropButton;

    private ImageView mImageView;

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
                loadImageView();
                break;
        }
    }

    private void loadImageView() {
        String internetUrl = "http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png";
        Glide.with(this)
                .load(internetUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.ic_account_circle_grey600_24dp)
//                .error(R.drawable.ic_account_circle_grey600_24dp)
                .centerCrop()
                .crossFade()
                .into(mImageView);
    }

}
