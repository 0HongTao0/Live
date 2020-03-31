package com.hongtao.live.me;

import com.hongtao.live.module.City;
import com.hongtao.live.module.Country;
import com.hongtao.live.module.Province;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created 2020/3/30.
 *
 * @author HongTao
 */
public interface AddressApi {

    @GET("address/getProvince")
    Observable<List<Province>> getProvince();


    @GET("address/getCity")
    Observable<List<City>> getCity(@Query("provinceId") int provinceId);

    @GET("address/getCountry")
    Observable<List<Country>> getCountry(@Query("cityId") int cityId);
}