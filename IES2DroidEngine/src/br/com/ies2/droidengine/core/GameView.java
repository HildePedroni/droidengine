package br.com.ies2.droidengine.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public abstract class GameView extends SurfaceView implements SurfaceHolder.Callback {

	public static final String TAG = "Engine";
	private GameLoop loop;
	private String avgFPS;
	private LayerManager layerManager;
	private int screenW;
	private int screenH;

	private void calculaTamanho() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenH = size.y;
		screenW = size.x;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		calculaTamanho();
		initiateElements();
		// Game s� inicia depois da tela carregada
		loop.free();
		super.onLayout(changed, left, top, right, bottom);
	}

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

	// Garante que o update da layer ser� chamado
	public final void layerUpdate(long gameTime) {
		layerManager.layerUpdate(gameTime);
	}

	/**
	 * Todos os elementos que ser�o desenhados na tela, sprites e layers devem
	 * ser iniciados nesse metodo.
	 */
	public abstract void initiateElements();

	public abstract void update();

	// Metodos de sufaceHolder

	public LayerManager getLayerManager() {
		return this.layerManager;
	}

	@Override
	public final void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "SurfaceCreated");
		// Assim que a surface estiver pronta
		// para desenho � enviado para o loop o
		// surfaceHolder.
		loop.setSurfaceHolder(holder);
	}

	@Override
	public final void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i(TAG, "SurfaceChanged");
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

	public int getScreenW() {
		return screenW;
	}

	public int getScreenH() {
		return screenH;
	}

}
