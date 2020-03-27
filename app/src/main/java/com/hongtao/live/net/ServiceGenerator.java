package com.hongtao.live.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created 2019/7/11.
 *
 * @author HongTao
 */
public class ServiceGenerator {
    // App 中的 BaseUrl，若更改 Url，即可在此直接修改。
    private static final String BASE_URL = "http://192.168.0.107:8080/Live/";
    // Gson 转换器
    private static GsonConverterFactory gsonFactory = GsonConverterFactory.create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonFactory);

    private static Retrofit retrofit = builder.build();
    //自定义的登录拦截器
    private static LoginInterceptor sLoginInterceptor = new LoginInterceptor();
    //使用 OKHttp 进行网络请求
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    // Api 接口泛型封装，参数：带注解配置的 Api 请求接口，返回即可执行网络请求。
    public static <S> S createService(Class<S> serviceClass) {
        //在此对 OKHttp 添加登录拦截器的拦截
        if (!httpClient.interceptors().contains(sLoginInterceptor)) {
            httpClient.addInterceptor(sLoginInterceptor);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
