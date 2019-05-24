package com.xgli.module_project.mvp.model.api;

import com.xgli.module_project.mvp.model.entity.BaseResponse;
import com.xgli.module_project.mvp.model.entity.ProjectClassifyData;
import com.xgli.module_project.mvp.model.entity.ProjectListData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * <p>
 * Created by ArmsComponentTemplate on 05/05/2019 15:23
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent">Star me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent-Template">模版请保持更新</a>
 * ================================================
 */
public interface Api {
    String WAN_DOMAIN_NAME = "wan";
    String WAN_DOMAIN = "http://www.wanandroid.com";

    @Headers({DOMAIN_NAME_HEADER + WAN_DOMAIN_NAME})
    @GET("/project/tree/json")
    Observable<BaseResponse<List<ProjectClassifyData>>> getProjectClassifyData();


    /**
     * 项目类别数据
     * http://www.wanandroid.com/project/list/1/json?cid=294
     *
     * @param page page num
     * @param cid second page id
     * @return 项目类别数据
     */
    @Headers({DOMAIN_NAME_HEADER + WAN_DOMAIN_NAME})
    @GET("project/list/{page}/json")
    Observable<BaseResponse<ProjectListData>> getProjectListData(@Path("page") int page, @Query("cid") int cid);
}
