package mystery.anonymous.saheni.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import mystery.anonymous.saheni.db.AlarmDao;
import mystery.anonymous.saheni.db.AlarmDatabase;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AlarmRepository {
    private AlarmDao alarmDao;

    public AlarmRepository(Context context) {
        AlarmDatabase db = AlarmDatabase.getInstance(context);
        alarmDao = db.alarmDao();
    }

    public LiveData<List<AlarmEntity>> getAllAlarms() {
        return alarmDao.getAllAlarms();
    }

    public void insertAlarm(AlarmEntity alarm) {
        Executors.newSingleThreadExecutor().execute(() -> {
            alarmDao.insertAlarm(alarm);
        });
    }

    public void updateAlarm(AlarmEntity alarm) {
        Executors.newSingleThreadExecutor().execute(() -> {
            alarmDao.updateAlarm(alarm);
        });
    }

    public void deleteAlarm(AlarmEntity alarm) {
        Executors.newSingleThreadExecutor().execute(() -> {
            alarmDao.deleteAlarm(alarm);
        });
    }

    // استرجاع منبه بناءً على المعرف
    public AlarmEntity getAlarmById(int alarmId) {
        try {
            Future<AlarmEntity> future = Executors.newSingleThreadExecutor().submit(new Callable<AlarmEntity>() {
                @Override
                public AlarmEntity call() {
                    return alarmDao.getAlarmById(alarmId);
                }
            });
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
