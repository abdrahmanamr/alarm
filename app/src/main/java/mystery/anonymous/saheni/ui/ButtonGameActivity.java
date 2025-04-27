package mystery.anonymous.saheni.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ButtonGameActivity extends AppCompatActivity {

    private FrameLayout container;
    private int score = 0;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        container = new FrameLayout(this);
        setContentView(container);

        spawnButton();
    }

    private void spawnButton() {
        container.removeAllViews();
        Button btn = new Button(this);
        int pts = new int[]{5, 1, -1}[new Random().nextInt(3)];
        btn.setText(String.valueOf(pts));
        btn.setOnClickListener(v -> {
            score += pts;
            if (score >= 50) {
                ((AlarmRingActivity) getParent()).stopAlarm();
            } else {
                spawnButton();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.leftMargin = new Random().nextInt(size.x - 200);
        lp.topMargin  = new Random().nextInt(size.y - 200);
        container.addView(btn, lp);
    }
}
