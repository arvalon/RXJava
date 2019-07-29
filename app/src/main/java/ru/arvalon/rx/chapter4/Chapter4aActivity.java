package ru.arvalon.rx.chapter4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.SerialDisposable;
import ru.arvalon.rx.R;

public class Chapter4aActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4a);

        TextView textView1 = findViewById(R.id.textView1);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);

        Observable<String> obs = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(i->Long.toString(i))
                .observeOn(AndroidSchedulers.mainThread());

        SerialDisposable s = new SerialDisposable();

        RxView.clicks(textView1).subscribe(ev->{s.set(obs.subscribe(textView1::setText));});
        RxView.clicks(textView2).subscribe(ev->{s.set(obs.subscribe(textView2::setText));});
        RxView.clicks(textView3).subscribe(ev->{s.set(obs.subscribe(textView3::setText));});
    }
}
