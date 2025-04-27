package mystery.anonymous.saheni.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Calendar;

import mystery.anonymous.saheni.service.AlarmReceiver;

public class AlarmHelper {
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleAlarm(Context ctx, long triggerAtMillis, Uri ringtoneUri) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctx, AlarmReceiver.class);
        i.putExtra("ringtone", ringtoneUri.toString());
        PendingIntent pi = PendingIntent.getBroadcast(ctx, (int) triggerAtMillis, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
    }
}
