package mystery.anonymous.saheni.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlarmReceiver", "Alarm received for id: " + intent.getIntExtra("alarmId", -1));

        // فتح شاشة الاختبار أو شاشة الرنين
        Intent testIntent = new Intent(context, mystery.anonymous.saheni.ui.AlarmTestActivity.class);
        testIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(testIntent);
    }
}
