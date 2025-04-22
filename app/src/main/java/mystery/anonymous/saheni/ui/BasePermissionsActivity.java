package mystery.anonymous.saheni.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BasePermissionsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private static final int REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = 124;
    private static final int REQUEST_OVERLAY_PERMISSION = 125;
    private static final int REQUEST_NOTIFICATION_POLICY = 126;

    protected void requestAllNeededPermissions() {
        try {
            List<String> permissionsToRequest = new ArrayList<>();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.SCHEDULE_EXACT_ALARM);
                }
            }

            if (checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.WAKE_LOCK);
            }

            if (checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE);
            }

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }

            // طلب صلاحية DND (عدم الإزعاج)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!getSystemService(android.app.NotificationManager.class).isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivityForResult(intent, REQUEST_NOTIFICATION_POLICY);
                }
            }

            // طلب صلاحية تجاهل تحسين البطارية
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                }
            }

            // طلب صلاحية الظهور فوق التطبيقات
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                }
            }

            if (!permissionsToRequest.isEmpty()) {
                requestPermissions(permissionsToRequest.toArray(new String[0]), REQUEST_CODE_PERMISSIONS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // يمكنك معالجة النتائج هنا إذا احتجت
    }
}
