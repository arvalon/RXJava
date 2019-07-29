package ru.arvalon.rx.chapter14.network;

import android.graphics.Bitmap;
import android.util.Log;

import io.reactivex.Observable;

import static ru.arvalon.rx.MainActivity.TAG;

public class MapNetworkAdapterSimple implements MapNetworkAdapter {

    final private NetworkClient networkClient;
    final private String urlFormat;

    public MapNetworkAdapterSimple(
            final NetworkClient networkClient,
            final String urlFormat) {
        this.networkClient = networkClient;
        this.urlFormat = urlFormat;
    }

    public Observable<Bitmap> getMapTile(final int zoom, final int x, final int y) {
        Log.d(TAG, "getMapTile(" + zoom + ", " + x + ", " + y + ")");
        final String url = String.format(urlFormat, zoom, x, y);
        return networkClient
                .loadBitmap(url);
    }

    @Override
    public int getTileSizePx() {
        return 256;
    }
}
