package aiou.muslim.mttech.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

import aiou.muslim.mttech.Adapters.AzkarCategoryAdapter;
import aiou.muslim.mttech.Manager.DBManagerAzkar;
import aiou.muslim.mttech.R;

public class AzkarCategoriesActivity extends AppCompatActivity {

    private AdView mAdView;
    DBManagerAzkar dbManagerAzkar;
    AzkarCategoryAdapter azkarAdapter;
    RecyclerView azkarRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar_categories);
        getSupportActionBar().hide();

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dbManagerAzkar = new DBManagerAzkar(AzkarCategoriesActivity.this);
        dbManagerAzkar.open();
        try {
            dbManagerAzkar.copyDataBase();
        }catch (IOException e){
            e.printStackTrace();
        }

        azkarRV = findViewById(R.id.azkarRV);
        azkarRV.setLayoutManager(new LinearLayoutManager(this));
        azkarAdapter = new AzkarCategoryAdapter(AzkarCategoriesActivity.this, dbManagerAzkar.getCategories("ar"), dbManagerAzkar.getCategories("en"));
        azkarRV.setAdapter(azkarAdapter);

    }

}