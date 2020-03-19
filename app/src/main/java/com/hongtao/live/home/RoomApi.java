package com.hongtao.live.home;

import com.hongtao.live.module.Room;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public interface RoomApi {
    @GET("room/getRooms")
    Observable<List<Room>> getRooms();
}
