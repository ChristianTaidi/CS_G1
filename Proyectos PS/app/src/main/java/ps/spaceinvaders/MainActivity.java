package ps.spaceinvaders;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText playerAge;
    private Button enterButton;
    private EditText Name;
    private Bitmap profilePic;
    private String profilePicEncoded;

    private ImageEncoder encoder;

    private static final int CAMERA_PIC_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        playerAge=(EditText)findViewById(R.id.ageText);
        Name=(EditText)findViewById(R.id.nameText);

        findViewById(R.id.profilePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        enterButton=(Button)findViewById(R.id.enterBtn);

        enterButton.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view){
                if (playerAge.getText().toString().compareTo("")!=0){
                    int pAge = Integer.parseInt(playerAge.getText().toString());
                    validate(pAge);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            profilePic = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.profilePic); //sets imageview as the bitmap
            imageview.setImageBitmap(profilePic);
            encoder = new ImageEncoder(profilePic);
            profilePicEncoded = encoder.getEncodedImage();
        }
    }

    private void validate(int n){
        if (profilePicEncoded == null){
            encoder = new ImageEncoder(Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.avatarvacio)));
            profilePicEncoded = encoder.getEncodedImage();

        }

        if(n<13){
            Intent intent = new Intent (MainActivity.this, PeacefulActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(MainActivity.this, ViolentActivity.class);
            intent.putExtra("name",Name.getText().toString());
            intent.putExtra("profilePic", profilePicEncoded);
            startActivity(intent);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
}