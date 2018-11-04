package ps.spaceinvaders;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class PeacefulActivity extends AppCompatActivity {

    InvadersGameView invGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peacefull);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String name=bundle.getString("name");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y establecerlo como la visualización
        invGameView = new InvadersGameView(this, size.x, size.y, false,name);

        setContentView(invGameView);

    }

    @Override
    protected void onResume(){
        super.onResume();
        invGameView.resume();
    }

    protected void onPause(){
        super.onPause();
        invGameView.pause();
    }
}
