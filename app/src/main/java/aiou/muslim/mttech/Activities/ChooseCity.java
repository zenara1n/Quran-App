package aiou.muslim.mttech.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import aiou.muslim.mttech.Adapters.SpinnerCustomAdapter;
import aiou.muslim.mttech.Dashboard;
import aiou.muslim.mttech.Manager.DBManager;
import aiou.muslim.mttech.Models.CitiesModel;
import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SharedData.SharedClass;

public class ChooseCity extends AppCompatActivity {

    DBManager dbManager;
    private AdView mAdView;
    Spinner citySpinner;
    ImageView backBtn;
    CitiesAdapterOne citiesAdapter;
    List<CitiesModel> citiesList = new ArrayList<>();
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        getSupportActionBar().hide();

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int value = preferences.getInt("citystatus", 0);
        if (value==1) {
            startActivity(new Intent(ChooseCity.this, Dashboard.class));
        }

        citySpinner = findViewById(R.id.citySpinner);
        RecyclerView languagesRV = (RecyclerView) findViewById(R.id.langyagesRV);
        languagesRV.setLayoutManager(new LinearLayoutManager(this));

        dbManager = new DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SpinnerCustomAdapter spinnerCustomAdapter = new SpinnerCustomAdapter(ChooseCity.this, dbManager.getCountries());
        citySpinner.setAdapter(spinnerCustomAdapter);

        for (int i = 0; i < dbManager.getCities(citySpinner.getSelectedItem().toString()).size(); i++) {
            CitiesModel languageModel = new CitiesModel();
            languageModel.setId(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getId());
            languageModel.setCountry(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getCountry());
            languageModel.setCity(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getCity());
            languageModel.setLatitude(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getLatitude());
            languageModel.setLongitude(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getLongitude());
            citiesList.add(languageModel);
        }
        citiesAdapter = new CitiesAdapterOne(ChooseCity.this, citiesList);
        languagesRV.setAdapter(citiesAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int a, long l) {
                if (a > 0) {
                    citiesList.clear();
                    for (int i = 0; i < dbManager.getCities(citySpinner.getSelectedItem().toString()).size(); i++) {
                        CitiesModel languageModel = new CitiesModel();
                        languageModel.setId(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getId());
                        languageModel.setCountry(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getCountry());
                        languageModel.setCity(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getCity());
                        languageModel.setLatitude(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getLatitude());
                        languageModel.setLongitude(dbManager.getCities(citySpinner.getSelectedItem().toString()).get(i).getLongitude());
                        citiesList.add(languageModel);
                    }
                    citiesAdapter = new CitiesAdapterOne(ChooseCity.this, citiesList);
                    languagesRV.setAdapter(citiesAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public class CitiesAdapterOne extends RecyclerView.Adapter<CitiesAdapterOne.MyAdapter> {

        Context context;
        List<CitiesModel> data;
        private LayoutInflater inflater;

        public CitiesAdapterOne(Context context, List<CitiesModel> data) {
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public CitiesAdapterOne.MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (inflater == null) {
                inflater = LayoutInflater.from(viewGroup.getContext());
            }
            View view = inflater.inflate(R.layout.custom_spinner_cell_two, viewGroup, false);
            return new CitiesAdapterOne.MyAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CitiesAdapterOne.MyAdapter myAdapter, int i) {
            myAdapter.countrynametxt.setText(data.get(i).getCity());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyAdapter extends RecyclerView.ViewHolder {
            TextView countrynametxt;

            public MyAdapter(@NonNull View itemView) {
                super(itemView);
                countrynametxt = itemView.findViewById(R.id.countrynametxt);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("cityis", data.get(getAdapterPosition()).getCity());
                        SharedClass.setLocationFlag(context, data.get(getAdapterPosition()).getCountry(),
                                data.get(getAdapterPosition()).getCity(), data.get(getAdapterPosition()).getLatitude(),
                                data.get(getAdapterPosition()).getLongitude(), 1);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("citystatus", 1);
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(ChooseCity.this, Dashboard.class));
                    }
                });

            }
        }
    }

}