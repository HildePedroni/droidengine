package br.com.ies2.testedroidengine;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svg_layout);
    }

    @Override
    public void onBackPressed() {
        // canvas.stopGame();
        this.finish();
    }

}
