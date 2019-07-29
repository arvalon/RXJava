package ru.arvalon.rx.chapter9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter9.data.GameModel;
import ru.arvalon.rx.chapter9.load.LoadGameActivity;
import ru.arvalon.rx.chapter9.pojo.GameStatus;

public class Chapter9Activity extends AppCompatActivity {

    private CompositeDisposable viewSubscriptions = new CompositeDisposable();
    private GameModel gameModel;
    private GameViewModel gameViewModel;

    private InteractiveGameGridView gameGridView;
    private PlayerView playerInTurnImageView;
    private View winnerView;
    private TextView winnerTextView;
    private Button newGameButton;
    private Button saveGameButton;
    private Button loadGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter9);

        // Get the shared GameModel
        gameModel = ((GameApplication) getApplication()).getGameModel();

        resolveViews();
        createViewModel();
        makeViewBinding();
    }

    private void resolveViews() {
        gameGridView = findViewById(R.id.grid_view);
        playerInTurnImageView = findViewById(R.id.player_in_turn_image_view);
        winnerView = findViewById(R.id.winner_view);
        winnerTextView = findViewById(R.id.winner_text_view);
        newGameButton = findViewById(R.id.new_game_button);
        saveGameButton = findViewById(R.id.save_game_button);
        loadGameButton = findViewById(R.id.load_game_button);
    }

    private void createViewModel() {
        gameViewModel = new GameViewModel(
                gameModel,
                gameGridView.getTouchesOnGrid()
        );
        gameViewModel.subscribe();
    }

    private void makeViewBinding() {
        // Handle new game, saving and loading games
        viewSubscriptions.add(RxView.clicks(newGameButton)
                .subscribe(ignore -> gameModel.newGame())
        );
        viewSubscriptions.add(RxView.clicks(saveGameButton)
                .subscribe(ignore -> gameModel.saveActiveGame())
        );
        viewSubscriptions.add(RxView.clicks(loadGameButton)
                .subscribe(ignore -> showLoadGameActivity())
        );

        // Bind the View Model
        viewSubscriptions.add(
            gameViewModel.getFullGameState()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(gameGridView::setData)
        );

        viewSubscriptions.add(
            gameViewModel.getPlayerInTurn()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(playerInTurnImageView::setData)
        );

        viewSubscriptions.add(
            gameViewModel.getGameStatus()
                    .map(GameStatus::isEnded)
                    .map(isEnded -> isEnded ? View.VISIBLE : View.GONE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(winnerView::setVisibility)
        );

        viewSubscriptions.add(
            gameViewModel.getGameStatus()
                    .map(gameStatus ->
                            gameStatus.isEnded() ?
                                    "Winner: " + gameStatus.getWinner() : "")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(winnerTextView::setText)
        );
    }

    private void releaseViewBinding() {
        viewSubscriptions.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseViewBinding();
        gameViewModel.unsubscribe();
    }

    private void showLoadGameActivity() {
        Intent intent = new Intent(this, LoadGameActivity.class);
        startActivity(intent);
    }
}
