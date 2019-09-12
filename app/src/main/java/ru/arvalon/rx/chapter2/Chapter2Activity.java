package ru.arvalon.rx.chapter2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter2.network.Entry;
import ru.arvalon.rx.chapter2.network.FeedObservable;

public class Chapter2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2);

        List<String> feedUrls = Arrays.asList(
                "https://news.google.com/?output=atom",
                "http://www.theregister.co.uk/software/headlines.atom");
                //"http://www.linux.com/news/soware?format=feed&type=atom" // xml parsing error

        List<Observable<List<Entry>>> observableList = new ArrayList<>();

        for (String feedUrl : feedUrls) {

            final Observable<List<Entry>> feedObservable =
                    FeedObservable.getFeed(feedUrl).retry(3).onErrorReturn(e -> new ArrayList<>());

            observableList.add(feedObservable);
        }

        Observable<List<Entry>> combinedObservable =
                Observable.combineLatest(observableList,
                        (listOfLists) -> {
                            final List<Entry> combinedList = new ArrayList<>();
                            for (Object list : listOfLists) {
                                combinedList.addAll((List<Entry>) list);
                            }
                            return combinedList;
                        }
                );

        combinedObservable
                .map(list -> {
                    List<Entry> sortedList = new ArrayList<>();
                    sortedList.addAll(list);
                    Collections.sort(sortedList);
                    return sortedList;
                })
                .flatMap(Observable::<Entry>fromIterable)
                .take(7)
                .map(Entry::toString)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::drawList);
    }

    private void drawList(List<String> listItems) {

        final ListView list = findViewById(R.id.rss_list);

        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        list.setAdapter(itemsAdapter);
    }
}
