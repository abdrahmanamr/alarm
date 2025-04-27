package mystery.anonymous.saheni.ui;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MathQuizActivity extends AppCompatActivity {

    private int correctCount = 0, total = 5;
    private Random rnd = new Random();
    private int usedMulDiv = 0;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        playNext();
    }

    private void playNext() {
        if (correctCount >= total) {
            ((AlarmRingActivity) getParent()).stopAlarm();
            return;
        }
        int a = rnd.nextInt(900000) + 100000;
        int b = rnd.nextInt(900000) + 100000;
        char op;
        if (usedMulDiv < 2) {
            if (rnd.nextBoolean()) {
                op = rnd.nextBoolean() ? '×' : '÷';
                usedMulDiv++;
            } else op = rnd.nextBoolean() ? '+' : '−';
        } else {
            op = rnd.nextBoolean() ? '+' : '−';
        }

        int result = switch (op) {
            case '+' -> a + b;
            case '−' -> a - b;
            case '×' -> a * b;
            default -> b == 0 ? 0 : a / b;
        };

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(this);
        EditText et = new EditText(this);
        Button bt = new Button(this);
        tv.setText(a + " " + op + " " + b + " = ?");
        bt.setText("تحقق");
        bt.setOnClickListener(v -> {
            try {
                if (Integer.parseInt(et.getText().toString()) == result) {
                    correctCount++;
                    playNext();
                } else {
                    Toast.makeText(this, "خطأ، حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { /* ignore */ }
        });

        layout.addView(tv); layout.addView(et); layout.addView(bt);
        setContentView(layout);
    }
}
