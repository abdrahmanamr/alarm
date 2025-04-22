package mystery.anonymous.saheni.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;

@Database(entities = {AlarmEntity.class}, version = 1, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();

    private static volatile AlarmDatabase INSTANCE;

    public static AlarmDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AlarmDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AlarmDatabase.class,
                                    "alarms_db"
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
