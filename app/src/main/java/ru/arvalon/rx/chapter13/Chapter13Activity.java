package ru.arvalon.rx.chapter13;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.arvalon.rx.R;

public class Chapter13Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter13);

        FanView fanView = findViewById(R.id.fan_view);
        View veilView = findViewById(R.id.veil);

        FanViewModel fanViewModel = new FanViewModel(
                RxView.clicks(fanView),
                Arrays.asList(
                        new FanItem("John Smith"),
                        new FanItem("Call"),
                        new FanItem("SMS"),
                        new FanItem("Send email")
                ));
        fanViewModel
                .getFanItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fanView::setFanItems);

        fanViewModel
                .getOpenRatio()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fanView::setOpenRatio);

        fanViewModel
                .getOpenRatio()
                .map(ratio -> (int) (64f * ratio))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(veilView.getBackground()::setAlpha);
    }
}
