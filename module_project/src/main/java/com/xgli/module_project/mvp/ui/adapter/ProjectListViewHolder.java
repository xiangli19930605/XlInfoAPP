package com.xgli.module_project.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xgli.module_project.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author quchao
 * @date 2018/2/24
 */

public class ProjectListViewHolder extends BaseViewHolder {

    @BindView(R2.id.item_project_list_iv)
    ImageView mProjectIv;
    @BindView(R2.id.item_project_list_title_tv)
    TextView mTitleTv;
    @BindView(R2.id.item_project_list_content_tv)
    TextView mContentTv;
    @BindView(R2.id.item_project_list_time_tv)
    TextView mTimeTv;
    @BindView(R2.id.item_project_list_author_tv)
    TextView mAuthorTv;
    @BindView(R2.id.item_project_list_install_tv)
    TextView mInstallTv;

    public ProjectListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
