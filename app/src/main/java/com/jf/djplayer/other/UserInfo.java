package com.jf.djplayer.other;

import java.io.Serializable;

/**
 * Created by JF on 2016/2/26.
 * 该类标记已登录的用户信息
 */
public class UserInfo implements Serializable{

    private String userName;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }
}
