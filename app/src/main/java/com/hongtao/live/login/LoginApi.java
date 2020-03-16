package com.hongtao.live.login;

import com.hongtao.live.module.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created 2020/3/16.
 *
 * @author HongTao
 */
public interface LoginApi {
    @FormUrlEncoded
    @POST("loginAction/login")
    Observable<LoginData> postLogin(
            @Field("userId") String userId,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("loginAction/registered")
    Observable<LoginData> postRegistered(
            @Field("userId") String userId,
            @Field("password") String password
    );
}
