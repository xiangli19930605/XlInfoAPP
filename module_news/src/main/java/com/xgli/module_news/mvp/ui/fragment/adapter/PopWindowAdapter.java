package com.xgli.module_news.mvp.ui.fragment.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xgli.module_news.R;
import com.xgli.module_news.mvp.model.entity.news.OtherNewsTypeBean;

import java.util.List;


/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      16:01
 * QQ:             1981367757
 */

public class PopWindowAdapter extends BaseQuickAdapter<OtherNewsTypeBean, BaseViewHolder> {

    public PopWindowAdapter(@Nullable List data) {
        super(R.layout.item_view_fragment_index_pop, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, OtherNewsTypeBean data) {

        holder.setText(R.id.item_view_fragment_index_pop_content, data.getName().trim())
//                .setOnItemClickListener()
        ;
        if (data.getTypeId() == null
                || data.getTypeId().equals("T1348647909107")
                || data.getTypeId().equals("TYPE_DD")) {
            ((TextView) (holder.getView(R.id.item_view_fragment_index_pop_content)))
            .setTextColor(holder.itemView.getContext().getResources().getColor(R.color.gray));
        }
    }



}
