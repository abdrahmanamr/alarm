package mystery.anonymous.saheni.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class StepCounterService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private int initialCount = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int count = (int) event.values[0];
        if (initialCount < 0) {
            initialCount = count;
        }
        int steps = count - initialCount;
        Intent i = new Intent("STEP_UPDATE");
        i.putExtra("steps", steps);
        sendBroadcast(i);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
