package ru.arvalon.rx.chapter2.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static ru.arvalon.rx.MainActivity.TAG;

public class FeedObservable {

    private FeedObservable() {

    }

    public static Observable<List<Entry>> getFeed(final String url) {
        return RawNetworkObservable.create(url)
                .map(response -> {
                    FeedParser parser = new FeedParser();
                    try {
                        List<Entry> entries = parser.parse(response.body().byteStream());
                        Log.v(TAG, "Number of entries from url " + url + ": " + entries.size());
                        return entries;
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing feed", e);
                    }
                    return new ArrayList<>();
                });
    }
}
