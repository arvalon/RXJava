package ru.arvalon.rx.chapter1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.arvalon.rx.R;

public class RxTextViewActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_text_view);

        EditText ed = findViewById(R.id.ch1_ed);
        tv = findViewById(R.id.ch1_tv);

        RxTextView.textChanges(ed)
                .filter(text->text.length()>2)
                .debounce(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateTextView);
    }

    private void updateTextView(CharSequence charSequence){
        tv.setText(charSequence.toString());
    }
}