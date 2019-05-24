package com.xgli.module_project.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgli.module_project.R2;
import com.xgli.module_project.di.component.DaggerProjectListComponent;
import com.xgli.module_project.mvp.contract.ProjectListContract;
import com.xgli.module_project.mvp.model.entity.ProjectListData;
import com.xgli.module_project.mvp.presenter.ProjectListPresenter;

import com.xgli.module_project.R;
import com.xgli.module_project.mvp.ui.adapter.ProjectListAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/05/2019 15:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ProjectListFragment extends BaseFragment<ProjectListPresenter> implements ProjectListContract.View {
    @BindView(R2.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    ProjectListAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    int id;
    public static ProjectListFragment newInstance(int name) {
        ProjectListFragment fragment = new ProjectListFragment();

        Bundle args = new Bundle();
        args.putInt("id", name);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerProjectListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        id=bundle.getInt("id");
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getProjectListData(id,true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                mPresenter.getProjectListData(id,false);
            }
        });
//        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getProjectListData(id,true);
    }
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mRefreshLayout.finishRefresh();
    }



    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void endLoadMore() {
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void noLoadMore() {
        mRefreshLayout.finishLoadMoreWithNoMoreData();
    }

    @Override
    public void showProjectListData(ProjectListData projectListData) {

    }
}
