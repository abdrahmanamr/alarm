package mystery.anonymous.saheni.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Alarm {
    @Entity(tableName = "alarms")
    public static class AlarmEntity {
        @PrimaryKey(autoGenerate = true)
        public int id;

        // وقت الرنين (بالملي ثانية)
        public long ringTime;

        // حالة المنبه (مفعل أم لا)
        public boolean isActive;

        // أيام التكرار بصيغة CSV (مثال: "false,true,false,false,true,false,false")
        public String repeatDays;

        // مسار النغمة أو URI
        @NonNull
        public String tone = "";

        // ملاحظة المنبه
        public String note = "";

        // تواريخ محددة (بصيغة CSV، يمكن أن تكون بدلاً من أيام التكرار)
        public String specificDates = "";
    }
}
