package com.xgli.module_news.mvp.ui.fragment.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.jess.arms.base.BaseApplication;
import com.xgli.module_news.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      17:05
 * QQ:             1981367757
 */

public class OtherNewPhotoSetAdapter extends PagerAdapter {
    private List<String> imageList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setImageList(List<String> imageList) {
        if (imageList != null) {
            this.imageList = imageList;
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_activity_other_new_photo_set, null);
        PhotoView photoView = view.findViewById(R.id.pv_item_activity_other_new_photo_set_display);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
        Glide.with(container.getContext()).load(imageList.get(position)).into(photoView);
        container.addView(view);
        return view;
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
