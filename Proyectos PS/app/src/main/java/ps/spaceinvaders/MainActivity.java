package ps.spaceinvaders;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText playerAge;
    private Button enterButton;
    private String Name;

    class ValidateThread extends Thread{
        int n;

        ValidateThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            if(n<13){
                Intent intent = new Intent (MainActivity.this, PeacefulActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(MainActivity.this, ViolentActivity.class);
                intent.putExtra("name",Name);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        playerAge=(EditText)findViewById(R.id.ageText);
        enterButton=(Button)findViewById(R.id.enterBtn);
        Name="pablomotos";

        enterButton.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view){
                int pAge = Integer.parseInt(playerAge.getText().toString());
                validate(pAge);
            }
        });
    }

    private void validate(int numberAge){
        ValidateThread validateTh = new ValidateThread(numberAge);
        validateTh.start();
    }
}
