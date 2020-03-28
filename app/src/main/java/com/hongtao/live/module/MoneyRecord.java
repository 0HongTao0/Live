package com.hongtao.live.module;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public class MoneyRecord {
    public static final int TYPE_SEND_GIFT = -2;
    public static final int TYPE_RECEIVE_GIFT = 2;
    public static final int TYPE_RECHARGE = 1;
    public static final int TYPE_WITHDRAW = -1;
    /**
     * id : 22
     * type : 1
     * money : 100.0
     * userId : 123
     * time : 1585389057000
     */

    private int id;
    private int type;
    private double money;
    private String userId;
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
