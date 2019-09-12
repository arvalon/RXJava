package ru.arvalon.rx.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import ru.arvalon.rx.MainActivity;
import ru.arvalon.rx.R;

public class ReactiveSensorActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button bSwitchOn;
    private Button bSwitchOff;
    private TextView tvDisplay;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactive_sensor);

        bSwitchOn = findViewById(R.id.bSwitchOn);
        tvDisplay = findViewById(R.id.tvDisplay);
        bSwitchOff = findViewById(R.id.bSwitchOff);

        bSwitchOn.setOnClickListener(this);
        bSwitchOff.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bSwitchOn:

                disposables.add(
                        Observable.create(new ReactiveSensor(this))

                                .subscribe(d -> {

                                    String data = String.format(Locale.getDefault(),
                                            "x =  %f, y: %f, z: %f", d[0], d[1], d[2]);

                                    Log.d(MainActivity.TAG, data);

                                    tvDisplay.setText(data);
                                })
                );

                return;

            case R.id.bSwitchOff:
                disposables.clear();
        }
    }
}
