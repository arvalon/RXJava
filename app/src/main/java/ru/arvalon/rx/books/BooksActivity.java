package ru.arvalon.rx.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.arvalon.rx.R;
import ru.arvalon.rx.adapter.SimpleStringAdapter;

public class BooksActivity extends AppCompatActivity {

    private Disposable bookSubscription;

    private RecyclerView booksRecyclerView;

    private ProgressBar progressBar;

    private SimpleStringAdapter stringAdapter;

    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(this);
        configureLayout();
        createObservable();
    }

    private void createObservable() {

        Observable<List<String>> booksObservable =
                Observable.fromCallable(() -> restClient.getFavoriteBooks());

        bookSubscription = booksObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::displayBooks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookSubscription != null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }

    private void displayBooks(List<String> books) {
        stringAdapter.setStrings(books);
        progressBar.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.VISIBLE);
    }

    private void configureLayout() {
        setContentView(R.layout.activity_books);
        progressBar = findViewById(R.id.loader);
        booksRecyclerView = findViewById(R.id.books_list);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stringAdapter = new SimpleStringAdapter(this);
        booksRecyclerView.setAdapter(stringAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bookSubscription!=null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }
}
