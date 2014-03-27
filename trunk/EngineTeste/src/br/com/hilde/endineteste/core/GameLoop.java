package br.com.hilde.endineteste.core;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop implements Runnable {

    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private Thread gameThread;

    private long begin = 0;

    private boolean isRunning = true;

    public GameLoop(GameView gameView) {
        this.gameView = gameView;
        Log.i(GameView.TAG, "Loop Criado");
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        Log.i(GameView.TAG, "SurfaceHolder Pronto");
        this.surfaceHolder = holder;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        while (isRunning) {
            if (this.surfaceHolder != null) {
                try {
                    canvas = this.surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        long now = System.currentTimeMillis();

                        if (now > begin + 1000) {
                            begin = now;
                            gameView.update();
                            gameView.draw(canvas);
                            
                        }

                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

            }
        }
    }

    public void startGame() {
        this.isRunning = true;
        gameThread = new Thread(this);
        gameThread.setPriority(Thread.NORM_PRIORITY);
        gameThread.start();
    }

    public void stopGame() {
        this.isRunning = false;
        this.gameThread = null;
    }

}
