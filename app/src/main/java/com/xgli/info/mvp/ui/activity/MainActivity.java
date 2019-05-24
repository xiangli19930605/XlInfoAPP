package com.xgli.info.mvp.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xgli.info.R;
import com.xgli.info.app.LocationEvent;
import com.xgli.info.app.WeatherInfoBean;
import com.xgli.info.app.manage.NewLocationManager;
import com.xgli.info.di.component.DaggerMainComponent;
import com.xgli.info.mvp.contract.MainContract;
import com.xgli.info.mvp.presenter.MainPresenter;
import com.xgli.info.mvp.ui.fragment.MainFragment;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.activity.BaseActivity;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.Constants;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.rxbus.RxBusManager;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.yokeyword.fragmentation.ISupportFragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/18/2019 10:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.MAIN_MAINEACTIVITY)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    //    private HeaderView mHeaderView;
    TextView tvCity;
    TextView tvWeather;
    private WeatherInfoBean mWeatherInfoBean = new WeatherInfoBean();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        initializeToolbar();
        initializeDrawer();
        if (findFragment(MainFragment.class) == null) {

            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
            String page = getIntent().getStringExtra(EventBusTags.JUMP_PAGE);
            if (page != null && page.endsWith(EventBusTags.THREE)) {
                EventBusUtils.sendEvent(new Event(EventBusTags.THREE), EventBusTags.JUMP_PAGE);
            }
        }

        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                             @Override
                                             public void onRequestPermissionSuccess() {
//                ToastUtil.showToast( "tongyi");
                                             }

                                             @Override
                                             public void onRequestPermissionFailure(List<String> permissions) {
                                                 ToastUtil.showToast("拒绝权限，等待下次询问哦");
                                             }

                                             @Override
                                             public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                                                 ToastUtil.showToast("拒绝权限，不再弹出询问框，请前往APP应用设置中打开此权限");
                                             }
                                         }, new RxPermissions(this), mErrorHandler, Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        startSearchLiveWeather(MyApplication.mComponent.getAppDataManager().getString(Constants.CITY));

        addDisposable(RxBusManager.getInstance().registerEvent(LocationEvent.class, locationEvent -> {
            if (!locationEvent.getCity().equals(mWeatherInfoBean.getCity())) {
                startSearchLiveWeather(locationEvent.getCity());
            }
        }));
    }

    private void startSearchLiveWeather(String city) {
        if (city == "") {
        NewLocationManager.getInstance().startLocation();
            return;
        }
        mWeatherInfoBean.setCity(city);
        WeatherSearchQuery query = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch weatherSearch = new WeatherSearch(this);
        weatherSearch.setQuery(query);
        weatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                if (i == 1000) {
                    if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive localWeatherLive = localWeatherLiveResult.getLiveResult();
                        mWeatherInfoBean.setRealTime(localWeatherLive.getReportTime());
                        mWeatherInfoBean.setWeatherStatus(localWeatherLive.getWeather());
                        mWeatherInfoBean.setTemperature(localWeatherLive.getTemperature() + "°");
                        mWeatherInfoBean.setWind(localWeatherLive.getWindDirection() + "风       " + localWeatherLive
                                .getWindPower() + "级");
                        mWeatherInfoBean.setHumidity("湿度       " + localWeatherLive.getHumidity() + "%");
                        tvCity.setText(mWeatherInfoBean.getCity());
                        tvWeather.setText(mWeatherInfoBean.getTemperature());
                    } else {
                        LogUtils.e("获取到的天气信息为空");
                    }
                } else {
                    LogUtils.e("获取到的天气信息失败" + i);
                }
            }
            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                if (i == 1000) {
                    if (localWeatherForecastResult != null && localWeatherForecastResult.getForecastResult() != null
                            && localWeatherForecastResult.getForecastResult().getWeatherForecast() != null
                            && localWeatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                        LocalWeatherForecast localWeatherForecast = localWeatherForecastResult.getForecastResult();
                        mWeatherInfoBean.setForecastTime(localWeatherForecast.getReportTime());
                        mWeatherInfoBean.setForecastInfoList(localWeatherForecast.getWeatherForecast());
                    } else {
                        LogUtils.e("查询不到天气预报的" +
                                "结果");
                    }
                } else {
                    LogUtils.e("查询天气预报的结果失败" + i);
                }
            }
        });
        weatherSearch.searchWeatherAsyn();
    }

    @Inject
    RxErrorHandler mErrorHandler;
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        ISupportFragment topFragment = getTopFragment();
        // 主页的Fragment
        if (topFragment instanceof MainFragment) {
            //中有当前fragment在MainFragment时通知界面跳转
            if (MainFragment.newInstance().getCurrent() == 1 || MainFragment.newInstance().getCurrent() == 2 || MainFragment.newInstance().getCurrent() == 3) {
                EventBusUtils.sendEvent(new Event(EventBusTags.ZERO), EventBusTags.JUMP_PAGE);
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出系统", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


//    private void initializeToolbar() {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
//        mHeaderView.bindTo(getString(R.string.app_name), "666");
//        //mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        // Toolbar will now take on default Action Bar characteristics
//        setSupportActionBar(mToolbar);
//    }

    private void initializeDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.app_name, R.string.app_name);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Version
        TextView appVersion = mNavigationView.getHeaderView(0).findViewById(R.id.app_version);
         tvCity = mNavigationView.getHeaderView(0).findViewById(R.id.tv_city);
        tvWeather = mNavigationView.getHeaderView(0).findViewById(R.id.tv_weather);

//        appVersion.setText(getString(R.string.about_version, Utils.getVersionName(this)));
    }

    public void openDrawer() {
        mDrawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_overall) {
        } else if (id == R.id.nav_selection_modes) {
        } else if (id == R.id.nav_about) {
        }
        return false;
    }
}
