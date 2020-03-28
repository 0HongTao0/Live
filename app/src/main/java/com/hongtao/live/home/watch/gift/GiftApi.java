package com.hongtao.live.home.watch.gift;

import com.hongtao.live.module.Gift;
import com.hongtao.live.module.NormalResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public interface GiftApi {

    @GET("gift/getGifts")
    Observable<List<Gift>> getGifts();

    @POST("gift/sendGift")
    @FormUrlEncoded
    Observable<NormalResponse> sendGift(@Field("giftId") int giftId
            , @Field("toUserId") String toUserId
            , @Field("roomId") int roomId);
}
