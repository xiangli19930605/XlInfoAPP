package com.xgli.module_news.mvp.model.api;

import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.mvp.model.entity.NewInfoBean;
import com.xgli.module_news.mvp.model.entity.OtherNewsDetailBean;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.model.entity.RawSpecialNewsBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Url;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      16:56
 * QQ:             1981367757
 */

public interface OtherNewsApi {


    @Headers({DOMAIN_NAME_HEADER + NewsUtil.WANGYI_DOMAIN_NAME,NewsUtil.HEADER_AGENT})
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewInfoBean>>> getNewsList(@Path("type") String type, @Path("id") String id,
                                                           @Path("startPage") int startPage);

    @Headers({DOMAIN_NAME_HEADER + NewsUtil.WANGYI_DOMAIN_NAME,NewsUtil.HEADER_AGENT
            , NewsUtil.CACHE_CONTROL})
    @GET("photo/api/set/{photoId}.json")
    Observable<PhotoSetBean> getPhotoSetData(@Path("photoId") String photoId);

    @Headers({DOMAIN_NAME_HEADER + NewsUtil.WANGYI_DOMAIN_NAME,NewsUtil.HEADER_AGENT
            , NewsUtil.CACHE_CONTROL})
    @GET("nc/special/{specialId}.html")
    Observable<Map<String, RawSpecialNewsBean>> getSpecialNewsData(@Path("specialId") String specialIde);

    @Headers({DOMAIN_NAME_HEADER+ NewsUtil.WANGYI_DOMAIN_NAME,NewsUtil.HEADER_AGENT
            , NewsUtil.CACHE_CONTROL})
    @GET("nc/article/{newsId}/full.html")
    Observable<Map<String, OtherNewsDetailBean>> getNewsDetail(@Path("newsId") String newsId);


    @Headers({DOMAIN_NAME_HEADER + "gank"})
    @GET("http://gank.io/api/data/福利/{num}/{page}")
    Observable<PictureBean> getPhotoListData(@Path("num") int num, @Path("page") int page);
}

