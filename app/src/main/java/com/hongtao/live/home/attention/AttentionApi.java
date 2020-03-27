package com.hongtao.live.home.attention;

import com.hongtao.live.module.Attention;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created 2020/3/27.
 *
 * @author HongTao
 */
public interface AttentionApi {
    @FormUrlEncoded
    @POST("attention/attentionRoom")
    Observable<Object> attentionRoom(
            @Field("roomId") int roomId);

    @FormUrlEncoded
    @POST("attention/getOffRoom")
    Observable<Object> getOffRoom(
            @Field("roomId") int roomId);

    @GET("attention/getAttentionRoom")
    Observable<List<Attention>> getAttentionRoom();
}
