package ru.arvalon.rx.sensor;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

import static ru.arvalon.rx.MainActivity.LOGTAG;

public class ReactiveSensor implements ObservableOnSubscribe<float[]>, Disposable, SensorEventListener
{
    ReactiveSensor(Context context) {
        this.context = context;
    }

    private Context context;

    private ObservableEmitter<float[]> emitter;

    private boolean isDisposed = false;

    @Override
    public void subscribe(ObservableEmitter<float[]> emitter) throws Exception {
        Log.d(LOGTAG, "subscribe");

        this.emitter = emitter;
        this.emitter.setDisposable(this);

        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.unregisterListener(this);
        manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void dispose() {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        manager.unregisterListener(this);
        isDisposed = true;
        Log.d(LOGTAG, "dispose");
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(LOGTAG, "onSensorChanged");
        emitter.onNext(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
