package com.hongtao.live.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created 2020/3/25.
 *
 * @author HongTao
 */
public class Message implements Parcelable {

    /**
     * avatar : http://pic1.win4000.com/pic/1/53/bdd7338f1f_250_350.jpg
     * nick : 123
     * message : 这是一条有灵魂的信息
     * time : 1585113609000
     * type : 1
     */

    private String avatar;
    private String nick;
    private String message;
    private long time;
    private int type;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.nick);
        dest.writeString(this.message);
        dest.writeLong(this.time);
        dest.writeInt(this.type);
    }

    public Message() {
    }

    protected Message(Parcel in) {
        this.avatar = in.readString();
        this.nick = in.readString();
        this.message = in.readString();
        this.time = in.readLong();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public String toString() {
        return "Message{" +
                "avatar='" + avatar + '\'' +
                ", nick='" + nick + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", type=" + type +
                '}';
    }
}
