package ru.arvalon.rx.binding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.arvalon.rx.R;

public class RXBindingExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding_example);

        Button button = findViewById(R.id.button);

        RxView.clicks(button).throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> Toast.makeText(
                        this,
                        "Avoid multiple clicks using throttleFirst",
                        Toast.LENGTH_SHORT).show());
    }
}