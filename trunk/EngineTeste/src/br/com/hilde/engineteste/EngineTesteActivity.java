package br.com.hilde.engineteste;

import br.com.hilde.endineteste.core.GameView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class EngineTesteActivity extends Activity {

    private MyGameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_teste);
        gameView = (MyGameView) findViewById(R.id.mygameview);
        gameView.startGame();
    }

    @Override
    public void onBackPressed() {
        Log.i(GameView.TAG, "BackPressed");
        gameView.stopGame();
        this.finish();
    }
}
