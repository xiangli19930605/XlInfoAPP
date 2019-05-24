package com.xgli.module_news.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgli.module_news.R2;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.customview.flowlayout.FlowLayout;
import com.xgli.module_news.customview.flowlayout.TagAdapter;
import com.xgli.module_news.customview.flowlayout.TagFlowLayout;
import com.xgli.module_news.di.component.DaggerSpecialNewsComponent;
import com.xgli.module_news.mvp.contract.SpecialNewsContract;
import com.xgli.module_news.mvp.model.entity.SpecialNewsBean;
import com.xgli.module_news.mvp.presenter.SpecialNewsPresenter;

import com.xgli.module_news.R;
import com.xgli.module_news.mvp.ui.fragment.adapter.SpecialNewstAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/28/2019 15:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SpecialNewsActivity extends BaseActivity<SpecialNewsPresenter> implements SpecialNewsContract.View, TagFlowLayout.OnTagClickListener {
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.normal_view)
    SmartRefreshLayout srl_smart_refresh;
    List<SpecialNewsBean> list;
    SpecialNewstAdapter mAdapter = new SpecialNewstAdapter(list);
    private String specialId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSpecialNewsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_special_news; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(getHeaderView());

        specialId = getIntent().getStringExtra(NewsUtil.SPECIAL_ID);
        srl_smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getSpecialNewsData(specialId);
            }
        });
        srl_smart_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                mPresenter.getSpecialNewsData(specialId);
            }
        });
        runOnUiThread(() -> mPresenter.getSpecialNewsData(specialId));
    }

    private ImageView banner;
    private TagFlowLayout tagFlowLayout;

    private View getHeaderView() {
        View headerView = LayoutInflater.from(this)
                .inflate(R.layout.view_activity_special_news_header, null);
        banner = headerView.findViewById(R.id.iv_view_activity_special_news_header_banner);
        tagFlowLayout = headerView.findViewById(R.id.tfl_view_activity_special_news_header_flow);
        return headerView;
    }

    public static void start(Context context, String skipID, String title) {
        Intent intent = new Intent(context, SpecialNewsActivity.class);
        intent.putExtra(NewsUtil.SPECIAL_ID, skipID);
        intent.putExtra(NewsUtil.TITLE, title);
        context.startActivity(intent);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }


    @Override
    public void updateData(List<SpecialNewsBean> list) {
        updateTag(list);
    }

    private void updateTag(List<SpecialNewsBean> list) {

        if (list != null && list.size() > 0) {
            if (list.size() == 1)
                return;
            List<String> list1 = new ArrayList<>();
            for (SpecialNewsBean bean :
                    list) {
                if (bean.getItemViewType() == SpecialNewsBean.TYPE_HEADER) {
                    list1.add(bean.getTitle());
                }
            }
            tagFlowLayout.setAdapter(new TagAdapter<String>(list1) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tag_flow_layout_item, null);
                    textView.setText(o);
                    return textView;
                }
            });
            tagFlowLayout.setOnTagClickListener(this);
        }
    }

    @Override
    public void updateBanner(String url) {
        Glide.with(this).load(url).into(banner);
    }

    @Override
    public boolean onTagClick(View view, int position,FlowLayout parent) {
        return false;
    }
}
