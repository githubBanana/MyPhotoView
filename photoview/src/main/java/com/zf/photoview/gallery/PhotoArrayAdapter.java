package com.zf.photoview.gallery;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zf.photoview.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

public class PhotoArrayAdapter<T> extends BasePagerAdapter<T> {
    public PhotoArrayAdapter(Activity act) {
        super(act);
    }

    public int getCount() {
        return this.mListData.size();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Uri uri = (Uri)this.mListData.get(position);
        View view = this.mAct.getLayoutInflater().inflate(R.layout.activity_photoview, (ViewGroup)null);
        PhotoArrayAdapter.Holder holder = new PhotoArrayAdapter.Holder(view);
        holder.setData(uri);
        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public static class Holder implements RequestListener {
        View mView;
        PhotoView mPhotoView;
        ProgressBar mProgressBar;

        public Holder(View view) {
            this.mView = view;
            this.mPhotoView = (PhotoView)this.mView.findViewById(R.id.photoView);
            this.mPhotoView.setAllowParentInterceptOnEdge(true);
            this.mProgressBar = (ProgressBar)this.mView.findViewById(R.id.progressBar1);
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity)view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }

        public void setData(Uri data) {
            this.mProgressBar.setVisibility(View.VISIBLE);
            Glide.with(this.mView.getContext()).load(data).thumbnail(0.6F).listener(this).into(this.mPhotoView);
        }

        private void showToastInThread(final CharSequence text) {
            this.mView.post(new Runnable() {
                public void run() {
                    Toast.makeText(Holder.this.mView.getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private BitmapFactory.Options imageBounds(File imageFile) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFile.getPath(), options);
            return options;
        }

        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            this.mProgressBar.setVisibility(View.GONE);
            this.showToastInThread("加载失败");
            return false;
        }

        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            this.mProgressBar.setVisibility(View.GONE);
            return false;
        }
    }
}

