package com.zf.photoview.images;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.zf.photoview.R;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class SelectImageFragment extends Fragment {
    private GridView mGridView;
    private SelectImageFragment.SelectAdapter mAdapter;
    private int allSelectImagesCount = 9;
    private int singelSelectImageCount;
    private boolean isMultiSelect;
    private boolean isShowCamera;
    public static final int REQUEST_SELECT_IMAGE = 15;
    public static final String PRE_KEY_IMAGES = "selects_images";

    public SelectImageFragment() {
        this.singelSelectImageCount = this.allSelectImagesCount;
        this.isMultiSelect = true;
        this.isShowCamera = true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_select_gv, container, false);
        this.mGridView = (GridView)view.findViewById(R.id.gridView);
        this.initView();
        return view;
    }

    private void initData() {
        this.mAdapter = new SelectImageFragment.SelectAdapter(this.getActivity());
    }

    private void initView() {
        this.mGridView.setNumColumns(3);
        int dp4 = this.dip2px(this.getActivity(), 4);
        this.mGridView.setPadding(dp4, 0, dp4, 0);
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridView.setGravity(17);
    }

    public int dip2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)((double)(density * (float)dip) + 0.5D);
    }

    public List<String> getSelectImages() {
        return this.mAdapter.mListData;
    }

    public SelectImageFragment.SelectAdapter getAdapter() {
        return this.mAdapter;
    }

    /** @deprecated */
    @Deprecated
    public SelectImageFragment setSelectImagesCount(int selectImagesCount) {
        this.allSelectImagesCount = selectImagesCount;
        return this;
    }

    public SelectImageFragment setMultiSelect(boolean multiSelect) {
        this.isMultiSelect = multiSelect;
        return this;
    }

    public SelectImageFragment setShowCamera(boolean showCamera) {
        this.isShowCamera = showCamera;
        return this;
    }

    public SelectImageFragment setSingelSelectImageCount(int singelSelectImageCount) {
        this.singelSelectImageCount = singelSelectImageCount;
        return this;
    }

    public SelectImageFragment setAllSelectImagesCount(int allSelectImagesCount) {
        this.allSelectImagesCount = allSelectImagesCount;
        return this;
    }

    public int getSelectImagesCount() {
        return this.allSelectImagesCount;
    }

    @SuppressLint("NewApi")
    public void setGridViewHeight(GridView gridView) {
        ListAdapter adapter = gridView.getAdapter();
        if(adapter != null) {
            int gridViewHeight = 0;
            int numColumns = gridView.getNumColumns();
            int count = adapter.getCount();
            int var10000;
            if(count % numColumns == 0) {
                var10000 = count / numColumns;
            } else {
                var10000 = count / numColumns + 1;
            }

            for(int i = 0; i < count; i += numColumns) {
                View view = adapter.getView(i, (View)null, gridView);
                view.measure(0, 0);
                int height = view.getMeasuredHeight();
                gridViewHeight += height + gridView.getVerticalSpacing();
                Log.e("tag", "gridViewHeight=" + gridViewHeight + "  count=" + count + "  height=" + height);
            }

            gridView.getLayoutParams().height = gridViewHeight;
            gridView.setLayoutParams(gridView.getLayoutParams());
        }
    }

    public static void setListViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if(listAdapter != null) {
            int col = gridView.getNumColumns();
            int totalHeight = 0;

            for(int params = 0; params < listAdapter.getCount(); params += col) {
                View listItem = listAdapter.getView(params, (View)null, gridView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params1 = gridView.getLayoutParams();
            params1.height = totalHeight;
            ((ViewGroup.MarginLayoutParams)params1).setMargins(10, 10, 10, 10);
            gridView.setLayoutParams(params1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 15 && resultCode == -1) {
            ArrayList paths = data.getStringArrayListExtra("select_result");
            this.mAdapter.addListData(paths);
            this.setGridViewHeight(this.mGridView);
        }

    }

    public class SelectAdapter extends BAdapter<String> implements View.OnClickListener {
        public SelectAdapter(Activity act) {
            super(act);
        }

        public int getCount() {
            int count = super.getCount();
            return count == SelectImageFragment.this.getSelectImagesCount()?SelectImageFragment.this.getSelectImagesCount():count + 1;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            view = this.mInflater.inflate(R.layout.layout_sel_img_item, viewGroup, false);
            ImageView iv = (ImageView)view.findViewById(R.id.iv);
            ImageView iv_delete = (ImageView)view.findViewById(R.id.iv_del);
            iv_delete.setOnClickListener(this);
            if(i == this.mListData.size()) {
                iv.setImageResource(R.mipmap.icon_addpic_unfocused);
                iv.setOnClickListener(this);
                iv_delete.setVisibility(View.INVISIBLE);
                return view;
            } else {
                String data = (String)this.getItem(i);
                iv_delete.setTag(data);
                iv_delete.setVisibility(View.VISIBLE);
                iv.setOnClickListener((View.OnClickListener)null);
                String uri = "file://" + data;
                Log.e("", "uri----" + uri);
                Glide.with(SelectImageFragment.this).load(uri).centerCrop().into(iv);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return view;
            }
        }

        public void onClick(View v) {
            int i = v.getId();
            if(i == R.id.iv) {
                int data = SelectImageFragment.this.allSelectImagesCount - this.mListData.size();
                this.selImage(data);
            } else if(i == R.id.iv_del) {
                String data1 = (String)v.getTag();
                this.removeListData(data1);
            }

        }

        private void selImage(int singelSelectImageCount) {
            Intent intent = new Intent(SelectImageFragment.this.getActivity(), MultiImageSelectorActivity.class);
            intent.putExtra("show_camera", SelectImageFragment.this.isShowCamera);
            intent.putExtra("max_select_count", singelSelectImageCount);
            intent.putExtra("select_count_mode", SelectImageFragment.this.isMultiSelect?1:0);
            SelectImageFragment.this.startActivityForResult(intent, 15);
        }
    }
}

