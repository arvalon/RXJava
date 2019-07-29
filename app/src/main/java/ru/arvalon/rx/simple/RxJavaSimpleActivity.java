package ru.arvalon.rx.simple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.arvalon.rx.R;
import ru.arvalon.rx.adapter.SimpleStringAdapter;

public class RxJavaSimpleActivity extends AppCompatActivity {

    RecyclerView colorListView;

    SimpleStringAdapter simpleStringAdapter;

    CompositeDisposable disposable = new CompositeDisposable();

    public int value =0;

    final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
        SystemClock.sleep(10000); // simulate delay
        emitter.onNext(5);
        emitter.onComplete();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_simple);

        View view = findViewById(R.id.button);

        view.setOnClickListener(v -> {

            v.setEnabled(false); // disables the button until execution has finished

            Disposable subscribe = serverDownloadObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(integer -> {updateTheUserInterface(integer);
                    v.setEnabled(true);});

            disposable.add(subscribe);

        });
    }

    private void updateTheUserInterface(int integer) {
        TextView view = findViewById(R.id.resultView);
        view.setText(String.valueOf(integer));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void onClick(View view) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show();
    }
}
