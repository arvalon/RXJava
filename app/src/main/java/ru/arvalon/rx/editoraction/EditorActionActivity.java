package ru.arvalon.rx.editoraction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import ru.arvalon.rx.R;

import static ru.arvalon.rx.MainActivity.LOGTAG;

public class EditorActionActivity extends AppCompatActivity {

    private static final String TITLE = "Editor Action";

    CompositeDisposable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_action);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(TITLE);
        }

        EditText ed = findViewById(R.id.edit_text);

        ed.setImeOptions(EditorInfo.IME_ACTION_GO);
        ed.setInputType(EditorInfo.IME_ACTION_GO);

        d = new CompositeDisposable();

        Predicate<TextViewEditorActionEvent> predicate = (event ->
                event.actionId() == EditorInfo.IME_ACTION_GO
                || (event.keyEvent().getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.keyEvent().getAction() == KeyEvent.ACTION_UP ));

        /*Disposable editorActionEvents = RxTextView.editorActionEvents(ed)
                .filter(predicate)
                .map(event -> ed.getText().toString())
                .subscribe(text -> {
                    Log.d(LOGTAG, "Text "+text);
        }
        );

        d.add(editorActionEvents);*/

        Observable<CharSequence> editorActionObservable = RxTextView.editorActionEvents(ed)
                .filter(predicate)
                .map(event -> ed.getText());

        Disposable textChangedEvent =  RxTextView.textChanges(ed)
                .debounce(2, TimeUnit.SECONDS)
                .filter(text -> text.length() > 2)
                //.skip(1)
                .mergeWith(editorActionObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> Log.d(LOGTAG, "Search string "+charSequence));

        d.add(textChangedEvent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        d.dispose();
    }
}
