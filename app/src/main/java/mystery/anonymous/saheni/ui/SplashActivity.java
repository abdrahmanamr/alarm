package mystery.anonymous.saheni.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;

import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private List<String> hints = Arrays.asList(
            "ابدأ يومك بنشاط!",
            "التحدي يقوي الذاكرة.",
            "النوم مبكرًا وصحي.",
            "لا تنسى تناول الماء.",
            "مستوى التحدي سيزيد قواك.",
            "الرياضة تجعل العقل يقظًا.",
            "التحدي يقوي التركيز.",
            "ابدأ بقوة وجدية.",
            "التحديات تصنع الأبطال.",
            "كن مستعدًا للفوز!"
    );
    private int idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        for (int i = 0; i < 6; i++) {
            handler.postDelayed(() -> {
                findViewById(R.id.tvHint).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tvHint)).setText(
                        hints.get(idx % hints.size()));
                idx++;
            }, i * 1000 * 1);
        }
        handler.postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 6000);
    }
}