package cs.spaceinvaders;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cs.spaceinvaders.ImageEncoder;


public class RankingActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Ranking2", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_ranking);
        ImageEncoder encoder;

        ImageButton replay;

        replay = findViewById(R.id.Replay);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView t1;
        TextView t2;
        TextView t3;
        TextView t4;
        TextView t5;
        TextView t6;
        TextView t7;
        TextView t8;
        TextView t9;
        TextView t10;

        t1 = findViewById(R.id.textTop1);
        t1.setText(t1.getText()+sharedPreferences.getString("Rank 1","-1"));
        t2 = findViewById(R.id.textTop2);
        t2.setText(t2.getText()+sharedPreferences.getString("Rank 2","-1"));
        t3 = findViewById(R.id.textTop3);
        t3.setText(t3.getText()+sharedPreferences.getString("Rank 3","-1"));
        t4 = findViewById(R.id.textTop4);
        t4.setText(t4.getText()+sharedPreferences.getString("Rank 4","-1"));
        t5 = findViewById(R.id.textTop5);
        t5.setText(t5.getText()+sharedPreferences.getString("Rank 5","-1"));
        t6 = findViewById(R.id.textTop6);
        t6.setText(t6.getText()+sharedPreferences.getString("Rank 6","-1"));
        t7 = findViewById(R.id.textTop7);
        t7.setText(t7.getText()+sharedPreferences.getString("Rank 7","-1"));
        t8 = findViewById(R.id.textTop8);
        t8.setText(t8.getText()+sharedPreferences.getString("Rank 8","-1"));
        t9 = findViewById(R.id.textTop9);
        t9.setText(t9.getText()+sharedPreferences.getString("Rank 9","-1"));
        t10 = findViewById(R.id.textTop10);
        t10.setText(t10.getText()+sharedPreferences.getString("Rank 10","-1"));

        ImageView i1;
        ImageView i2;
        ImageView i3;
        ImageView i4;
        ImageView i5;
        ImageView i6;
        ImageView i7;
        ImageView i8;
        ImageView i9;
        ImageView i10;

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 1","-1")));
        i1 = findViewById(R.id.Top1);
        i1.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),110,147,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 2","-1")));
        i2 = findViewById(R.id.Top2);
        i2.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 3","-1")));
        i3 = findViewById(R.id.Top3);
        i3.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 4","-1")));
        i4 = findViewById(R.id.Top4);
        i4.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 5","-1")));
        i5 = findViewById(R.id.Top5);
        i5.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 6","-1")));
        i6 = findViewById(R.id.Top6);
        i6.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 7","-1")));
        i7 = findViewById(R.id.Top7);
        i7.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 8","-1")));
        i8 = findViewById(R.id.Top8);
        i8.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 9","-1")));
        i9 = findViewById(R.id.Top9);
        i9.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));

        encoder = new ImageEncoder((sharedPreferences.getString("Photo 10","-1")));
        i10 = findViewById(R.id.Top10);
        i10.setImageBitmap(Bitmap.createScaledBitmap(encoder.getDecodedImage(),60,80,false));
    }

}
