package ru.arvalon.rx.chapter9;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter9.pojo.GameSymbol;

public class PlayerView extends AppCompatImageView {
    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(GameSymbol gameSymbol) {
        switch(gameSymbol) {
            case BLACK:
                setImageResource(R.drawable.symbol_black_circle);
                break;
            case RED:
                setImageResource(R.drawable.symbol_red_circle);
                break;
            default:
                setImageResource(0);
        }
    }
}
