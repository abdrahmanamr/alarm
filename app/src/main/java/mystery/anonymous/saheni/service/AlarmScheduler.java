package mystery.anonymous.saheni.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import mystery.anonymous.saheni.db.AlarmDatabase;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import mystery.anonymous.saheni.db.AlarmDao;
import android.os.Build;
import android.util.Log;

public class AlarmScheduler {

    public static void scheduleAlarms(Context context) {
        try {
            AlarmDao dao = AlarmDatabase.getInstance(context).alarmDao();
            for (AlarmEntity alarm : dao.getActiveAlarmsDirect()) {
                scheduleSingleAlarm(context, alarm);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleSingleAlarm(Context context, AlarmEntity alarm) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("alarmId", alarm.id);
            // يمكن تمرير نغمة الرنين والملاحظات إذا كانت مطلوبة
            intent.putExtra("tone", alarm.tone);
            intent.putExtra("note", alarm.note);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            long scheduledTime = alarm.ringTime;
            long currentTime = System.currentTimeMillis();
            if (scheduledTime < currentTime) {
                // إذا كان الوقت في الماضي، إضافة 24 ساعة
                scheduledTime += 24 * 60 * 60 * 1000;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, scheduledTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduledTime, pendingIntent);
            }
            Log.d("AlarmScheduler", "Scheduling alarm for time: " + scheduledTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelAlarm(Context context, int alarmId) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
