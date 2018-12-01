package ps.spaceinvaders;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity {

    private TextView t1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        t1 = findViewById(R.id.textTop1);
        t1.setText("patata");
    }
}
