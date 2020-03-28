package com.hongtao.live.module;

/**
 * Created 2020/3/28.
 *
 * @author HongTao
 */
public class Gift {

    /**
     * name : 糖
     * price : 1.68
     * giftId : 1
     * pic : https://raw.githubusercontent.com/0HongTao0/Blog/master/sugar.png
     */

    private String name;
    private double price;
    private int giftId;
    private String pic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
