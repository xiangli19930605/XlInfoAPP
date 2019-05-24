package com.xgli.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.xgli.module_main.R2;
import com.xgli.module_main.di.component.DaggerHomeComponent;
import com.xgli.module_main.mvp.contract.HomeContract;
import com.xgli.module_main.mvp.presenter.HomePresenter;

import com.xgli.module_main.R;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;
import me.jessyan.armscomponent.commonsdk.data.UserManager;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/16/2019 14:55
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {
//    @BindView(R2.id.toolbar)
//     Toolbar mToolbar;
//    @BindView(R2.id.toolbar_header_view)
//    HeaderView mHeaderView;

    @BindView(R2.id.tv)
    TextView tv;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initializeToolbar();
    }
    private void initializeToolbar() {
//        mHeaderView.bindTo(getString(R.string.app_name), "666");
        //mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        // Toolbar will now take on default Action Bar characteristics
//        ((AppCompatActivity)_mActivity). setSupportActionBar(mToolbar);
//        _mActivity.setActionBar(mToolbar);


        tv.setText( UserManager.getCurrentUser().getName());
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
    protected void initEventAndData() {

    }
}
