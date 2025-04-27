package mystery.anonymous.saheni.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import mystery.anonymous.saheni.repository.AlarmRepository;
import mystery.anonymous.saheni.viewmodel.AlarmViewModel;

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

        // تهيئة ViewModel و Repository
        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmRepository = new AlarmRepository(this);

        // ربط الواجهات
        etTime = findViewById(R.id.etTime);
        etNote = findViewById(R.id.etNote);
        etDates = findViewById(R.id.etDates);
        btnSelectTone = findViewById(R.id.btnSelectTone);
        btnAddDate = findViewById(R.id.btnAddDate);
        btnSave = findViewById(R.id.btnSaveAlarm);
        btnDelete = findViewById(R.id.btnDeleteAlarm);

        switchesDays = new Switch[] {
                findViewById(R.id.switchSunday),
                findViewById(R.id.switchMonday),
                findViewById(R.id.switchTuesday),
                findViewById(R.id.switchWednesday),
                findViewById(R.id.switchThursday),
                findViewById(R.id.switchFriday),
                findViewById(R.id.switchSaturday)
        };

        // ضبط الوقت الافتراضي 06:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        updateTimeField(6, 0);

        // اختيار الوقت باستخدام TimePickerDialog بتنسيق 24h
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            boolean is24Hour = DateFormat.is24HourFormat(this);
            new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> updateTimeField(hourOfDay, minute),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    is24Hour
            ).show();
        });

        // اختيار نغمة النظام
        selectedToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        btnSelectTone.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "اختر نغمة الرنين");
            startActivityForResult(intent, RINGTONE_REQUEST_CODE);
        });

        // اختيار تواريخ من Calendar مع منع الماضي
        btnAddDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar chosen = Calendar.getInstance();
                        chosen.set(year, month, dayOfMonth);
                        String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(chosen.getTime());
                        String existing = etDates.getText().toString().trim();
                        etDates.setText(existing.isEmpty() ? dateStr : existing + "," + dateStr);
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker()
                    .setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        if(etTime.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"برجاء اختيار الوقت",Toast.LENGTH_SHORT).show();

        }
        if(etNote.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"برجاء إدخال ملاحظة",Toast.LENGTH_SHORT).show();

        }

        // تحميل بيانات المنبه للمراجعة/تعديل
        if (getIntent().hasExtra("alarmId")) {
            alarmId = getIntent().getIntExtra("alarmId", -1);
            loadAlarmData(alarmId);
            btnDelete.setVisibility(Button.VISIBLE);
        } else {
            btnDelete.setVisibility(Button.GONE);
        }

        // الحفظ مع التحقق من المدخلات
        btnSave.setOnClickListener(v -> {
            if (!validateInputs()) return;
            try {
                AlarmEntity alarm = new AlarmEntity();
                if (alarmId != -1) alarm.id = alarmId;

                // ضبط وقت الرنين
                Calendar cal = Calendar.getInstance();
                String[] parts = etTime.getText().toString().split(":");
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0].trim()));
                cal.set(Calendar.MINUTE, Integer.parseInt(parts[1].trim()));
                alarm.ringTime = cal.getTimeInMillis();

                // أيام التكرار
                StringBuilder days = new StringBuilder();
                for (int i = 0; i < switchesDays.length; i++) {
                    days.append(switchesDays[i].isChecked());
                    if (i < switchesDays.length - 1) days.append(",");
                }
                alarm.repeatDays = days.toString();

                // باقي الخصائص
                alarm.tone = selectedToneUri.toString();
                alarm.note = etNote.getText().toString().trim();
                alarm.isActive = true;
                alarm.specificDates = etDates.getText().toString().trim();

                if (alarmId == -1) alarmViewModel.addAlarm(alarm);
                else alarmViewModel.updateAlarm(alarm);

                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // الحذف
        btnDelete.setOnClickListener(v -> {
            if (alarmId != -1) {
                AlarmEntity alarm = new AlarmEntity();
                alarm.id = alarmId;
                alarmViewModel.deleteAlarm(alarm);
                finish();
            }
        });
    }

    // تحديث حقل الوقت
    private void updateTimeField(int hour, int minute) {
        etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
    }

    // تحميل بيانات المنبه
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

    // التعامل مع نتيجة اختيار النغمة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RINGTONE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                selectedToneUri = uri;
                Toast.makeText(this, "تم اختيار النغمة", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // دالة التحقق من صحة المدخلات
    private boolean validateInputs() {
        if (etTime.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "برجاء اختيار الوقت", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etNote.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "برجاء إدخال ملاحظة", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
