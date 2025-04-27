package mystery.anonymous.saheni.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class StepChallengeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int target, startCount;

    @Override
    protected void onCreate(@Nullable Bundle s) {
        super.onCreate(s);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        target = new Random().nextInt(6) + 5;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (startCount == 0) startCount = (int) event.values[0];
        int steps = (int) event.values[0] - startCount;
        if (steps >= target) {
            ((AlarmRingActivity) getParent()).stopAlarm();
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
