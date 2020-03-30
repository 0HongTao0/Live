package com.hongtao.live.me;

import com.hongtao.live.module.NormalResponse;
import com.hongtao.live.module.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created 2020/3/17.
 *
 * @author HongTao
 */
public interface MeApi {

    @GET("me/user")
    Observable<User> getUser();

    @POST("me/alterNick")
    @FormUrlEncoded
    Observable<NormalResponse> alterNick(@Field("nick") String nick);

    @POST("me/alterJob")
    @FormUrlEncoded
    Observable<NormalResponse> alterJob(@Field("job") String job);

    @POST("me/alterIntroduce")
    @FormUrlEncoded
    Observable<NormalResponse> alterIntroduce(@Field("introduce") String introduce);

    @POST("me/alterLiveIntroduce")
    @FormUrlEncoded
    Observable<NormalResponse> alterLiveIntroduce(@Field("liveIntroduce") String liveIntroduce);

    @POST("me/alterGender")
    @FormUrlEncoded
    Observable<NormalResponse> alterGender(@Field("gender") int gender);

    @POST("me/alterAddress")
    @FormUrlEncoded
    Observable<NormalResponse> alterAddress(@Field("addressId") int addressId);

    @POST("me/alterBirthday")
    @FormUrlEncoded
    Observable<NormalResponse> alterBirthday(@Field("birthday") long birthday);
}
