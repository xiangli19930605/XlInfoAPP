package com.xgli.module_news.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgli.module_news.R2;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.di.component.DaggerPhotoListComponent;
import com.xgli.module_news.mvp.contract.PhotoListContract;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.presenter.PhotoListPresenter;

import com.xgli.module_news.R;
import com.xgli.module_news.mvp.ui.fragment.adapter.PhotoListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/17/2019 13:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class PhotoListFragment extends BaseFragment<PhotoListPresenter> implements PhotoListContract.View {
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.normal_view)
    SmartRefreshLayout srl_smart_refresh;

    @Inject
    PhotoListAdapter mAdapter;
    @Inject
    List<PictureBean.PictureEntity> mList;
    public static PhotoListFragment newInstance() {
        PhotoListFragment fragment = new PhotoListFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerPhotoListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, new GridLayoutManager(getActivity(), 2));

        mRecyclerView.setAdapter(mAdapter);
        srl_smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.requestGirls(true);
            }
        });
        srl_smart_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                mPresenter.requestGirls(false);
            }
        });
//        srl_smart_refresh.autoRefresh();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PictureBean.PictureEntity> imageList = adapter.getData();
                if (imageList != null && imageList.size() > 0) {
                    ArrayList<String> result = new ArrayList<>();
                    for (PictureBean.PictureEntity item :
                            imageList) {
                        result.add(item.getUrl());
                    }

//                    ImagePreViewActivity.start(getActivity(), result, position, view, NewsUtil.PHOTO_LIST_FLAG);

            }

            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        srl_smart_refresh.finishRefresh();
    }

    @Override
    protected void initEventAndData() {
        mPresenter.requestGirls(true);
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        srl_smart_refresh.finishLoadMore();
    }

    @Override
    public void noLoadMore() {
        srl_smart_refresh.finishLoadMoreWithNoMoreData();
    }
}
