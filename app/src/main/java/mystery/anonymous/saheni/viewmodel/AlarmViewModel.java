package mystery.anonymous.saheni.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import mystery.anonymous.saheni.repository.AlarmRepository;
import java.util.List;

public class AlarmViewModel extends AndroidViewModel {
    private AlarmRepository repository;
    private LiveData<List<AlarmEntity>> alarmsLiveData;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);
        alarmsLiveData = repository.getAllAlarms();
    }

    public LiveData<List<AlarmEntity>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    public void addAlarm(AlarmEntity alarm) {
        repository.insertAlarm(alarm);
    }

    public void updateAlarm(AlarmEntity alarm) {
        repository.updateAlarm(alarm);
    }

    public void deleteAlarm(AlarmEntity alarm) {
        repository.deleteAlarm(alarm);
    }
}

