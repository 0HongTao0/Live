package com.hongtao.live.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created 2020/3/19.
 *
 * @author HongTao
 */
public class Room implements Parcelable {


    /**
     * code : 1
     * avatar : http://pic1.win4000.com/pic/4/80/96e66c3531_250_350.jpg
     * nick : 肥仔
     * userId : QOE889
     * living : 1
     * num : 89000
     * roomName : 肥仔
     * roomIntroduction : 一起来聊车
     * url : rtmp://192.168.0.105:1935/Live/QOE889
     * roomId : 888
     */

    private int code;
    private String avatar;
    private String nick;
    private String userId;
    private int living;
    private int num;
    private String roomName;
    private String roomIntroduction;
    private String url;
    private int roomId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLiving() {
        return living;
    }

    public void setLiving(int living) {
        this.living = living;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomIntroduction() {
        return roomIntroduction;
    }

    public void setRoomIntroduction(String roomIntroduction) {
        this.roomIntroduction = roomIntroduction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.avatar);
        dest.writeString(this.nick);
        dest.writeString(this.userId);
        dest.writeInt(this.living);
        dest.writeInt(this.num);
        dest.writeString(this.roomName);
        dest.writeString(this.roomIntroduction);
        dest.writeString(this.url);
        dest.writeInt(this.roomId);
    }

    public Room() {
    }

    protected Room(Parcel in) {
        this.code = in.readInt();
        this.avatar = in.readString();
        this.nick = in.readString();
        this.userId = in.readString();
        this.living = in.readInt();
        this.num = in.readInt();
        this.roomName = in.readString();
        this.roomIntroduction = in.readString();
        this.url = in.readString();
        this.roomId = in.readInt();
    }

    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel source) {
            return new Room(source);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public String toString() {
        return "Room{" +
                "code=" + code +
                ", avatar='" + avatar + '\'' +
                ", nick='" + nick + '\'' +
                ", userId='" + userId + '\'' +
                ", living=" + living +
                ", num=" + num +
                ", roomName='" + roomName + '\'' +
                ", roomIntroduction='" + roomIntroduction + '\'' +
                ", url='" + url + '\'' +
                ", roomId=" + roomId +
                '}';
    }
}
