package br.com.hilde.engineteste;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import br.com.hilde.endineteste.core.GameView;

public class MyGameView extends GameView {

    private Paint squarePaint;
    private float x = 10;
    private float y = 10;

    public MyGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        squarePaint = new Paint();
        squarePaint.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, 100 + x, 100 + y, squarePaint);
    }

    @Override
    public void update() {
        this.x += 1f;
        this.y += 1f;
        Log.i(GameView.TAG, "Valor : X : " + x);
        Log.i(GameView.TAG, "Valor : Y : " + y);
    }

}
