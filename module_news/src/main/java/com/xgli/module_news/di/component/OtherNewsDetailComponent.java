package com.xgli.module_news.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_news.di.module.OtherNewsDetailModule;
import com.xgli.module_news.mvp.contract.OtherNewsDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xgli.module_news.mvp.ui.activity.OtherNewsDetailActivity;


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
@ActivityScope
@Component(modules = OtherNewsDetailModule.class, dependencies = AppComponent.class)
public interface OtherNewsDetailComponent {
    void inject(OtherNewsDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OtherNewsDetailComponent.Builder view(OtherNewsDetailContract.View view);

        OtherNewsDetailComponent.Builder appComponent(AppComponent appComponent);

        OtherNewsDetailComponent build();
    }
}