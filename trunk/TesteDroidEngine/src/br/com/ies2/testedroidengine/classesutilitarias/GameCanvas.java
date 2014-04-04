package br.com.ies2.testedroidengine.classesutilitarias;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import br.com.ies2.droidengine.core.GameView;
import br.com.ies2.droidengine.utils.Sprite;
import br.com.ies2.testedroidengine.R;

public class GameCanvas extends GameView {

    private Sprite bad1;
    private Sprite bad2;
    private Sprite staticSprite;

    public GameCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        // rectangle = new Rectangle("retangulo", 10, 100, 100, 100);
        // getLayerManager().add(rectangle);
        Bitmap image = loadImage(R.drawable.bad1, false);
        bad1 = new Sprite("bad1", image, 4, 3, 6);
        bad1.setPosition(20, 20);
        getLayerManager().add(bad1);

        Bitmap imageBad2 = loadImage(R.drawable.bad2, false);
        bad2 = new Sprite("bad2", imageBad2, 4, 3, 6);
        bad2.setPosition(50, 140);
        getLayerManager().add(bad2);

        staticSprite = new Sprite("statico", imageBad2);
        staticSprite.setPosition(160, 190);
        getLayerManager().add(staticSprite);

        bad1.setState("FRONT", new int[] { 0, 1, 2 });
        bad1.changeState("FRONT");

        bad2.setState("BACK", new int[] { 9, 10, 11 });
        bad2.changeState("BACK");

    }

    @Override
    public void update() {
        bad2.move(0.2f, 0);
        if (bad2.collidesWith(staticSprite, true)) {
            Log.i(GameView.TAG, "Colidiu!!");
        }
    }

}
