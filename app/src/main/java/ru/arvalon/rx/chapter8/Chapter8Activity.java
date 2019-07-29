package ru.arvalon.rx.chapter8;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter8.pojo.GameStatus;

public class Chapter8Activity extends AppCompatActivity {

    private CompositeDisposable viewSubscriptions = new CompositeDisposable();
    private GameViewModel gameViewModel;

    private InteractiveGameGridView gameGridView;
    private PlayerView playerInTurnImageView;
    private View winnerView;
    private TextView winnerTextView;
    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter8);

        gameGridView = findViewById(R.id.grid_view);
        playerInTurnImageView = findViewById(R.id.player_in_turn_image_view);
        winnerView = findViewById(R.id.winner_view);
        winnerTextView = findViewById(R.id.winner_text_view);
        newGameButton = findViewById(R.id.new_game_button);

        gameViewModel = new GameViewModel(
                gameGridView.getTouchesOnGrid(),
                RxView.clicks(newGameButton)
        );
        gameViewModel.subscribe();
        makeViewBinding();
    }

    private void makeViewBinding() {
        viewSubscriptions.add(
            gameViewModel.getGameGrid()
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
}
