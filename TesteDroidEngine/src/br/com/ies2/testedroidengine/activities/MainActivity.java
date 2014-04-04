package br.com.ies2.testedroidengine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import br.com.ies2.testedroidengine.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_testes);
    }

    public void spriteTeste(View v) {
        Log.i("TESTE", "Rodei");
        Intent intent = new Intent(getApplicationContext(), SpriteActivity.class);
        startActivity(intent);
    }

    public void svgTeste(View v) {
        Log.i("TESTE", "Rodei");
        Intent intent = new Intent(this, SvgActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        this.finish();
    }

}
