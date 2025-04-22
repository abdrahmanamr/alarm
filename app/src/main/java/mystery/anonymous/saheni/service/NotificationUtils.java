package mystery.anonymous.saheni.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import mystery.anonymous.saheni.ui.MainActivity;

public class NotificationUtils {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendAlarmScheduledNotification(Context context, AlarmEntity alarm, long timeRemaining) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.notification_channel_id),
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(context.getString(R.string.notification_channel_desc));
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            String contentText = "المنبه سوف يرن بعد " + (timeRemaining / 60000) + " دقيقة";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("تنبيه منبه!")
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND);

            if (manager != null) {
                manager.notify(alarm.id, builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
