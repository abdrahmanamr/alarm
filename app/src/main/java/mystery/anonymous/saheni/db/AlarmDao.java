package mystery.anonymous.saheni.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insertAlarm(AlarmEntity alarm);

    @Update
    void updateAlarm(AlarmEntity alarm);

    @Delete
    void deleteAlarm(AlarmEntity alarm);

    @Query("SELECT * FROM alarms ORDER BY isActive DESC, ringTime ASC")
    LiveData<List<AlarmEntity>> getAllAlarms();

    @Query("SELECT * FROM alarms WHERE isActive = 1")
    List<AlarmEntity> getActiveAlarmsDirect();

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    AlarmEntity getAlarmById(int alarmId);
}
