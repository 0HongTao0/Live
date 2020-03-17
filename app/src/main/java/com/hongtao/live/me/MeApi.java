package com.hongtao.live.me;

import com.hongtao.live.module.User;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created 2020/3/17.
 *
 * @author HongTao
 */
public interface MeApi {

    @GET("Me/user")
    Observable<User> getUser(
    );
}
