package com.xgli.module_news.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.xgli.module_news.R2;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.customview.WrappedViewPager;
import com.xgli.module_news.di.component.DaggerOtherNewPhotoSetComponent;
import com.xgli.module_news.mvp.contract.OtherNewPhotoSetContract;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;
import com.xgli.module_news.mvp.presenter.OtherNewPhotoSetPresenter;

import com.xgli.module_news.R;
import com.xgli.module_news.mvp.ui.fragment.adapter.OtherNewPhotoSetAdapter;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.base.activity.BaseActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/28/2019 16:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class OtherNewPhotoSetActivity extends BaseActivity<OtherNewPhotoSetPresenter> implements OtherNewPhotoSetContract.View ,OtherNewPhotoSetAdapter.OnItemClickListener{
    @BindView(R2.id.iv_activity_other_new_photo_set_back)
     ImageView back;
    @BindView(R2.id.tv_activity_other_new_photo_index)
     TextView index;
    @BindView(R2.id.ll_activity_other_new_photo_set_bottom)
     LinearLayout bottomContainer;
    @BindView(R2.id.tv_activity_other_new_photo_set_title)
     TextView title;
    @BindView(R2.id.tv_activity_other_new_photo_set_content)
     TextView content
            ;
    @BindView(R2.id.wvp_activity_other_new_photo_set_display)
     WrappedViewPager display;

    private List<PhotoSetBean.PhotosEntity> photoSet;

    private boolean isHide=false;

//    @Inject
//    OtherNewPhotoSetAdapter otherNewPhotoSetAdapter;
    OtherNewPhotoSetAdapter otherNewPhotoSetAdapter=new OtherNewPhotoSetAdapter();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOtherNewPhotoSetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_other_new_photo_set; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }




    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                index.setText((position + 1) + "/" +photoSet.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPresenter.getOtherNewPhotoSetData(getIntent().getStringExtra(NewsUtil.PHOTO_SET_ID));
            }
        });
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    public static void start(Context context, String id) {
        Intent intent = new Intent(context, OtherNewPhotoSetActivity.class);
        intent.putExtra(NewsUtil.PHOTO_SET_ID, id);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void updateData(PhotoSetBean photoSetBean) {
        if (photoSetBean != null && photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
            photoSet = photoSetBean.getPhotos();
            List<String> imageList = new ArrayList<>();
            for (PhotoSetBean.PhotosEntity item :
                    photoSetBean.getPhotos()) {
                imageList.add(item.getImgurl());
            }
            otherNewPhotoSetAdapter.setImageList(imageList);
            otherNewPhotoSetAdapter.setOnItemClickListener(this);
            display.setAdapter(otherNewPhotoSetAdapter);
            display.setCurrentItem(0);
            content.setText(photoSetBean.getPhotos().get(0).getNote());
            title.setText(photoSetBean.getSetname());
            index.setText("1/" + photoSetBean.getPhotos().size());
        }
    }
}
