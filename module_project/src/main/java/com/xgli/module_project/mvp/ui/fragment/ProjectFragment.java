package com.xgli.module_project.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.xgli.module_project.R2;
import com.xgli.module_project.di.component.DaggerProjectComponent;
import com.xgli.module_project.mvp.contract.ProjectContract;
import com.xgli.module_project.mvp.model.entity.ProjectClassifyData;
import com.xgli.module_project.mvp.presenter.ProjectPresenter;

import com.xgli.module_project.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/05/2019 15:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ProjectFragment extends BaseFragment<ProjectPresenter> implements ProjectContract.View {
    @BindView(R2.id.project_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R2.id.project_divider)
    View mDivider;
    @BindView(R2.id.project_viewpager)
    ViewPager mViewPager;
    private List<ProjectClassifyData> mData;
    private List<ProjectListFragment> mFragments = new ArrayList<>();
    private int currentPage;

    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getProjectClassifyData();
    }

    @Override
    public void showProjectClassifyData(List<ProjectClassifyData> projectClassifyDataList) {


//showError();
        setChildViewVisibility(1);
        mData = projectClassifyDataList;
        initViewPagerAndTabLayout();
//        showNormal();
    }

    private void initViewPagerAndTabLayout() {
        for (ProjectClassifyData data : mData) {
            ProjectListFragment projectListFragment = ProjectListFragment.newInstance(data.getId());
            mFragments.add(projectListFragment);
        }
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mData == null ? 0 : mData.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mData.get(position).getName();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    private void setChildViewVisibility(int visibility) {
        mTabLayout.setVisibility(visibility);
        mDivider.setVisibility(visibility);
        mViewPager.setVisibility(visibility);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void initEventAndData() {

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
}
