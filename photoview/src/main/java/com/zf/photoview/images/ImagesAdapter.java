package com.zf.photoview.images;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zf.photoview.gallery.PhotoViewActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ImagesAdapter extends BAdapter<Uri> implements View.OnClickListener {
    int itemWidth;
    int itemHeigth;
    int currentIndex;

    public ImagesAdapter(Activity act) {
        super(act);
    }

    public ImagesAdapter(Activity act, int itemWidth, int itemHeigth) {
        super(act);
        this.itemWidth = itemWidth;
        this.itemHeigth = itemHeigth;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        this.currentIndex = i;
        ImageView view1 = new ImageView(this.mAct) {
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
                int childWidthSize = this.getMeasuredWidth();
                int childHeightSize = this.getMeasuredHeight();
                heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);

                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        Uri url = (Uri)this.getItem(i);
        ImageView iv = (ImageView)view1;
        iv.setOnClickListener(this);
        iv.setTag(currentIndex, Integer.valueOf(i));
        if(this.itemHeigth == 0) {
            this.itemHeigth = -2;
        }

        if(this.itemWidth == 0) {
            this.itemWidth = -2;
        }

        iv.setLayoutParams(new AbsListView.LayoutParams(this.itemWidth, this.itemHeigth));
        Glide.with(this.mAct).load(url).placeholder(17301522).thumbnail(0.6F).override(300, 300).into(iv);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return view1;
    }

    public void onClick(View v) {
        int startIndex = ((Integer)v.getTag(currentIndex)).intValue();
        Uri[] uris = new Uri[this.mListData.size()];
        this.mListData.toArray(uris);
        PhotoViewActivity.startActivity(this.mAct, startIndex, uris);
    }

    public void setListData(List<String> list) {
        ArrayList uriList = new ArrayList();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            String str = (String)var3.next();
            uriList.add(Uri.parse(str));
        }

        this.setListData((Collection)uriList);
    }
}

