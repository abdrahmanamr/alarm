package mystery.anonymous.saheni.ui;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.adapter.AlarmAdapter;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import mystery.anonymous.saheni.service.AlarmScheduler;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;


public class MainActivity extends BasePermissionsActivity implements AlarmAdapter.OnAlarmActionListener {

    private AlarmViewModel alarmViewModel;
    private AlarmAdapter adapter;
    private RecyclerView rvAlarms;
    private ImageButton btnAddAlarm, btnToggleTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
        try {
            setContentView(R.layout.activity_main);
            requestAllNeededPermissions();

            alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

            rvAlarms = findViewById(R.id.rvAlarms);
            rvAlarms.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AlarmAdapter(this, this);
            rvAlarms.setAdapter(adapter);

            btnAddAlarm = findViewById(R.id.btnAddAlarm);

            btnAddAlarm.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, AlarmAddEditActivity.class));
            });


            alarmViewModel.getAlarmsLiveData().observe(this, new Observer<List<AlarmEntity>>() {
                @Override
                public void onChanged(List<AlarmEntity> alarmEntities) {
                    adapter.setAlarms(alarmEntities);
                }
            });

            // جدولة المنبهات
            AlarmScheduler.scheduleAlarms(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteClicked(AlarmEntity alarm) {
        try {
            alarmViewModel.deleteAlarm(alarm);
            AlarmScheduler.cancelAlarm(MainActivity.this, alarm.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] needed = { /* قائمة الصلاحيات المهمة */ };
        List<String> missing = Arrays.stream(needed)
                .filter(p -> checkSelfPermission(p)!=GRANTED)
                .collect(Collectors.toList());
        if (!missing.isEmpty()) {
            // إنشاء إشعار يحتوي زر لإعادة طلب الصلاحيات عبر Intent إلى BasePermissionsActivity
        }

    }

    @Override
    public void onItemClicked(AlarmEntity alarm) {
        try {
            Intent intent = new Intent(MainActivity.this, AlarmAddEditActivity.class);
            intent.putExtra("alarmId", alarm.id);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
