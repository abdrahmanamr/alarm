package mystery.anonymous.saheni.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import mystery.anonymous.saheni.R;
import mystery.anonymous.saheni.model.Alarm.AlarmEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<AlarmEntity> alarmList = new ArrayList<>();
    private final OnAlarmActionListener listener;
    private final Context context;

    public AlarmAdapter(Context context, OnAlarmActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAlarms(List<AlarmEntity> alarms) {
        this.alarmList = alarms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        try {
            AlarmEntity alarm = alarmList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            holder.tvAlarmTime.setText(sdf.format(new Date(alarm.ringTime)));
            holder.tvAlarmNote.setText(alarm.note);

            // زر حذف المنبه
            holder.btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClicked(alarm);
                }
            });

            // عند الضغط على العنصر يتم فتح شاشة التعديل
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClicked(alarm);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return alarmList == null ? 0 : alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlarmTime, tvAlarmNote;
        Button btnDelete;
        CardView cardAlarm;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlarmTime = itemView.findViewById(R.id.tvAlarmTime);
            tvAlarmNote = itemView.findViewById(R.id.tvAlarmNote);
            btnDelete = itemView.findViewById(R.id.btnDeleteAlarm);
            cardAlarm = itemView.findViewById(R.id.cardAlarm);
        }
    }

    public interface OnAlarmActionListener {
        void onDeleteClicked(AlarmEntity alarm);
        void onItemClicked(AlarmEntity alarm);
    }
}
