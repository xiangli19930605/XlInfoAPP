package com.xgli.module_news.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.xgli.module_news.R2;
import com.xgli.module_news.app.utils.FileUtil;
import com.xgli.module_news.customview.CustomPopWindow;
import com.xgli.module_news.customview.WrappedViewPager;
import com.xgli.module_news.di.component.DaggerNewsComponent;
import com.xgli.module_news.mvp.contract.NewsContract;
import com.xgli.module_news.mvp.model.entity.news.OtherNewsTypeBean;
import com.xgli.module_news.mvp.presenter.NewsPresenter;

import com.xgli.module_news.R;
import com.xgli.module_news.mvp.ui.fragment.adapter.PopWindowAdapter;
import com.xgli.module_news.mvp.ui.fragment.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/26/2019 14:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View {
    @BindView(R2.id.tl_fragment_tab)
    TabLayout tabLayout;
    @BindView(R2.id.vp_fragment_index_display)
    WrappedViewPager display;
    @BindView(R2.id.iv_fragment_index_expend_list)
    ImageView expend;

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private PopWindowAdapter popWindowAdapter;
    private ViewPagerAdapter viewPagerAdapter;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerNewsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initFragment();
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.setTitleAndFragments(titleList, fragmentList);
        tabLayout.setupWithViewPager(display);
        display.setAdapter(viewPagerAdapter);
    }

    private void initFragment() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        List<OtherNewsTypeBean> result;
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(FileUtil.readData(_mActivity, "NewsChannel")).getAsJsonArray();
        result = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement item :
                jsonElements) {
            OtherNewsTypeBean bean = gson.fromJson(item, OtherNewsTypeBean.class);
            bean.setHasSelected(true);
            result.add(bean);
        }

        List<OtherNewsTypeBean> tempList = new ArrayList<>();
        List<OtherNewsTypeBean> otherList = new ArrayList<>();

        for (OtherNewsTypeBean bean :
                result) {
            if (bean.getName().equals("福利") || bean.getName().equals("头条")
            ) {
                tempList.add(bean);
            } else {
                otherList.add(bean);
            }
        }
        otherList.addAll(0, tempList);
        for (OtherNewsTypeBean bean :
                otherList) {
            titleList.add(bean.getName());
            if (TextUtils.isEmpty(bean.getTypeId())) {
                fragmentList.add(PhotoListFragment.newInstance());
            } else {
                fragmentList.add(OtherNewsListFragment.newInstance(bean));
            }
        }

        for (String title :
                titleList) {
            OtherNewsTypeBean item = new OtherNewsTypeBean();
            item.setName(title);
            popList.add(item);
        }
    }

    private CustomPopWindow customPopWindow;


    @OnClick({R2.id.iv_fragment_index_expend_list})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_fragment_index_expend_list) {
            if (customPopWindow == null) {
                customPopWindow = new CustomPopWindow.Builder().contentView(getContentView())
                        .build();
            }


            popWindowAdapter.replaceData(popList);
            if (!customPopWindow.isShowing()) {
                customPopWindow.showAsDropDown(tabLayout);
            } else {
                customPopWindow.dismiss();
            }

        }
    }
    List<OtherNewsTypeBean> popList = new ArrayList<>();
    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_index_pop_window, null);
        Button adjust = contentView.findViewById(R.id.btn_view_fragment_index_pop_adjust);
        final RecyclerView display = contentView.findViewById(R.id.recyclerView);
        display.setLayoutManager(new GridLayoutManager(getContext(), 4));
        popWindowAdapter = new PopWindowAdapter(popList);
        display.setAdapter(popWindowAdapter);
        popWindowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsFragment.this.display.setCurrentItem(position);
                customPopWindow.dismiss();
            }
        });
        return contentView;
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
