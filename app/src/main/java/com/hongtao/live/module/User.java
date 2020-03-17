package com.hongtao.live.module;

/**
 * Created 2020/3/17.
 *
 * @author HongTao
 */
public class User {

    /**
     * address :
     * id : 34
     * nick : 123
     * userId : 123
     * job :
     * birthday : 946656000000
     * introduction : 这人懒的什么都没写下！
     * gender : 1
     * liveIntroduction : 来呀，直播间里面很好玩哦！
     * avatar : https://raw.githubusercontent.com/0HongTao0/Blog/master/default_avatar.png
     */

    private String address;
    private int id;
    private String nick;
    private String userId;
    private String job;
    private long birthday;
    private String introduction;
    private int gender;
    private String liveIntroduction;
    private String avatar;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLiveIntroduction() {
        return liveIntroduction;
    }

    public void setLiveIntroduction(String liveIntroduction) {
        this.liveIntroduction = liveIntroduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
