package com.xgli.module_news.mvp.ui.fragment.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.xgli.module_news.R;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.mvp.model.entity.NewInfoBean;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.ui.fragment.adapter.baseitem.MultipleItem;

import java.util.List;

public class OtherNewsListAdapter extends BaseMultiItemQuickAdapter<NewInfoBean, BaseViewHolder> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public OtherNewsListAdapter(@Nullable List data) {
        super( data);
        addItemType(NewInfoBean.TYPE_NORMAL, R.layout.item_fragment_other_news_list);
        addItemType(NewInfoBean.TYPE_PHOTO, R.layout.item_fragment_other_news_list_photo);
    }



    @Override
    protected void convert(BaseViewHolder helper, NewInfoBean data) {
        helper.setText(R.id.tv_item_fragment_other_news_list_title, data.getTitle())
                .setText(R.id.tv_item_fragment_other_news_list_time, data.getMtime())
                .setText(R.id.tv_item_fragment_other_news_list_from, data.getSource())
        ;

        if (data.getItemType() == NewInfoBean.TYPE_PHOTO) {
            if (data.getImgextra() != null) {
                for (int i = 0; i < data.getImgextra().size(); i++) {
                    NewInfoBean.ImgextraEntity imgextraEntity = data.getImgextra().get(i);
                    if (i == 0) {
                        Glide.with(mContext).load(imgextraEntity.getImgsrc()).into((ImageView) helper.getView(R.id.iv_item_fragment_other_news_list_photo_one));
                    } else if (i == 1) {
                        Glide.with(mContext).load(imgextraEntity.getImgsrc()).into((ImageView) helper.getView(R.id.iv_item_fragment_other_news_list_photo_two));
                    } else if (i == 2) {
                        Glide.with(mContext).load(imgextraEntity.getImgsrc()).into((ImageView) helper.getView(R.id.iv_item_fragment_other_news_list_photo_three));
                        break;
                    }
                }
            }
        } else {
            Glide.with(mContext).load(data.getImgsrc()).into((ImageView) helper.getView(R.id.iv_item_fragment_other_news_list_display));
        }
        if (NewsUtil.SPECIAL_TITLE.equals(data.getSkipType())) {
            helper.setVisible(R.id.lv_item_fragment_other_news_list_label, true);
        } else if (NewInfoBean.TYPE_PHOTO != data.getItemType()) {
            helper.setVisible(R.id.lv_item_fragment_other_news_list_label, false);
        }
    }
}
