package mystery.anonymous.saheni.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import mystery.anonymous.saheni.R;

public class AlarmRingActivity extends AppCompatActivity {

    private TextView tvAlarmTime, tvAlarmNote;
    private Button btnAwake;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        tvAlarmTime = findViewById(R.id.tvAlarmTimeDisplay);
        tvAlarmNote = findViewById(R.id.tvAlarmNoteDisplay);
        btnAwake = findViewById(R.id.btnAwake);

        // استرجاع البيانات الممررة عبر Intent
        String time = getIntent().getStringExtra("time");
        String note = getIntent().getStringExtra("note");
        String toneUri = getIntent().getStringExtra("tone");

        // تعيين قيم افتراضية إذا لم يتم تمريرها
        tvAlarmTime.setText(time != null ? time : "06:00 AM");
        tvAlarmNote.setText(note != null ? note : "منبه");

        // التأكد من وجود نغمة، وإن لم توجد يتم استخدام النغمة الافتراضية الخاصة بالنظام
        if (toneUri == null || toneUri.isEmpty()) {
            Uri defaultTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            toneUri = defaultTone != null ? defaultTone.toString() : "";
        }

        // بدء تشغيل نغمة الرنين باستخدام MediaPlayer
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(toneUri));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d("AlarmReceiver", "Playing alarm sound...");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AlarmReceiver", "Failed to play alarm tone", e);
        }


        btnAwake.setOnClickListener(v -> {
            // إيقاف نغمة الرنين وتحرير الموارد عند الضغط على الزر
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }
}
