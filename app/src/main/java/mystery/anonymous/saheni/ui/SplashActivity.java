package mystery.anonymous.saheni.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import mystery.anonymous.saheni.R;

public class SplashActivity extends AppCompatActivity {

    private TextView tvHint;
    private String[] hints;
    private final Handler handler = new Handler();
    private int hintsShown = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvHint = findViewById(R.id.tvHint);
        hints = new String[]{
                "استيقظ مبكرًا لتنظم يومك",
                "الصباح هو بداية النجاح",
                "كل صباح فرصة جديدة",
                "تنظيم الوقت سر النجاح",
                "ابدأ يومك بطاقة إيجابية"
        };

        showRandomHint();

        for (int i = 1; i <= 3; i++) {
            handler.postDelayed(this::showRandomHint, 2000L * i);
        }
        handler.postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 6000);


        handler.postDelayed(() -> {
            hintsShown++;
            if (hintsShown == 2) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 4000);
    }

    private void showRandomHint() {
        Random random = new Random();
        int randomIndex = random.nextInt(hints.length);
        tvHint.setText(hints[randomIndex]);
    }
}

