package com.xgli.module_news.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.xgli.module_news.R2;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.customview.ToolBarOption;
import com.xgli.module_news.customview.rich.RichText;
import com.xgli.module_news.di.component.DaggerOtherNewsDetailComponent;
import com.xgli.module_news.mvp.contract.OtherNewsDetailContract;
import com.xgli.module_news.mvp.model.entity.OtherNewsDetailBean;
import com.xgli.module_news.mvp.presenter.OtherNewsDetailPresenter;

import com.xgli.module_news.R;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/30/2019 10:02
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class OtherNewsDetailActivity extends BaseActivity<OtherNewsDetailPresenter> implements OtherNewsDetailContract.View {
    @BindView(R2.id.tv_activity_other_news_detail_content)
     RichText content;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOtherNewsDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_other_news_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        content.setOnRichTextImageClickListener((view, imageUrls, position) -> {
            if (imageUrls != null && imageUrls.size() > 0) {
                //                OtherNewsDetailActivity.this.view = view;
                //                url = imageUrls.get(position);
                //                ImagePreViewActivity.start(OtherNewsDetailActivity.this, new ArrayList<>(Collections.singletonList(url)), 0, view, NewsUtil.NEWS_DETAIL_FLAG);
//                Map<String, Object> map = new HashMap<>();
//                map.put(Constant.POSITION, position);
//                Router.getInstance().deal(new RouterRequest.Builder()
//                        .provideName("chat").actionName("preview")
//                        .context(OtherNewsDetailActivity.this)
//                        .paramMap(map).object(imageUrls).build());
            }
        });
        runOnUiThread(() -> mPresenter.getOtherNewsDetailData(getIntent().getStringExtra(NewsUtil.POST_ID)));
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("详情");
        toolBarOption.setNeedNavigation(true);
//        setToolBar(toolBarOption);
    }
    public static void start(Context context, String postid) {
        Intent intent = new Intent(context, OtherNewsDetailActivity.class);
        intent.putExtra(NewsUtil.POST_ID, postid);
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
    public void killMyself() {
        finish();
    }

    @Override
    public void updateData(OtherNewsDetailBean otherNewsDetailBean) {
        if (otherNewsDetailBean != null) {
//            updateTitle(otherNewsDetailBean.getTitle());
            content.setRichText(otherNewsDetailBean.getBody());
        }
    }
}
