package ru.arvalon.rx.chapter8;

import android.util.Pair;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import ru.arvalon.rx.chapter8.pojo.GameGrid;
import ru.arvalon.rx.chapter8.pojo.GameState;
import ru.arvalon.rx.chapter8.pojo.GameStatus;
import ru.arvalon.rx.chapter8.pojo.GameSymbol;
import ru.arvalon.rx.chapter8.pojo.GridPosition;

public class GameViewModel {

    private static final int GRID_WIDTH = 3;
    private static final int GRID_HEIGHT = 3;
    private static final GameGrid EMPTY_GRID = new GameGrid(GRID_WIDTH, GRID_HEIGHT);
    private static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final BehaviorSubject<GameState> gameStateSubject = BehaviorSubject.createDefault(EMPTY_GAME);
    private final Observable<GameSymbol> playerInTurnObservable;
    private final Observable<GameStatus> gameStatusObservable;

    private final Observable<GridPosition> touchEventObservable;
    private final Observable<Object> newGameEventObservable;

    public GameViewModel(Observable<GridPosition> touchEventObservable,
                         Observable<Object> newGameEventObservable) {

        this.touchEventObservable = touchEventObservable;
        this.newGameEventObservable = newGameEventObservable;
        playerInTurnObservable = gameStateSubject
                .map(GameState::getLastPlayedSymbol)
                .map(symbol -> {
                    if (symbol == GameSymbol.CIRCLE) {
                        return GameSymbol.CROSS;
                    } else {
                        return GameSymbol.CIRCLE;
                    }
                });
        gameStatusObservable = gameStateSubject
                .map(GameUtils::calculateGameStatus);
    }

    public Observable<GameGrid> getGameGrid() {

        return gameStateSubject.hide().map(GameState::getGameGrid);
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public void subscribe() {
        subscriptions.add(newGameEventObservable
                .map(ignore -> EMPTY_GAME)
                .subscribe(gameStateSubject::onNext)
        );

        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateSubject, playerInTurnObservable, Pair::new);

        Observable<GridPosition> gameNotEndedTouches =
                touchEventObservable
                        .withLatestFrom(gameStatusObservable, Pair::new)
                        .filter(pair -> !pair.second.isEnded())
                        .map(pair -> pair.first);

        Observable<GridPosition> filteredTouches =
                gameNotEndedTouches
                        .withLatestFrom(gameStateSubject, Pair::new)
                        .filter(pair -> {
                            GridPosition gridPosition = pair.first;
                            GameState gameState = pair.second;
                            return gameState.isEmpty(gridPosition);
                        })
                        .map(pair -> pair.first);

        subscriptions.add(filteredTouches
                .withLatestFrom(gameInfoObservable,
                        (gridPosition, gameInfo) ->
                                gameInfo.first.setSymbolAt(
                                        gridPosition, gameInfo.second))
                .subscribe(gameStateSubject::onNext));
    }

    public void unsubscribe() {
        subscriptions.clear();
    }

    public Observable<GameStatus> getGameStatus() {
        return gameStatusObservable;
    }
}
