package mystery.anonymous.saheni.ui;

import android.app.TimePickerDialog;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;
import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;
import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;
import mystery.anonymous.saheni.repository.AlarmRepository;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class AlarmAddEditActivity extends AppCompatActivity {

    private AlarmViewModel alarmViewModel;
    private AlarmRepository alarmRepository;
    private EditText etTime, etNote, etDates;
    private Button btnSelectTone, btnAddDate, btnSave, btnDelete;
    private Switch[] switchesDays;
    private Uri selectedToneUri;
    private int alarmId = -1;
    private final int RINGTONE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_edit);

        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmRepository = new AlarmRepository(this);

        etTime = findViewById(R.id.etTime);
        etNote = findViewById(R.id.etNote);
        etDates = findViewById(R.id.etDates);
        btnSelectTone = findViewById(R.id.btnSelectTone);
        btnAddDate = findViewById(R.id.btnAddDate);
        btnSave = findViewById(R.id.btnSaveAlarm);
        btnDelete = findViewById(R.id.btnDeleteAlarm);

        switchesDays = new Switch[7];
        switchesDays[0] = findViewById(R.id.switchSunday);
        switchesDays[1] = findViewById(R.id.switchMonday);
        switchesDays[2] = findViewById(R.id.switchTuesday);
        switchesDays[3] = findViewById(R.id.switchWednesday);
        switchesDays[4] = findViewById(R.id.switchThursday);
        switchesDays[5] = findViewById(R.id.switchFriday);
        switchesDays[6] = findViewById(R.id.switchSaturday);

        // ضبط الوقت الافتراضي 6:00 صباحاً
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        updateTimeField(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(AlarmAddEditActivity.this, (TimePicker view, int hourOfDay, int minute) -> {
                updateTimeField(hourOfDay, minute);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
        });

        // اختيار النغمة عبر Ringtone Picker
        selectedToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        btnSelectTone.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "اختر نغمة الرنين");
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedToneUri);
            startActivityForResult(intent, RINGTONE_REQUEST_CODE);
        });

        // عند الضغط على زر "إضافة تاريخ" يتم فتح DatePicker لاختيار تاريخ وإضافته إلى الحقل
        btnAddDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmAddEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar chosenDate = Calendar.getInstance();
                    chosenDate.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String dateStr = sdf.format(chosenDate.getTime());
                    String currentDates = etDates.getText().toString().trim();
                    if (!currentDates.isEmpty()) {
                        currentDates += ",";
                    }
                    etDates.setText(currentDates + dateStr);
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // تحميل بيانات المنبه في حالة التعديل
        if (getIntent().hasExtra("alarmId")) {
            alarmId = getIntent().getIntExtra("alarmId", -1);
            loadAlarmData(alarmId);
            btnDelete.setVisibility(Button.VISIBLE);
        } else {
            btnDelete.setVisibility(Button.GONE);
        }

        btnSave.setOnClickListener(v -> {
            try {
                AlarmEntity alarm = new AlarmEntity();
                if (alarmId != -1) alarm.id = alarmId;

                Calendar cal = Calendar.getInstance();
                String[] timeParts = etTime.getText().toString().split(":");
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0].trim()));
                cal.set(Calendar.MINUTE, Integer.parseInt(timeParts[1].trim()));
                alarm.ringTime = cal.getTimeInMillis();

                StringBuilder daysBuilder = new StringBuilder();
                for (int i = 0; i < switchesDays.length; i++) {
                    daysBuilder.append(switchesDays[i].isChecked());
                    if (i < switchesDays.length - 1) daysBuilder.append(",");
                }
                alarm.repeatDays = daysBuilder.toString();

                alarm.tone = selectedToneUri.toString();
                alarm.note = etNote.getText().toString();
                alarm.isActive = true;
                alarm.specificDates = etDates.getText().toString().trim();

                if (alarmId == -1) {
                    alarmViewModel.addAlarm(alarm);
                } else {
                    alarmViewModel.updateAlarm(alarm);
                }
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnDelete.setOnClickListener(v -> {
            try {
                if (alarmId != -1) {
                    AlarmEntity alarm = new AlarmEntity();
                    alarm.id = alarmId;
                    alarmViewModel.deleteAlarm(alarm);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateTimeField(int hour, int minute) {
        etTime.setText(String.format("%02d:%02d", hour, minute));
    }

    private void loadAlarmData(int alarmId) {
        new Thread(() -> {
            AlarmEntity alarm = alarmRepository.getAlarmById(alarmId);
            if (alarm != null) {
                runOnUiThread(() -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(alarm.ringTime);
                    updateTimeField(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
                    etNote.setText(alarm.note);
                    etDates.setText(alarm.specificDates);

                    String[] days = alarm.repeatDays.split(",");
                    for (int i = 0; i < days.length && i < switchesDays.length; i++) {
                        switchesDays[i].setChecked(Boolean.parseBoolean(days[i]));
                    }
                    selectedToneUri = Uri.parse(alarm.tone);
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RINGTONE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if(uri != null) {
                selectedToneUri = uri;
                Toast.makeText(this, "تم اختيار النغمة", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

