package ru.arvalon.rx.chapter2.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static ru.arvalon.rx.MainActivity.LOGTAG;

public class FeedObservable {

    private FeedObservable() {

    }

    public static Observable<List<Entry>> getFeed(final String url) {
        return RawNetworkObservable.create(url)
                .map(response -> {
                    FeedParser parser = new FeedParser();
                    try {
                        List<Entry> entries = parser.parse(response.body().byteStream());
                        Log.v(LOGTAG, "Number of entries from url " + url + ": " + entries.size());
                        return entries;
                    } catch (Exception e) {
                        Log.e(LOGTAG, "Error parsing feed", e);
                    }
                    return new ArrayList<>();
                });
    }
}
