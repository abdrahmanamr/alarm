package mystery.anonymous.saheni.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mystery.anonymous.saheni.R;

public class AlarmRingActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private static final String[] CHALLENGES = {
            "ButtonGameActivity",
            "MathQuizActivity",
            "StepChallengeActivity"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // وضع Immersive Mode لإخفاء أشرطة النظام ومنع الخروج :contentReference[oaicite:0]{index=0}
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        // ضبط أعلى مستوى صوت للمنبه :contentReference[oaicite:1]{index=1}
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        am.setStreamVolume(AudioManager.STREAM_ALARM, max, AudioManager.FLAG_PLAY_SOUND);

        // تشغيل نغمة المنبه
        mediaPlayer = MediaPlayer.create(this, getIntent().getData());
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // إطلاق تحدٍّ عشوائي بعد رسالة قصيرة
        new Handler().postDelayed(this::launchRandomChallenge, 500);
    }

    private void launchRandomChallenge() {
        int idx = (int) (Math.random() * CHALLENGES.length);
        try {
            Class<?> cls = Class.forName("mystery.anonymous.saheni.ui." + CHALLENGES[idx]);
            startActivity(new Intent(this, cls));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent e){
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN||keyCode==KeyEvent.KEYCODE_VOLUME_UP)
            return true;
        return super.onKeyDown(keyCode,e);
    }

}
