package mystery.anonymous.saheni.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

public class PermissionUtils {
    private static final int REQ_OVERLAY = 100;
    private static final int REQ_DND = 101;
    private static final int REQ_NOTIF = 102;
    private static final int REQ_ACTIVITY = 103;

    public static void checkAndRequestAll(Activity act) {
        if (!Settings.canDrawOverlays(act)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + act.getPackageName()));
            act.startActivityForResult(intent, REQ_OVERLAY);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationHelper.requestDoNotDisturbPermission(act, REQ_DND);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(act,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIF);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(act,
                    new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, REQ_ACTIVITY);
        }
    }
}