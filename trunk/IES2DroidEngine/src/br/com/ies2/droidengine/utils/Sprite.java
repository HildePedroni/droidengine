package br.com.ies2.droidengine.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import br.com.ies2.droidengine.core.GameView;
import br.com.ies2.droidengine.core.Layer;

public class Sprite extends Layer {

    public static final String DEFAULT_STATE = "DEFAULT";
    private Bitmap image;
    private int frameWidth;
    private int frameHeight;
    private int currentFrame = 0;
    private List<Point> framePositions;
    private Map<String, int[]> states;

    private String state;
    private boolean stateChanged = false;
    private int[] atualState;
    private int stateFrame = 0;

    private long frameTicker;
    private int framePeriod;

    private Rect collisionRectangle;

    public Sprite(String name, Bitmap image, int rows, int columns, int fps) {
        super(name);
        this.image = image;
        frameWidth = image.getWidth() / columns;
        frameHeight = image.getHeight() / rows;
        // Pontos que irão representar a posição do frame
        framePositions = new ArrayList<Point>();
        int frameArray[][] = new int[rows][columns];
        int x = 0;
        int y = 0;
        // Monta o quadro de posições;
        for (int i = 0; i < frameArray.length; i++) {
            for (int j = 0; j < frameArray[i].length; j++) {
                Point p = new Point(x, y);
                framePositions.add(p);
                x += frameWidth;
            }
            x = 0;
            y += frameHeight;
        }
        states = new HashMap<String, int[]>();
        state = DEFAULT_STATE;
        framePeriod = 1000 / fps;
        frameTicker = 0l;

    }

    @Override
    public void draw(Canvas canvas) {
        Point p = framePositions.get(currentFrame);
        int srcX = p.x;
        int srcY = p.y;
        Rect src = new Rect(srcX, srcY, srcX + frameWidth, srcY + frameHeight);
        Rect dst = new Rect((int) getX(), (int) getY(), (int) getX() + frameWidth, (int) getY() + frameHeight);
        collisionRectangle = dst;
        canvas.drawBitmap(image, src, dst, null);
    }

    /**
     * Define a state that can be used.
     * 
     * @param stateName
     * @param frameSequence
     */
    public void setState(String stateName, int[] frameSequence) {
        states.put(stateName, frameSequence);
    }

    /**
     * Changes the atual state
     * 
     * @param stateName
     */
    public void changeState(String stateName) {
        this.state = stateName;
        stateChanged = true;
    }

    public void setBounds() {

    }

    @Override
    public void layerUpdate(long gameTime) {
        if (gameTime > frameTicker + framePeriod) {
            frameTicker = gameTime;
            if (state.equals("DEFAULT")) {
                // Se o status for default corre pelos indices sequencialmente
                currentFrame++;
                if (currentFrame > framePositions.size() - 1) {
                    currentFrame = 0;
                }
            } else {
                // Variavel de controle.
                boolean allOk = true;
                /**
                 * O estado sempre inicia como default. Caso o programador
                 * queira um estado diferente, ele deve alterar isso com o
                 * changeState. Feito isso a variável stateChanged tem seu valor
                 * alterado para que o estado seja trocado;
                 */
                if (stateChanged) {
                    // Trava a condição novamente
                    stateChanged = false;
                    // Apaga a referencia ao estado anterior
                    atualState = null;
                    // Busca um novo estado.
                    atualState = states.get(state);
                    // Caso o estado não exista, ou por não ter sido definido ou
                    // por
                    // ter sido chamado
                    // de forma errada, o estado é definido como o default.
                    if (atualState == null) {
                        allOk = false;
                        changeState(DEFAULT_STATE);
                    }
                }

                // allOk Evita a entrada nesse trecho para evitar
                // inconsistencias.
                if (allOk) {
                    // Pega o frame definido no array
                    currentFrame = atualState[stateFrame];
                    stateFrame++; // Incrementa para o proximo
                    if (stateFrame > atualState.length - 1) {
                        // Zera o ciclo
                        stateFrame = 0;
                    }
                }

            }
        }
    }

    public boolean collidesWith(Sprite other, boolean pixelPerfect) {
        boolean result = false;
        if (isVisible()) {
            if (collisionRectangle != null) {
                if (collisionRectangle.intersect(other.getCollisionRectangle())) {
                    result = true;
                    if (pixelPerfect) {
                        return pixelCollisionTest(other);
                    }
                }
            }
        }
        return result;
    }

    private boolean pixelCollisionTest(Sprite other) {

        Rect intersection = new Rect();
        Bitmap otherImage = other.getBitmap();
        intersection.setIntersect(this.collisionRectangle, other.getCollisionRectangle());
        for (int i = intersection.left+1; i < intersection.right; i++) {
            for (int j = intersection.top+1; j < intersection.bottom; j++) {

                int pixelA = image.getPixel(i, j);
                int pixelB = otherImage.getPixel(i, j);

                int alphavalueA = Color.alpha(pixelA);
                int alphavalueB = Color.alpha(pixelB);

                if (alphavalueA > 0 && alphavalueB > 0) {
                    Log.i(GameView.TAG, "Pixel A " + alphavalueA);
                    Log.i(GameView.TAG, "Pixel B " + alphavalueB);
                    return true;
                }
            }
        }
        return false;
    }

    public Rect getCollisionRectangle() {
        return collisionRectangle;
    }

    public Bitmap getBitmap() {
        return this.image;
    }

}
