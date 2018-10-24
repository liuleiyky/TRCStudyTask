package com.liul.trc_study_task.retrofit;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

/**
 * 创建用于描述网络请求的接口
 */
public interface APi {
    // GET注解的作用：采用GET方法发送网络请求
    // getNews  接收网络请求数据的方法
    // 返回类型为Call<News>，News是接收数据的类
    // 如果想直接获得Responsebody中的内容，可以定义网络请求返回值为Call<ResponseBody>

    @Headers("")
    @GET("")
//    Call<News> getNews(@Query("num") String num,@Query("page") String page);
    Observable<News> getNews(@Query("num") String num, @Query("page") String page);
}
