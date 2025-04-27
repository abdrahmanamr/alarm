package mystery.anonymous.saheni.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import mystery.anonymous.saheni.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "alarm_channel";

    public static void createNotificationChannel(Context ctx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm App Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for alarm setup and permission status");
            NotificationManager nm = ctx.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    public static void showSetupNotification(Context ctx, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat.from(ctx).notify(1, builder.build());
    }

    public static void requestDoNotDisturbPermission(Activity act, int reqCode) {
        NotificationManager nm = act.getSystemService(NotificationManager.class);
        if (!nm.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            act.startActivityForResult(intent, reqCode);
        }
    }
}