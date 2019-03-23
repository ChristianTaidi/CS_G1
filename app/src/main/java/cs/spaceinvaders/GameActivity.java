package cs.spaceinvaders;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import cs.spaceinvaders.InvadersGameView;


public class GameActivity extends AppCompatActivity {

    InvadersGameView invGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String name=bundle.getString("name");
        String profilePicEncoded = bundle.getString("profilePic");
        Boolean violent = bundle.getBoolean("violent");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y establecerlo como la visualización
        invGameView = new InvadersGameView(this, size.x, size.y, violent,name, profilePicEncoded);
        setContentView(invGameView);

    }

    @Override
    protected void onResume(){
        super.onResume();
        invGameView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        invGameView.pause();
    }
}
