package com.aile.cloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aile.cloud.R;
import com.aile.cloud.utils.AnimaUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aile.cloud.net.cache.BitmapCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIActivity extends Activity implements View.OnClickListener {
    private RequestQueue mQueue;
    private Button mDropButton;
    private Button mStringButton;
    private Button mJsonButton;
    private ImageView mImageView;

    public static void launch(Activity activity) {
        Intent i = new Intent(activity, APIActivity.class);
        AnimaUtils.showUp(activity, i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        initView();
        initListener();
        initVolley();
    }

    private void initView() {
        mDropButton = (Button) findViewById(R.id.main_drop);
        mStringButton = (Button) findViewById(R.id.string_request);
        mJsonButton = (Button) findViewById(R.id.json_request);
        mImageView = (ImageView) findViewById(R.id.main_iv);
    }

    private void initListener() {
        if (mDropButton != null) {
            mDropButton.setOnClickListener(this);
        }
        if (mStringButton != null) {
            mStringButton.setOnClickListener(this);
        }
        if (mJsonButton != null) {
            mJsonButton.setOnClickListener(this);
        }
    }

    private void initVolley() {
        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.main_drop:
//                testImageRequest();
                testImageLoader();
                break;
            case R.id.string_request:
                testStringRequest();
                break;
            case R.id.json_request:
                testJSON();
                break;
        }
    }

    private void testStringRequest() {
        String url = "https://emotao.com/miaotao/api/location/list.d?parent_id=0";
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
    }

    private void testStringRequest2() {
        String url = "https://emotao.com/miaotao/api/location/list.d?parent_id=0";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("params1", "value1");
                map.put("params2", "value2");
                return map;
            }
        };

        mQueue.add(stringRequest);
    }

    private void testJSON() {
        String url = "https://emotao.com/miaotao/api/location/list.d?parent_id=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(jsonObjectRequest);
    }


    private String[] URL = {
            "http://img5.imgtn.bdimg.com/it/u=3814375171,3829115000&fm=11&gp=0.jpg",
            "http://pic.58pic.com/58pic/13/53/30/36G58PICBcS_1024.jpg",
            "http://sc.jb51.net/uploads/allimg/150211/10-150211111432Y4.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2263713043,1871121150&fm=23&gp=0.jpg",
            "http://img06.tooopen.com/images/20170321/tooopen_sy_202673188311.jpg",
            "http://img07.tooopen.com/images/20170314/tooopen_sy_201827185379.jpg"
    };

    private void testImageRequest() {
        ImageRequest imageRequest = new ImageRequest(URL[5],
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mImageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mImageView.setImageResource(R.drawable.ic_launcher);
            }
        });

        mQueue.add(imageRequest);
    }

    private void testImageLoader() {
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache() {
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }
        });

        imageLoader.get(URL[0], new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, 400, 400);
    }

}
