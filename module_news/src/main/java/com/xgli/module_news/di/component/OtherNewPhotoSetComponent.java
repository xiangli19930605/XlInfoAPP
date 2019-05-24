package com.xgli.module_news.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_news.di.module.OtherNewPhotoSetModule;
import com.xgli.module_news.mvp.contract.OtherNewPhotoSetContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xgli.module_news.mvp.ui.activity.OtherNewPhotoSetActivity;


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
@ActivityScope
@Component(modules = OtherNewPhotoSetModule.class, dependencies = AppComponent.class)
public interface OtherNewPhotoSetComponent {
    void inject(OtherNewPhotoSetActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OtherNewPhotoSetComponent.Builder view(OtherNewPhotoSetContract.View view);

        OtherNewPhotoSetComponent.Builder appComponent(AppComponent appComponent);

        OtherNewPhotoSetComponent build();
    }
}