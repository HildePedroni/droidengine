package br.com.ies2.droidengine.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
    private LayerManager layerManager;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        layerManager = new LayerManager();
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

    public final void draw(Canvas canvas) {
        layerManager.draw(canvas);
    }

    // Garante que o update da layer será chamado
    public final void layerUpdate(long gameTime) {
        layerManager.layerUpdate(gameTime);
    }

    public abstract void update();

    // Metodos de sufaceHolder

    public LayerManager getLayerManager() {
        return this.layerManager;
    }

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

    public String getAvgFPS() {
        return this.avgFPS;
    }

    /**
     * Be careful when you set isMuttable to true, because if your image is too
     * big it can cause a outOfMemory exception.
     * 
     * @param resourceID
     * @param isMutable
     * @return
     */
    public Bitmap loadImage(int resourceID, boolean isMutable) {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), resourceID);
        if (isMutable) {
            return mBitmap.copy(Config.ARGB_8888, true);
        } else {
            return mBitmap;
        }
    }

}
