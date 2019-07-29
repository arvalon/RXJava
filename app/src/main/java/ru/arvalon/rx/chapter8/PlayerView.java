package ru.arvalon.rx.chapter8;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter8.pojo.GameSymbol;

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
            case CIRCLE:
                setImageResource(R.drawable.symbol_circle);
                break;
            case CROSS:
                setImageResource(R.drawable.symbol_cross);
                break;
            default:
                setImageResource(0);
        }
    }
}
