package br.com.ies2.testedroidengine.activities;

import android.app.Activity;
import android.os.Bundle;
import br.com.ies2.testedroidengine.R;
import br.com.ies2.testedroidengine.classesutilitarias.GameCanvas;

public class SpriteActivity extends Activity {

    private GameCanvas gameCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        gameCanvas = (GameCanvas) findViewById(R.id.gameCanvas);
        gameCanvas.startGame();
    }

    @Override
    public void onBackPressed() {
        gameCanvas.stopGame();
        this.finish();
    }

}
