package com.zf.photoview.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zf.photoview.R;

public class PhotoViewActivity extends Activity {
    public static final String EXTRA_PHOTOARRAY = "photo_array";
    public static final String EXTRA_PHOTOARRAY_START_INDEX = "photo_array_startindex";
    private Uri[] mPhotoArray;
    private int startIndex;
    Activity mAct;

    public PhotoViewActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        this.mAct = this;
        if(getIntent().hasExtra("photo_array")) {
            Parcelable[] parcelable = getIntent().getParcelableArrayExtra("photo_array");
            mPhotoArray = new Uri[parcelable.length];
            System.arraycopy(parcelable, 0, mPhotoArray, 0, parcelable.length);
            startIndex = getIntent().getIntExtra("photo_array_startindex", 0);
            if(startIndex >= mPhotoArray.length || startIndex < 0) {
                startIndex = 0;
            }

            showPhotoArray();
        } else if(getIntent().getData() != null) {
            showNormalImage(getIntent().getData());
        }

    }

    public static void startActivity(Context context, int startIndex, Uri... paths) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        if(paths != null) {
            if(paths.length == 1) {
                intent.setData(paths[0]);
            } else if(paths.length > 1) {
                intent.putExtra("photo_array", paths);
                intent.putExtra("photo_array_startindex", startIndex);
            }
        }

        context.startActivity(intent);
    }

    private void showNormalImage(Uri path) {
        View view = mAct.getLayoutInflater().inflate(R.layout.activity_photoview, (ViewGroup)null);
        PhotoArrayAdapter.Holder holder = new PhotoArrayAdapter.Holder(view);
        holder.setData(path);
        setContentView(view);
    }

    private void showPhotoArray() {
        FrameLayout fl = new FrameLayout(mAct);
//        fl.setBackgroundColor(-16777216);
        PhotoViewPager viewPager = new PhotoViewPager(mAct);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2, 17);
        fl.addView(viewPager, params);
        final TextView tvIndex = new TextView(mAct);
        tvIndex.setText(startIndex + 1 + "/" + mPhotoArray.length);
        tvIndex.setTextColor(-1);
        params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = 81;
        params.setMargins(0, 0, 0, dip2px(mAct, 60));
        fl.addView(tvIndex, params);
        setContentView(fl);
        PhotoArrayAdapter<Uri> adapter = new PhotoArrayAdapter<Uri>(mAct);
        adapter.setListData(mPhotoArray);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(startIndex);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                tvIndex.setText(arg0 + 1 + "/" + mPhotoArray.length);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private int dip2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(density * (float)dip + 0.5F);
    }

    private void showToast(CharSequence text) {
        Toast.makeText(mAct, text, Toast.LENGTH_SHORT).show();
    }

    private void showToastInThread(final CharSequence text) {
        runOnUiThread(new Runnable() {
            public void run() {
                showToast(text);
            }
        });
    }
}

