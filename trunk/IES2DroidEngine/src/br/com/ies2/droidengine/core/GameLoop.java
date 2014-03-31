package br.com.ies2.droidengine.core;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop implements Runnable {

    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private Thread gameThread;
    private boolean isRunning = true;

    // FPS desejado
    private final static int MAX_FPS = 100;
    // numero máximo de frames a ser ignorado
    private final static int MAX_FRAME_SKIPS = 5;
    // periodo de cada frame
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    // Para medir FPS ===========================================
    private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
    // we'll be reading the stats every second
    private final static int STAT_INTERVAL = 1000; // ms
    // the average will be calculated by storing
    // the last n FPSs
    private final static int FPS_HISTORY_NR = 10;
    // last time the status was stored
    private long lastStatusStore = 0;
    // the status time counter
    private long statusIntervalTimer = 0l;
    // number of frames skipped since the game started

    // number of frames skipped in a store cycle (1 sec)

    // number of rendered frames in an interval
    private int frameCountPerStatCycle = 0;

    // the last FPS values
    private double fpsStore[];
    // the number of times the stat has been read
    private long statsCount = 0;
    // the average FPS since the game started
    private double averageFps = 0.0;

    public GameLoop(GameView gameView) {
        this.gameView = gameView;
        Log.i(GameView.TAG, "Loop Criado");
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        Log.i(GameView.TAG, "SurfaceHolder Pronto");
        this.surfaceHolder = holder;
    }

    public void run() {
        Canvas canvas;
        Log.d(GameView.TAG, "Iniciando o loop");
        // initialise timing elements for stat gathering
        initTimingElements();

        long beginTime;
        long timeDiff;
        int sleepTime;
        int framesSkipped;
        sleepTime = 0;

        while (isRunning) {
            canvas = null;
            // Verifica se a tela está pronta para se desenhar nela.
            if (this.surfaceHolder != null) {
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {

                        beginTime = System.currentTimeMillis();
                        framesSkipped = 0;
                        this.gameView.update();
                        this.gameView.layerUpdate(System.currentTimeMillis());
                        this.gameView.clearScreen(canvas);
                        // this.gameView.drawLayerManager(canvas);
                        this.gameView.draw(canvas);
                        // this.gameCanvas.paintFPS(canvas);
                        timeDiff = System.currentTimeMillis() - beginTime;
                        sleepTime = (int) (FRAME_PERIOD - timeDiff);
                        if (sleepTime > 0) {
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                            }
                        }
                        while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                            // Calculos gerais
                            this.gameView.update();
                            this.gameView.layerUpdate(System.currentTimeMillis());
                            sleepTime += FRAME_PERIOD;
                            framesSkipped++;
                        }

                        // calling the routine to store the gathered statistics
                        storeStats();

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

    /*
     * Para calculos de FPs
     */
    private void storeStats() {

        frameCountPerStatCycle++;

        // check the actual time
        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
            // calculate the actual frames pers status check interval
            double actualFps = (double) (frameCountPerStatCycle / (STAT_INTERVAL / 1000));
            // stores the latest fps in the array
            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;
            // increase the number of times statistics was calculated
            statsCount++;
            double totalFps = 0.0;

            // sum up the stored fps values
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            // obtain the average

            if (statsCount < FPS_HISTORY_NR) {
                // in case of the first 10 triggers
                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }

            // saving the number of total frames skipped

            // resetting the counters after a status record (1 sec)
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;
            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
            Log.d(GameView.TAG, "Average FPS:" + df.format(averageFps));
            gameView.setAvgFPS("FPS: " + df.format(averageFps));

        }

    }

    private void initTimingElements() {
        // initialise timing elements
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
        Log.d(GameView.TAG + ".initTimingElements()", "Timing elements for stats initialised");
    }

}
