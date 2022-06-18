package aiou.muslim.mttech.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SharedData.SharedClass;

public class CounterActivity extends AppCompatActivity implements View.OnClickListener {

    int counter = 0;
    TextView counterTxt;
    ImageView backBtn, btnBackTwo;
    CardView btnCount, btnReset;
    LinearLayout btnSave;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        getSupportActionBar().hide();

        mp = MediaPlayer.create(CounterActivity.this, R.raw.tasbeehsound);

        counterTxt = findViewById(R.id.counterTxt);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        btnBackTwo = findViewById(R.id.btnBackTwo);
        btnBackTwo.setOnClickListener(this);
        btnCount = findViewById(R.id.btnCount);
        btnCount.setOnClickListener(this);
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        counter = SharedClass.getCounter(CounterActivity.this);
        counterTxt.setText(String.valueOf(SharedClass.getCounter(CounterActivity.this)));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.btnBackTwo:
                onBackPressed();
                break;
            case R.id.btnCount:
                counter = counter+1;
                counterTxt.setText(String.valueOf(counter));
                if (SharedClass.getTasbeenSound(CounterActivity.this)==1){
                    if (!mp.isPlaying()){
                        mp.start();
                    }
                }
                break;
            case R.id.btnReset:
                counter = 0;
                SharedClass.saveCounter(CounterActivity.this, counter);
                counterTxt.setText(String.valueOf(counter));
                break;
            case R.id.btnSave:
                SharedClass.saveCounter(CounterActivity.this, counter);
                Toast.makeText(this, "Counter Saved", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}