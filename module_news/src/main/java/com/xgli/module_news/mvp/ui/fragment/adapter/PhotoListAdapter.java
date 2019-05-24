package com.xgli.module_news.mvp.ui.fragment.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.xgli.module_news.R;
import com.xgli.module_news.mvp.model.entity.PictureBean;

import java.util.List;

public class PhotoListAdapter extends BaseQuickAdapter<PictureBean.PictureEntity, BaseViewHolder> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public PhotoListAdapter(@Nullable List data) {
        super(R.layout.item_fragment_photo_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PictureBean.PictureEntity data) {

        helper.setText(R.id.tv_item_fragment_photo_list_description, data.getDesc()).addOnClickListener(R.id.iv_item_fragment_photo_list_picture);
        Glide.with(mContext).load(data.getUrl()).into((ImageView) helper.getView(R.id.iv_item_fragment_photo_list_picture));
    }


}