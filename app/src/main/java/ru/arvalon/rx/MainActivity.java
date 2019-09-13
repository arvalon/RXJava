package ru.arvalon.rx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.reactivex.disposables.Disposable;
import ru.arvalon.rx.binding.RXBindingExample;
import ru.arvalon.rx.books.BooksActivity;
import ru.arvalon.rx.chapter1.RxTextViewActivity;
import ru.arvalon.rx.chapter13.Chapter13Activity;
import ru.arvalon.rx.chapter14.Chapter14Activity;
import ru.arvalon.rx.chapter2.Chapter2Activity;
import ru.arvalon.rx.chapter3.Chapter3Activity;
import ru.arvalon.rx.chapter4.Chapter4Activity;
import ru.arvalon.rx.chapter4.Chapter4aActivity;
import ru.arvalon.rx.chapter5.Chapter5Activity;
import ru.arvalon.rx.chapter6.Chapter6Activity;
import ru.arvalon.rx.chapter7.Chapter7Activity;
import ru.arvalon.rx.chapter8.Chapter8Activity;
import ru.arvalon.rx.color.ColorsActivity;
import ru.arvalon.rx.editoraction.EditorActionActivity;
import ru.arvalon.rx.sensor.ReactiveSensorActivity;
import ru.arvalon.rx.simple.RxJavaSimpleActivity;
import ru.arvalon.rx.schediler.SchedulerActivity;

public class MainActivity extends AppCompatActivity {

    public static final String LOGTAG = "rx4.logs";

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOGTAG,"onCreate");

        /*final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
            SystemClock.sleep(3000);
            emitter.onNext(3);
            emitter.onComplete();
        });

         disposable = serverDownloadObservable.subscribe(integer -> {Log.d(TAG,"Integer = "+integer);});*/
    }

    public void onClick(View view) {

        Intent i = null;
        switch (view.getId()) {

            case R.id.first:
                i = new Intent(this, RxJavaSimpleActivity.class);
                break;

            case R.id.second:
                i = new Intent(this, ColorsActivity.class);
                break;

            case R.id.third:
                i = new Intent(this, BooksActivity.class);
                break;

            case R.id.four:
                i = new Intent(this, SchedulerActivity.class);
                break;

            case R.id.rxbinding_btn:
                i = new Intent(this, RXBindingExample.class);
                break;

            case R.id.reactive_sensor_btn:
                i = new Intent(this,ReactiveSensorActivity.class);
                break;

            case R.id.editor_action_btn:
                i = new Intent(this, EditorActionActivity.class);
                break;

            case R.id.ch1_rxtextview_btn:
                i = new Intent(this,RxTextViewActivity.class);
                break;

            case R.id.ch2_network_search_btn:
                i = new Intent(this, Chapter2Activity.class);
                break;

            case R.id.ch3_btn:
                i = new Intent(this, Chapter3Activity.class);
                break;

            case R.id.ch4_btn:
                i = new Intent(this, Chapter4Activity.class);
                break;

            case R.id.ch4a_btn:
                i = new Intent(this, Chapter4aActivity.class);
                break;

            case R.id.ch5_btn:
                i = new Intent(this, Chapter5Activity.class);
                break;

            case R.id.ch6_btn:
                i = new Intent(this, Chapter6Activity.class);
                break;

            case R.id.ch7_btn:
                i = new Intent(this, Chapter7Activity.class);
                break;

            case R.id.ch8_btn:
                i = new Intent(this, Chapter8Activity.class);
                break;

            case R.id.ch9_btn:
                i = new Intent(this, Chapter8Activity.class);
                break;

            case R.id.ch13_btn:
                i = new Intent(this, Chapter13Activity.class);
                break;

            case R.id.ch14_btn:
                i = new Intent(this, Chapter14Activity.class);
                break;
        }

        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}