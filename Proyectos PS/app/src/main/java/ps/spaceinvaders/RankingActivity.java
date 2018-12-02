package ps.spaceinvaders;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity {

    private TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    private ImageView i1,i2,i3,i4,i5,i6,i7,i8,i9,i10;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Ranking2", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_ranking);
        t1 = findViewById(R.id.textTop1);
        t1.setText(sharedPreferences.getString("Rank 1","-1"));
        t2 = findViewById(R.id.textTop2);
        t2.setText(sharedPreferences.getString("Rank 2","-1"));
        t3 = findViewById(R.id.textTop3);
        t3.setText(sharedPreferences.getString("Rank 3","-1"));
        t4 = findViewById(R.id.textTop4);
        t4.setText(sharedPreferences.getString("Rank 4","-1"));
        t5 = findViewById(R.id.textTop5);
        t5.setText(sharedPreferences.getString("Rank 5","-1"));
        t6 = findViewById(R.id.textTop6);
        t6.setText(sharedPreferences.getString("Rank 6","-1"));
        t7 = findViewById(R.id.textTop7);
        t7.setText(sharedPreferences.getString("Rank 7","-1"));
        t8 = findViewById(R.id.textTop8);
        t8.setText(sharedPreferences.getString("Rank 8","-1"));
        t9 = findViewById(R.id.textTop9);
        t9.setText(sharedPreferences.getString("Rank 9","-1"));
        t10 = findViewById(R.id.textTop10);
        t10.setText(sharedPreferences.getString("Rank 10","-1"));
        i1 = findViewById(R.id.Top1);
        i1.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 1","-1")));
        i2 = findViewById(R.id.Top2);
        i2.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 2","-1")));
        i3 = findViewById(R.id.Top3);
        i3.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 3","-1")));
        i4 = findViewById(R.id.Top4);
        i4.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 4","-1")));
        i5 = findViewById(R.id.Top5);
        i5.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 5","-1")));
        i6 = findViewById(R.id.Top6);
        i6.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 6","-1")));
        i7 = findViewById(R.id.Top7);
        i7.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 7","-1")));
        i8 = findViewById(R.id.Top8);
        i8.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 8","-1")));
        i9 = findViewById(R.id.Top9);
        i9.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 9","-1")));
        i10 = findViewById(R.id.Top10);
        i10.setImageBitmap(decodeBase64(sharedPreferences.getString("Photo 10","-1")));
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
