package com.xgli.module_project.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xgli.module_project.R;
import com.xgli.module_project.mvp.model.entity.FeedArticleData;

import java.util.List;


/**
 * @author quchao
 * @date 2018/2/24
 */
public class ProjectListAdapter extends BaseQuickAdapter<FeedArticleData, ProjectListViewHolder> {

    public ProjectListAdapter(int layoutResId, @Nullable List<FeedArticleData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(ProjectListViewHolder helper, FeedArticleData item) {
        if (!TextUtils.isEmpty(item.getEnvelopePic())) {
//            ImageLoader.load(mContext, item.getEnvelopePic(), helper.getView(R.id.item_project_list_iv));
         ImageView img= helper.getView(R.id.item_project_list_iv);
            Glide.with(helper.itemView.getContext())
                    .load(item.getEnvelopePic())
//                    .placeholder(R.drawable.leak_canary_icon)
//                    .error(R.drawable.leak_canary_icon)
                    .into(img);
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.item_project_list_title_tv, item.getTitle());
        }
        if (!TextUtils.isEmpty(item.getDesc())) {
            helper.setText(R.id.item_project_list_content_tv, item.getDesc());
        }
        if (!TextUtils.isEmpty(item.getNiceDate())) {
            helper.setText(R.id.item_project_list_time_tv, item.getNiceDate());
        }
        if (!TextUtils.isEmpty(item.getAuthor())) {
            helper.setText(R.id.item_project_list_author_tv, item.getAuthor());
        }
        if (!TextUtils.isEmpty(item.getApkLink())) {
            helper.getView(R.id.item_project_list_install_tv).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.item_project_list_install_tv).setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.item_project_list_install_tv);
    }
}
