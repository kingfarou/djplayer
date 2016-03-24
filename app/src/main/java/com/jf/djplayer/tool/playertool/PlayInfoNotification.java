package com.jf.djplayer.tool.playertool;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jf.djplayer.SongInfo;
import com.jf.djplayer.activity.SongPlayInfoActivity;
import com.jf.djplayer.R;

/**
 * Created by Administrator on 2015/8/24.
 * 负责发送播放消息的类
 */
public class PlayInfoNotification {

    private final NotificationCompat.Builder builder;
    private final RemoteViews remoteViews;
    private final NotificationManager notificationManager;
    private Context context;
    private static final int SONG_INFO_ACTIVITY_REQUEST_CODE = 4;//请求音乐播放信息窗体的请求码
    private static final int SONG_NOTIFICATION_FLAG = 1;//这是所发送的通知标记

    /*Notification广播用的相关常量*/
    private static final int SONG_PLAY = 1;
    private static final int SONG_PAUSE = 2;
    private static final int SONG_NEXT = 3;
    private static final String SONG_CONTROL_BROADCAST_EXTRA = "song_control_broadCast_extra";
    private static final int SONG_PREVIOUS = 4;
    private static final String NOTIFICATION_TO_SERVICE_ACTION = "com.danjuan.www.djplayer.SongControlBoradCast";
    /*Notification通知用的相关常量*/

    /**
     * 构造的同时需要将：
     * builder初始化。
     * remoteViews给创建。
     * 创建NotificationManager
     * @param context
     */
    public PlayInfoNotification(Context context){
        this.context = context;
        this.builder = new NotificationCompat.Builder(context);
        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_custom_layout);
        this.notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        initNotification();
    }

    /**
     * Notification的初始化
     * 注意这只是初始化
     * 这并没有执行唤醒Notification
     * */
    private void initNotification() {

        //设置出RemoteViews
        remoteViews.setImageViewResource(R.id.iv_notification_custom_song_icon, R.drawable.ic_app);

        //设置用于打开窗体用的意图
        Intent songInfoIntent = new Intent(context, SongPlayInfoActivity.class);
        PendingIntent songInfoPendingIntent =
                PendingIntent.getActivity(context, SONG_INFO_ACTIVITY_REQUEST_CODE, songInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置好builder
        builder.setSmallIcon(R.drawable.ic_app)
                .setContent(remoteViews)
                .setOngoing(true)
                .setContentIntent(songInfoPendingIntent);

    }


    /**
     * 传入歌曲信息对象
     * 就能更新歌曲信息
     * @param songInfo 要显示的歌曲信息对象
     */
    public void updateNotification(SongInfo songInfo){
        remoteViews.setTextViewText(R.id.tv_notification_custom_title, songInfo.getSongName());
        remoteViews.setTextViewText(R.id.tv_notification_custom_artist, songInfo.getSongSinger());
        builder.setContent(remoteViews).setTicker(songInfo.getSongName());
        notificationManager.notify(SONG_NOTIFICATION_FLAG, builder.build());
    }

    public void cancelNotification(){
        notificationManager.cancel(SONG_NOTIFICATION_FLAG);
    }
}
