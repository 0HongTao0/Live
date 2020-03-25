package com.hongtao.live.home.watch;

import com.hongtao.live.module.Message;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created 2020/3/25.
 *
 * @author HongTao
 */
public interface MessageApi {
    @FormUrlEncoded
    @POST("chat/getMessage")
    Observable<List<Message>> getMessage(
            @Field("roomId") int roomId);

    @FormUrlEncoded
    @POST("chat/sendMessage")
    Observable<Object> sendMessage(
            @Field("roomId") int roomId,
            @Field("message") String message,
            @Field("type") int type);

}
