package com.jf.djplayer.playertool;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jf.djplayer.other.IntentConstant;
import com.jf.djplayer.module.Song;
import com.jf.djplayer.playinfo.PlayInfoActivity;
import com.jf.djplayer.R;
import com.jf.djplayer.service.PlayerService;

/**
 * Created by Administrator on 2015/8/24.
 * 负责发送播放消息的类
 */
public class PlayInfoNotification {

    private final NotificationCompat.Builder builder;//"notification"的构造类
    private final RemoteViews remoteViews;//"notification"自定义视图的工具
    private final NotificationManager notificationManager;//"notification"的管理器

    private Context context;
    private static final int PLAY_INFO_NOTIFICATION_FLAG = 9;//这是所发送的通知标记
    private static final int REQUEST_CODE_SONG_INFO_ACTIVITY = 7;//请求音乐播放信息窗体的请求码

    //启动"PlayService"所使用的请求码
    private static final int REQUEST_CODE_PREVIOUS = 1<<1;//播放前一首请求码
    private static final int REQUEST_CODE_PLAY = 1<<2;//播放或暂停请求码
    private static final int REQUEST_CODE_NEXT = 1<<3;//播放下一首请求码

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
        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_play_info);
        this.notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        initNotification();
    }

    /**
     * Notification的初始化
     * 设置点击"notification"后的意图
     * 设置"notification"弹出图标
     * */
    private void initNotification() {

        //设置出RemoteViews
        remoteViews.setImageViewResource(R.id.iv_notification_custom_song_icon, R.drawable.ic_app);

        //设置用于打开窗体用的意图
        Intent songInfoIntent = new Intent(context, PlayInfoActivity.class);
        PendingIntent songInfoPendingIntent =
                PendingIntent.getActivity(context, REQUEST_CODE_SONG_INFO_ACTIVITY, songInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置好builder
        builder.setSmallIcon(R.drawable.ic_app)
//                .setOngoing(true)
                .setContentIntent(songInfoPendingIntent);

    }


    /**
     * 传入歌曲信息对象
     * 就能更新歌曲信息
     * @param songInfo 要显示的歌曲信息对象
     */
    public void updateNotification(Song songInfo){
        if(songInfo == null){
            return;
        }
        //设置要显示的歌曲名字歌手名字
        remoteViews.setTextViewText(R.id.tv_notification_play_info_songName, songInfo.getSongName());
        remoteViews.setTextViewText(R.id.tv_notification_play_info_singerName, songInfo.getSingerName());
        //设置前一曲和播放以及下一曲按钮的点击
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play_info_previous, getPreviousPending());
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play_info_play, getPlayPending());
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play_info_next, getNextPending());

        builder.setContent(remoteViews)
                .setTicker(songInfo.getSongName());
        notificationManager.notify(PLAY_INFO_NOTIFICATION_FLAG, builder.build());
    }

    /**
     * 传入歌曲信息对象，更新通知栏的歌曲信息
     * @param songInfo 待更新的歌曲信息对象
     * @param isPlaying 当前歌曲是否正在播放
     */
    public void updateNotification(Song songInfo, boolean isPlaying){
        if(songInfo == null){
            return;
        }
        //设置要显示的歌曲名字歌手名字
        remoteViews.setTextViewText(R.id.tv_notification_play_info_songName, songInfo.getSongName());
        remoteViews.setTextViewText(R.id.tv_notification_play_info_singerName, songInfo.getSingerName());
        //根据歌曲播放状态设置图片
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.iv_notification_play_info_play,R.drawable.icon_notification_pause);
        }else{
            remoteViews.setImageViewResource(R.id.iv_notification_play_info_play, R.drawable.ic_notification_play_info_play);
        }
        //设置前一曲和播放以及下一曲按钮的点击
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play_info_previous, getPreviousPending());
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play_info_play, getPlayPending());
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_play_info_next, getNextPending());

        builder.setContent(remoteViews)
                .setTicker(songInfo.getSongName());
        notificationManager.notify(PLAY_INFO_NOTIFICATION_FLAG, builder.build());

    }

    public void cancelNotification(){
        notificationManager.cancel(PLAY_INFO_NOTIFICATION_FLAG);
    }

    /*
    设置前一曲按钮的"PendingIntent"
     */
    private PendingIntent getPreviousPending(){
        Intent previousIntent = new Intent(context, PlayerService.class);
        previousIntent.setAction(IntentConstant.ACTION_PLAY_PREVIOUS_SONG);
        return PendingIntent.getService(context, REQUEST_CODE_PREVIOUS, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPlayPending(){
        Intent playIntent = new Intent(context, PlayerService.class);
        playIntent.setAction(IntentConstant.ACTION_PLAY_SONG);
        return PendingIntent.getService(context, REQUEST_CODE_PLAY, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getNextPending(){
        Intent nextIntent = new Intent(context, PlayerService.class);
        nextIntent.setAction(IntentConstant.ACTION_PLAY_NEXT_SONG);

        return PendingIntent.getService(context, REQUEST_CODE_NEXT, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
