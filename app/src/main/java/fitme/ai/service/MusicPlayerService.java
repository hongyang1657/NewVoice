/*
 * Created by baoleiwei on 17-5-9 下午3:42
 * Copyright (c) 2017. All rights reserved.
 *
 *
 */

package fitme.ai.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

import fitme.ai.utils.L;

/**
 * Created by hongy on 2017/5/9.
 */

public class MusicPlayerService extends Service{

    public static final int PLAT_MUSIC = 1;
    public static final int PAUSE_MUSIC = 2;
    public static final int RESUME_MUSIC = 3;
    public static final int NEXT_MUSIC = 4;
    public static final int STOP_MUSIC = 5;
    public static final int REDUCE_MUSIC_VOLUME = 6;   //把音乐声音调小
    public static final int RECOVER_MUSIC_VOLUME = 7;   //恢复音乐声音

    public static final int currentVolume = 0;

    //用于播放音乐等媒体资源
    private MediaPlayer mediaPlayer;
    //标志判断播放歌曲是否是停止之后重新播放，还是继续播放
    private boolean isStop = true;
    private boolean isCreatPlayer = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i("service-----------------------------onCreate");
        isCreatPlayer = false;
        if (mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
            //为播放器添加播放完成时的监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (isCreatPlayer){
                        L.i("当前歌曲播放完毕，下一首"+mp.isPlaying());

                    }


                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    L.i("onError------------------------------");
                    return false;
                }
            });
        }
    }

    /**
     * 在此方法中，可以执行相关逻辑，如耗时操作
     * @param intent :由Activity传递给service的信息，存在intent中
     * @param flags ：规定的额外信息
     * @param startId ：开启服务时，如果有规定id，则传入startid
     * @return 返回值规定此startservice是哪种类型，粘性的还是非粘性的
     *          START_STICKY:粘性的，遇到异常停止后重新启动，并且intent=null
     *          START_NOT_STICKY:非粘性，遇到异常停止不会重启
     *          START_REDELIVER_INTENT:粘性的，重新启动，并且将Context传递的信息intent传递
     * 此方法是唯一的可以执行很多次的方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("收到播放指令");
        String songUrl = intent.getStringExtra("songUrl");
        L.i("isstopppppppppp:"+isStop);
        switch (intent.getIntExtra("type",-1)){
            case PLAT_MUSIC:
                L.i("播放音乐！！！！！！！！！！！！！！！！！");
                isCreatPlayer = true;
                if (isStop){
                    playMusic(songUrl);
                }else if (!isStop&&mediaPlayer.isPlaying()&&mediaPlayer!=null){
                    mediaPlayer.start();

                }
                break;
            case PAUSE_MUSIC:
                L.i("暂停播放音乐！！！！！！！！！！！！！！！！！");
                //播放器不为空，并且正在播放
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case RESUME_MUSIC:
                L.i("继续播放音乐！！！！！！！！！！！！！！！！！");
                if (mediaPlayer!=null&&!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case NEXT_MUSIC:
                L.i("下一曲音乐！！！！！！！！！！！！！！！！！");
                playMusic(songUrl);
                break;
            case STOP_MUSIC:
                if (mediaPlayer!=null){
                    //停止之后要开始播放音乐
                    mediaPlayer.stop();
                    isStop=true;
                }
                break;
            case REDUCE_MUSIC_VOLUME:
                if (mediaPlayer!=null){
                    //把声音调小
                    mediaPlayer.setVolume(0.1f,0.1f);

                }
                break;
            case RECOVER_MUSIC_VOLUME:
                if (mediaPlayer!=null){
                    //把声音调正常
                    mediaPlayer.setVolume(1f,1f);
                }
                break;
        }
        return START_NOT_STICKY;
    }

    private void playMusic(String songUrl){
        //重置mediaplayer
        mediaPlayer.reset();
        //将需要播放的资源与之绑定
        try {
            mediaPlayer.setDataSource(songUrl);
            L.i("正在播放："+songUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.prepare();//同步的准备方法。
            mediaPlayer.prepareAsync();   //异步准备
            //是否循环播放
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    isStop=false;
                    mediaPlayer.start();
                    L.i("准备完毕，开始播放");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
