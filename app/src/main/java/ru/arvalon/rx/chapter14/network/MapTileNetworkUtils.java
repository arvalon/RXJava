package ru.arvalon.rx.chapter14.network;

import android.util.Log;

import ru.arvalon.rx.chapter14.Tile;
import ru.arvalon.rx.chapter14.TileBitmap;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MapTileNetworkUtils {
    private static final String TAG = MapTileNetworkUtils.class.getSimpleName();

    static public Function<Tile, Observable<TileBitmap>> loadMapTile(final MapNetworkAdapter mapNetworkAdapter) {

        return mapTile -> mapNetworkAdapter.getMapTile(
                mapTile.getZoom(), mapTile.getX(), mapTile.getY())
                .map(bitmap -> new TileBitmap(mapTile, bitmap))
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "Error loading tile (" + mapTile + ")", throwable);
                    throwable.printStackTrace();
                    return Observable.just(new TileBitmap(mapTile, null));
                });
    }
}
