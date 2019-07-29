package ru.arvalon.rx.chapter9.load;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter9.GameApplication;
import ru.arvalon.rx.chapter9.data.GameModel;
import ru.arvalon.rx.chapter9.pojo.SavedGame;

public class LoadGameActivity extends AppCompatActivity {
    private GameModel gameModel;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        setTitle("Load Previous Game");

        // Get the shared GameModel
        gameModel = ((GameApplication) getApplication()).getGameModel();

        SavedGamesListAdapter listAdapter =
                new SavedGamesListAdapter(this, android.R.layout.simple_list_item_1);

        ListView listView = (ListView) findViewById(R.id.saved_games_list);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            SavedGame savedGame = (SavedGame) view.getTag();
            gameModel.putActiveGameState(savedGame.getGameState());
            finish();
        });

        subscriptions.add(
                gameModel.getSavedGamesStream()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(savedGames -> {
                            listAdapter.clear();
                            listAdapter.addAll(savedGames);
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }
}
