package mystery.anonymous.saheni.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import mystery.anonymous.saheni.R;

public class AlarmTestActivity extends AppCompatActivity {

    private TextView tvTestInstruction;
    private Button btnTestAction;
    private int testType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alarm_test);

        tvTestInstruction = findViewById(R.id.tvTestInstruction);
        btnTestAction = findViewById(R.id.btnTestAction);

        // اختيار اختبار عشوائي من بين خمسة اختبارات
        Random random = new Random();
        testType = random.nextInt(5);
        switch (testType) {
            case 0:
                tvTestInstruction.setText("امشِ 20 خطوة");
                break;
            case 1:
                tvTestInstruction.setText("حل 3 مسائل رياضية بسيطة");
                break;
            case 2:
                tvTestInstruction.setText("اضغط على الأزرار بالترتيب الصحيح للحصول على نقاط");
                break;
            case 3:
                tvTestInstruction.setText("اختبار إضافي 1: اضغط على الزر الذي يظهر عشوائياً");
                break;
            case 4:
                tvTestInstruction.setText("اختبار إضافي 2: حل اللغز البسيط المعروض");
                break;
        }

        btnTestAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // بعد اجتياز الاختبار، فتح شاشة الرنين
                Intent intent = new Intent(AlarmTestActivity.this, AlarmRingActivity.class);
                intent.putExtra("time", "06:00 AM");
                intent.putExtra("note", "استيقظت!");
                // تمرير النغمة يمكن تعديلها حسب الحاجة
                intent.putExtra("tone", "");
                intent.putExtra("isRepeat", false);
                startActivity(intent);
                finish();
            }
        });
    }
}
