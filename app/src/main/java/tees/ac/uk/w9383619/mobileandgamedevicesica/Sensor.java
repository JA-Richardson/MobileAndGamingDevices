package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Sensor implements SensorEventListener {

    private final SensorManager sensorManager;
    private final android.hardware.Sensor sensor;
    double ax, ay, az;
    public float charge = 0;
    public boolean chargeFull = false;
    public Sensor(Context context)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        final float alpha =  0.1f;

        // Isolate the force of gravity with the low-pass filter.
        float[] gravity = new float[3];
        //gravity[2] = 9.81f;

        gravity[0] = gravity[0] * alpha + sensorEvent.values[0] * (1.f - alpha);
        gravity[1] = gravity[1] * alpha + sensorEvent.values[1] * (1.f - alpha);
        gravity[2] = gravity[2] * alpha + sensorEvent.values[2] * (1.f - alpha);

        float[] linear_acceleration = new float[3];
        linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
        linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
        linear_acceleration[2] = sensorEvent.values[2] - gravity[2];

        if(sensorEvent.sensor.getType()== android.hardware.Sensor.TYPE_ACCELEROMETER)
        {
            ax=linear_acceleration[0];
            ay=linear_acceleration[1];
            az=linear_acceleration[2];
        }
        Log.d("sensor", "X accel: " + ax);
        Log.d("sensor", "Y accel: " + ay);
        Log.d("sensor", "Z accel: " + az);


        if(ax > 0.8)
        {
            charge+=0.1;
            Log.d("charge", "current charge: " + charge);
            if(charge > 100)
            {
                chargeFull = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {

    }

    public void charge()
    {

    }
}
