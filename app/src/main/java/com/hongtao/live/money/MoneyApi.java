package com.hongtao.live.money;

import com.hongtao.live.module.MoneyRecord;
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
public interface MoneyApi {

    @POST("money/recharge")
    @FormUrlEncoded
    Observable<NormalResponse> recharge(@Field("money") float money);

    @POST("money/withdraw")
    @FormUrlEncoded
    Observable<NormalResponse> withdraw(@Field("money") float money);

    @GET("money/getMoneyRecord")
    Observable<List<MoneyRecord>> getMoneyRecord();
}
