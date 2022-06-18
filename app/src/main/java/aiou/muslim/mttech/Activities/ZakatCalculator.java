package aiou.muslim.mttech.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import aiou.muslim.mttech.R;

public class ZakatCalculator extends AppCompatActivity implements View.OnClickListener {

    private AdView mAdView;
    TextView ToUnderline;
    EditText GoldPrice,TotalPrice;
    TextView Result1,Result2;
    float InputGold, InputTotal;
    ImageView backBtn;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakat_calculator);
        getSupportActionBar().hide();

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ToUnderline= findViewById(R.id.underlinelink);
        ToUnderline.setPaintFlags(ToUnderline.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }

    public void LocalPriceGold(View v) {
        Intent intent = new Intent(this, GoldPriceActivity.class);
        startActivity(intent);
    }

    public void ZakatInfo(View v){
        k=1;
        setContentView(R.layout.zakatinfo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public  void CalculateZakat(View v){
        GoldPrice=findViewById(R.id.inputgoldprice);
        TotalPrice=findViewById(R.id.inputtotalprice);
        Result1=findViewById(R.id.zakatresult1);
        Result2=findViewById(R.id.zakatresult2);
        if (GoldPrice.getText().toString().equals("") || TotalPrice.getText().toString().equals("")) return;
        InputGold=Float.parseFloat(GoldPrice.getText().toString());
        InputTotal=Float.parseFloat(TotalPrice.getText().toString());

        double nissab = InputGold*85.05;
        String result = getString(R.string.nissab) +" "+ nissab;
        String zakat ;
        if (nissab>InputTotal){
            zakat =getString(R.string.nozakat);
            Result1.setText(zakat);
            Result2.setText(result);
        }else{
            float zakatprice = (float) (InputTotal*0.025);
            zakat= getString(R.string.zakatprice) +" "+ zakatprice ;
            Result1.setText(zakat);
            Result2.setText(result);
        }
    }

    @Override
    public void onBackPressed() {
        if (k == 1) {
            k = 0;
            setContentView(R.layout.activity_zakat_calculator);

        } else {
            super.onBackPressed();
        }
    }
}
