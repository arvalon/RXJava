package ru.arvalon.rx.chapter14;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;
import ru.arvalon.rx.chapter14.network.MapNetworkAdapter;
import ru.arvalon.rx.chapter14.network.MapNetworkAdapterSimple;
import ru.arvalon.rx.chapter14.network.NetworkClient;
import ru.arvalon.rx.chapter14.network.NetworkClientOkHttp;
import ru.arvalon.rx.chapter14.network.TileBitmapLoader;
import ru.arvalon.rx.chapter14.utils.CoordinateProjection;
import ru.arvalon.rx.chapter14.utils.LatLng;
import ru.arvalon.rx.chapter14.utils.MapTileUtils;
import ru.arvalon.rx.chapter14.utils.PointD;
import ru.arvalon.rx.chapter14.utils.Triple;
import ru.arvalon.rx.chapter14.utils.ViewUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import ru.arvalon.rx.R;

import static ru.arvalon.rx.MainActivity.TAG;

public class Chapter14Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter14);

        NetworkClient networkClient = new NetworkClientOkHttp();
        MapNetworkAdapter mapNetworkAdapter = new MapNetworkAdapterSimple(networkClient,
                "https://b.tile.openstreetmap.org/%d/%d/%d.png");

        TileBitmapLoader tileBitmapLoader = new TileBitmapLoader(mapNetworkAdapter);

        final double TILE_SIZE = 256;

        ViewUtils.TouchDelta touchDelta = new ViewUtils.TouchDelta();

        TilesView tilesView = findViewById(R.id.tiles_view);
        tilesView.setOnTouchListener(touchDelta);
        tilesView.setTileBitmapLoader(tileBitmapLoader);

        BehaviorSubject<Integer> zoom = BehaviorSubject.createDefault(10);

        RxView.clicks(findViewById(R.id.zoom_in_button))
                .subscribe(ignore -> zoom.onNext(zoom.getValue() + 1));

        RxView.clicks(findViewById(R.id.zoom_out_button))
                .subscribe(ignore -> zoom.onNext(zoom.getValue() - 1));

        Observable<PointD> tilesViewSize = tilesView.getViewSize();

        // OSTANKINO MEAT PROCESSING PLANT JSC
        BehaviorSubject<LatLng> mapCenter = BehaviorSubject.createDefault(new LatLng(55.816164, 37.598820));

        CoordinateProjection coordinateProjection = new CoordinateProjection((int) TILE_SIZE);

        Observable<PointD> offset =
                Observable.combineLatest(
                        tilesViewSize, mapCenter, zoom,
                        (tilesViewSizeValue, mapCenterValue, zoomValue) ->
                                MapTileUtils.calculateOffset(coordinateProjection,
                                        zoomValue, tilesViewSizeValue, mapCenterValue));

        Observable<Triple<PointD, PointD, Integer>> mapState =
                Observable.combineLatest(
                        tilesViewSize, offset, zoom, Triple::new);
        touchDelta.getObservable()
                .withLatestFrom(mapState,
                        (pixelDelta, mapStateValue) -> {
                            Log.v(TAG, "pixelDelta(" + pixelDelta + ")");
                            final double cx = mapStateValue.first.x / 2.0 - mapStateValue.second.x;
                            final double cy = mapStateValue.first.y / 2.0 - mapStateValue.second.y;
                            final PointD newPoint = new PointD(cx - pixelDelta.x, cy - pixelDelta.y);
                            return coordinateProjection.fromPointToLatLng(newPoint, mapStateValue.third);
                        })
                .subscribe(mapCenter::onNext);


        Observable.combineLatest(
                tilesViewSize, offset, zoom,
                (tilesViewSizeValue, offsetValue, zoomValue) ->
                        MapTileUtils.calculateMapTiles(
                                TILE_SIZE, zoomValue, tilesViewSizeValue, offsetValue
                        ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tilesView::setTiles);
    }
}
