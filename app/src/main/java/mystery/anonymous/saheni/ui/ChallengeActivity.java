package mystery.anonymous.saheni.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.service.StepCounterService;

public class ChallengeActivity extends AppCompatActivity {
    private Ringtone ringtone;
    private Random random = new Random();
    private int gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameType = random.nextInt(3);
        String uriString = getIntent().getStringExtra("ringtone");
        Uri uri = Uri.parse(uriString);
        ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();

        switch (gameType) {
            case 0: startButtonGame(); break;
            case 1: startMathGame(); break;
            case 2: startStepGame(); break;
        }
    }

    @Override
    public void onBackPressed() {
        // disable back
        super.onBackPressed();
    }

    private void startButtonGame() {
        setContentView(R.layout.activity_button_game);
        new ButtonGameActivity();
    }

    private void startMathGame() {
        setContentView(R.layout.activity_math_game);
        new MathQuizActivity();
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void startStepGame() {
        setContentView(R.layout.activity_step_game);
        registerReceiver(stepReceiver, new IntentFilter("STEP_UPDATE"));
        startService(new Intent(this, StepCounterService.class));
    }

    private BroadcastReceiver stepReceiver = new BroadcastReceiver() {
        int target = random.nextInt(6) + 5;
        @Override
        public void onReceive(Context context, Intent intent) {
            int steps = intent.getIntExtra("steps", 0);
            ((TextView) findViewById(R.id.stepsText)).setText(steps + "/" + target);
            if (steps >= target) {
                stopAlarm();
            }
        }
    };

    private void stopAlarm() {
        ringtone.stop();
        finish();
    }
}
