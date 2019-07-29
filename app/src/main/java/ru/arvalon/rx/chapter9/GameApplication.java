package ru.arvalon.rx.chapter9;

import android.app.Application;

import ru.arvalon.rx.chapter9.data.GameModel;

public class GameApplication extends Application {

    private GameModel gameModel;

    @Override
    public void onCreate() {
        super.onCreate();
        gameModel = new GameModel(this);
    }

    public GameModel getGameModel() {
        return gameModel;
    }
}
