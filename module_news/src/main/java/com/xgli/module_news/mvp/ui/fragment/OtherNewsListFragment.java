package com.xgli.module_news.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.xgli.module_news.di.component.DaggerOtherNewsListComponent;
import com.xgli.module_news.mvp.contract.OtherNewsListContract;
import com.xgli.module_news.mvp.model.entity.NewInfoBean;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.model.entity.news.OtherNewsTypeBean;
import com.xgli.module_news.mvp.presenter.OtherNewsListPresenter;

import com.xgli.module_news.R;
import com.xgli.module_news.mvp.ui.activity.OtherNewPhotoSetActivity;
import com.xgli.module_news.mvp.ui.activity.OtherNewsDetailActivity;
import com.xgli.module_news.mvp.ui.activity.SpecialNewsActivity;
import com.xgli.module_news.mvp.ui.fragment.adapter.OtherNewsListAdapter;

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
public class OtherNewsListFragment extends BaseFragment<OtherNewsListPresenter> implements OtherNewsListContract.View {

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.normal_view)
    SmartRefreshLayout srl_smart_refresh;
    @Inject
    OtherNewsListAdapter mAdapter;
    @Inject
    List<NewInfoBean> mList;

    private OtherNewsTypeBean otherNewsTypeBean;
    public static OtherNewsListFragment newInstance(OtherNewsTypeBean otherNewsTypeBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", otherNewsTypeBean);
        OtherNewsListFragment otherNewsListFragment = new OtherNewsListFragment();
        otherNewsListFragment.setArguments(bundle);
        return otherNewsListFragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerOtherNewsListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_news_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        otherNewsTypeBean = getArguments().getParcelable("item");
        initRecyclerView();
    }
    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mAdapter);
        srl_smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getNewsList(true,otherNewsTypeBean.getTypeId());
            }
        });
        srl_smart_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                mPresenter.getNewsList(false,otherNewsTypeBean.getTypeId());
            }
        });
//        srl_smart_refresh.autoRefresh();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewInfoBean newInfoBean = (NewInfoBean) adapter.getData().get(position);
                if (NewsUtil.SPECIAL_TITLE.equals(newInfoBean.getSkipType())) {
                    SpecialNewsActivity.start(getContext(), newInfoBean.getSpecialID(), newInfoBean.getTitle());
                } else if (NewsUtil.PHOTO_SET.equals(newInfoBean.getSkipType())) {
                    OtherNewPhotoSetActivity.start(getContext(), newInfoBean.getPhotosetID());
                } else {
                    OtherNewsDetailActivity.start(getContext(), TextUtils.isEmpty(newInfoBean.getPostid())?newInfoBean.getSkipID():newInfoBean.getPostid());
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
        mPresenter.getNewsList(true,otherNewsTypeBean.getTypeId());
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
    public void killMyself() {

    }

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
