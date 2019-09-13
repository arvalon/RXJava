package ru.arvalon.rx.chapter4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import ru.arvalon.rx.BuildConfig;
import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter4.network.FlickrApi;
import ru.arvalon.rx.chapter4.network.FlickrPhotoInfoResponse;
import ru.arvalon.rx.chapter4.network.FlickrPhotosGetSizesResponse;
import ru.arvalon.rx.chapter4.network.FlickrSearchResponse;
import ru.arvalon.rx.chapter4.pojo.Photo;

import static ru.arvalon.rx.MainActivity.LOGTAG;

public class Chapter4Activity extends FragmentActivity {

    private static final String FLICKR_API_ENDPOINT = "https://api.flickr.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4);

        final Retrofit restAdapter = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FLICKR_API_ENDPOINT)
                .build();

        final FlickrApi api = restAdapter.create(FlickrApi.class);
        final String apiKey = BuildConfig.FLICKR_API_KEY;

        final Button searchButton = findViewById(R.id.search_button);
        final Observable<Object> buttonClickObservable = RxView.clicks(searchButton);

        final TextView searchTextView = findViewById(R.id.search_text);
        final Observable<String> searchTextInput =
                RxTextView.textChanges(searchTextView).map(CharSequence::toString);

        searchTextInput
                .map(searchText -> searchText.length() >= 3)
                .subscribe(searchButton::setEnabled);

        buttonClickObservable
                .doOnNext(e -> Log.d(LOGTAG, "Search button clicked"))
                .withLatestFrom(searchTextInput, (e, searchText) -> searchText)
                .doOnNext(searchText -> Log.d(LOGTAG, "Start search with '" + searchText + "'"))
                .flatMap(searchText ->
                        api.searchPhotos(apiKey, searchText, 4)
                                .subscribeOn(Schedulers.io()))
                .map(FlickrSearchResponse::getPhotos)
                .doOnNext(photos -> Log.d(LOGTAG, "Found " + photos.size() + " photos to process"))
                .flatMap((Function<List<FlickrSearchResponse.Photo>, Observable<List<Photo>>>) photos -> {
                    if (photos.size() > 0) {
                        return Observable.fromIterable(photos)
                                .doOnNext(photo -> Log.d(LOGTAG, "Processing photo  " + photo.getId()))
                                .concatMap(photo ->
                                        Observable.combineLatest(
                                                api.photoInfo(apiKey, photo.getId())
                                                        .subscribeOn(Schedulers.io())
                                                        .map(FlickrPhotoInfoResponse::getPhotoInfo),
                                                api.getSizes(apiKey, photo.getId())
                                                        .subscribeOn(Schedulers.io())
                                                        .map(FlickrPhotosGetSizesResponse::getSizes),
                                                Photo::createPhoto))
                                .doOnNext(photo -> Log.d(LOGTAG, "Finished processing photo " + photo.getId()))
                                .toList()
                                .doOnSuccess(photo -> Log.d(LOGTAG, "Finished processing all photos"))
                                .toObservable();

                    } else {
                        return Observable.just(new ArrayList<>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photos -> {
                            final RecyclerView rv = findViewById(R.id.main_list);
                            rv.setLayoutManager(new LinearLayoutManager(this));

                            Log.d(LOGTAG, "Found " + photos.size() + " photos");
                            final PhotoAdapter photoAdapter = new PhotoAdapter(this, photos);
                            rv.setAdapter(photoAdapter);
                        },
                        e -> Log.e(LOGTAG, "Error getting photos", e));
    }
}
