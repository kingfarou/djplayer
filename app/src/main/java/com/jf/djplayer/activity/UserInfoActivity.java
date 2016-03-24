package com.jf.djplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.jf.djplayer.other.UserInfo;
import com.jf.djplayer.R;

import com.jf.djplayer.tool.database.UserInfoOpenHelper;

/**
 * Created by JF on 2016/2/22.
 * 如果没有账号登录
 * 该活动会加载注册登陆用的Fragment
 * 否则它会加载显示用户信息那个Fragment
 */
public class UserInfoActivity extends Activity implements View.OnClickListener{

    private EditText userNameEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的Actionbar
        setContentView(R.layout.fragment_no_sign_in);
        viewInit();
    }

    private void viewInit(){
        findViewById(R.id.btn_activity_sign_in_login).setOnClickListener(this);
        findViewById(R.id.btn_activity_sign_in_sign_up).setOnClickListener(this);
        userNameEt = (EditText)findViewById(R.id.et_activity_sign_in_username);
        passwordEt = (EditText)findViewById(R.id.et_activity_sign_in_password);
    }

    @Override
    public void onClick(View v) {
//        如果按下邮箱登陆按钮
        if(v.getId()==R.id.btn_activity_sign_in_login){
            eMailLogin();
        }else if(v.getId()==R.id.btn_activity_sign_in_sign_up){
//            如果按下注册按钮
            mailSignUp();
        }else if(v.getId()==R.id.btn_activity_sign_in_weixin){
//            如果按下微信登录
        }else if(v.getId()==R.id.btn_activity_sign_in_weibo){
//            如果按下微博登录
        }
    }//onClick

//    通过邮箱进行登录
//    调用这个方法处理
    private void eMailLogin(){
//        处理流程大概如下
//        先看用户名是不是为空
//        如果为空提示用户输入
//        否则查看是否存在该用户名
//        如果存在匹配密码
//        否则提示用户改用户名并不存在
        String userName = userNameEt.getText().toString();
//        如果没输入用户名
        if(userName.length()==0) {
            Toast.makeText(this,"请输入账号哦亲~",Toast.LENGTH_SHORT).show();
            return;
        }
//        如果用户名不为空
//        查数据库看用户名是否存在
        UserInfoOpenHelper userInfoOpenHelper = new UserInfoOpenHelper(this,1);
        if(userInfoOpenHelper.hasUserNames(userName)){
//            如果存在该用户名查看密码是否正确
            String password = this.passwordEt.getText().toString();//获取用户输入密码
            if( userInfoOpenHelper.userNameMachPassword(userName,password)){
//                返回用户信息给登陆处
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(userName);
                setResult(RESULT_OK,new Intent().putExtra("userInfo",userInfo));
                finish();
            }else{
                Toast.makeText(this,"密码输入错误哦亲",Toast.LENGTH_SHORT).show();
            }
        }else{
//            如果当前用户名不存在
            Toast.makeText(this,"当前用户名不存在，趁用户名未被占用赶紧注册哦亲~",Toast.LENGTH_SHORT).show();
        }
    }//eMailLogin()

//    通过邮箱进行注册调用这个方法处理
    private void mailSignUp(){
        /*
        流程如下：
        先看用户名是不是为空，如果为空提示用户，
        如不为空，判断密码是否大于8位，小于8位提示用户，
        如果用户名和密码输入都是合法，
        判断该用户是否已注册，根据注册与否提示用户
         */
        String userName = userNameEt.getText().toString();
//        如果用户为输入用户名
        if(userName.length()==0){
            Toast.makeText(this,"请你输入注册用的邮箱哦亲",Toast.LENGTH_SHORT).show();
            return;
        }
        String password = passwordEt.getText().toString();
        if(password.length()<8){
            Toast.makeText(this,"密码最少8位数哦亲~",Toast.LENGTH_SHORT).show();
            return;
        }
//        如果用户名和密码都不为空
//        查看用户名是否已被注册过
        UserInfoOpenHelper userInfoOpenHelper = new UserInfoOpenHelper(this,1);
        if(userInfoOpenHelper.hasUserNames(userName)){
            Toast.makeText(this,"该用户名已被占用，请你更换一个哦亲",Toast.LENGTH_SHORT).show();
            return;
        }else{
            userInfoOpenHelper.addNewUser(userName,password);
            Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
        }
    }//mailSignUp
}
