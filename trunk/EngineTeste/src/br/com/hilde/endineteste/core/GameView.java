package br.com.hilde.endineteste.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = "Engine";
    private GameLoop loop;
    private String avgFPS;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        loop = new GameLoop(this);
    }

    public final void startGame() {
        loop.startGame();
    }

    public final void stopGame() {
        loop.stopGame();
    }

    public void clearScreen(Canvas canvas) {
        postInvalidate();
        canvas.drawColor(Color.WHITE);
    }

    // Metodos abstratos

    public abstract void draw(Canvas canvas);

    public abstract void update();

    // Metodos de sufaceHolder

    @Override
    public final void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "SurfaceCreated");
        // Assim que a surface estiver pronta
        // para desenho é enviado para o loop o
        // surfaceHolder.
        loop.setSurfaceHolder(holder);
    }

    @Override
    public final void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "SurfaceChanged");
        // TODO Auto-generated method stub
        loop.setSurfaceHolder(holder);
    }

    @Override
    public final void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "SurfaceDestroied");
        // TODO Auto-generated method stub

    }

    protected void setAvgFPS(String avgFPS) {
        this.avgFPS = avgFPS;
    }

}
