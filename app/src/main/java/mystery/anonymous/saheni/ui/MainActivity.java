package mystery.anonymous.saheni.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.utils.PermissionUtils;
import mystery.anonymous.saheni.utils.NotificationHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Check and request all required permissions
        PermissionUtils.checkAndRequestAll(this);

        // 2. Create notification channel for app notifications
        NotificationHelper.createNotificationChannel(this);

        // Launch alarm setup screen
        startActivity(new Intent(this, AlarmSetupActivity.class));
        finish();
    }
}