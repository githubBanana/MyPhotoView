package com.xs.myphotoview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zf.photoview.images.SelectImageFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SelectImageFragment mFmSelectImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.text).setOnClickListener(this);
        mFmSelectImg = new SelectImageFragment();
        mFmSelectImg.setAllSelectImagesCount(6).setMultiSelect(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_select_img,mFmSelectImg).commit();

    }


    @Override
    public void onClick(View view) {
        if (true) {
            List<String> list = mFmSelectImg.getSelectImages();
            for (String str :
                    list) {
                Log.e("info", "onClick: "+str );
            }
        }
    }
}
