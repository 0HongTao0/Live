package com.hongtao.live.module;

/**
 * Created 2020/3/27.
 *
 * @author HongTao
 */
public class Attention {

    /**
     * userId : QOE889
     * avatar : http://pic1.win4000.com/pic/4/80/96e66c3531_250_350.jpg
     * nick : 肥仔
     * gender : 1
     * roomId : 888
     * url : rtmp://192.168.0.107:1935/Live/QOE889
     * roomName : 肥仔
     * roomIntroduction : 一起来聊车
     * attentionId : 31
     * time : 1585217519000
     * living : true
     * num : 1
     */

    private String userId;
    private String avatar;
    private String nick;
    private int gender;
    private int roomId;
    private String roomName;
    private String roomIntroduction;
    private int attentionId;
    private long time;
    private boolean living;
    private String url;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public int getAttentionId() {
        return attentionId;
    }

    public void setAttentionId(int attentionId) {
        this.attentionId = attentionId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
