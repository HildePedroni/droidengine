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
    private boolean staticSprite = false;

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

    public Sprite(String name, Bitmap image) {
        super(name);
        this.image = image;
        staticSprite = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (staticSprite) {
            canvas.drawBitmap(image, getX(), getY(), null);
            collisionRectangle = new Rect((int) getX(), (int) getY(), image.getWidth(), image.getHeight());
        } else {
            Point p = framePositions.get(currentFrame);
            int srcX = p.x;
            int srcY = p.y;
            Rect src = new Rect(srcX, srcY, srcX + frameWidth, srcY + frameHeight);
            Rect dst = new Rect((int) getX(), (int) getY(), (int) getX() + frameWidth, (int) getY() + frameHeight);
            collisionRectangle = dst;
            canvas.drawBitmap(image, src, dst, null);
        }
    }

    /**
     * Define a state that can be used.
     * 
     * @param stateName
     * @param frameSequence
     */
    public void setState(String stateName, int[] frameSequence) {
        if (!staticSprite) {
            states.put(stateName, frameSequence);
        } else {
            try {
                throw new Exception("States is invalid for static sprites!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Changes the atual state
     * 
     * @param stateName
     */
    public void changeState(String stateName) {
        if (!staticSprite) {
            this.state = stateName;
            stateChanged = true;
        } else {
            try {
                throw new Exception("Static sprites doen't handle state changes!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBounds() {

    }

    @Override
    public void layerUpdate(long gameTime) {
        if (!staticSprite) {
            if (gameTime > frameTicker + framePeriod) {
                frameTicker = gameTime;
                if (state.equals("DEFAULT")) {
                    // Se o status for default corre pelos indices
                    // sequencialmente
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
                     * changeState. Feito isso a variável stateChanged tem seu
                     * valor alterado para que o estado seja trocado;
                     */
                    if (stateChanged) {
                        // Trava a condição novamente
                        stateChanged = false;
                        // Apaga a referencia ao estado anterior
                        atualState = null;
                        // Busca um novo estado.
                        atualState = states.get(state);
                        // Caso o estado não exista, ou por não ter sido
                        // definido ou
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
        
        Bitmap b1 = getBitmap();
        Bitmap b2 = other.getBitmap();
        
        Rect rectIntersect = new Rect();
        
        rectIntersect.setIntersect(this.collisionRectangle, other.getCollisionRectangle());
        
        int left    =   rectIntersect.left;
        int top     =    rectIntersect.top;
        int right   =  rectIntersect.right;
        int bottom  = rectIntersect.bottom;
        
        int aX, aY, bX, bY;
        
        for (int i = left; i < right; i++) {
            for (int j = top; j < bottom; j++) {
                
                //obtém o x e y da da intersecção na imagem A
                aX = (int) (i - this.x);
                aY = (int) (j - this.y);
                //obtém o x e y da da intersecção na imagem B
                bX = (int) (i - other.getX());
                bY = (int) (j - other.getY());
                
                //fora da IMAGEM RECT
                if(aX < 0 || aY < 0 || bX < 0 || bY < 0){
                    return false;
                }
                
                int corA = b1.getPixel(aX , aY);
                int corB = b2.getPixel(bX, bY);
                
                if(corA != 0 && corB != 0){
                    return true;
                }
            }
        }
        return false;
    }
    
    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = (int) Math.max(rect1.left, rect2.left);
        int top = (int) Math.max(rect1.top, rect2.top);
        int right = (int) Math.min(rect1.right, rect2.right);
        int bottom = (int) Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }


    public Rect getCollisionRectangle() {
        return collisionRectangle;
    }

    public Bitmap getBitmap() {
        return this.image;
    }

}
