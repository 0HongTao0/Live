package com.hongtao.live.home;

import com.hongtao.live.module.Room;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public interface RoomApi {
    @GET("room/getRooms")
    Observable<List<Room>> getRooms();

    @GET("room/searchRooms")
    Observable<List<Room>> searchRooms(@Query("roomKey")String roomKey);

    @GET("room/checkRoom")
    Observable<Room> checkRoom();

    @FormUrlEncoded
    @POST("room/createRoom")
    Observable<Room> createRoom(@Field("roomName") String roomName,
                                @Field("roomIntroduction") String roomIntroduction);

    @FormUrlEncoded
    @POST("room/updateRoom")
    Observable<Room> updateRoom(@Field("roomId") int roomId,
                                @Field("roomName") String roomName,
                                @Field("roomIntroduction") String roomIntroduction);
}
