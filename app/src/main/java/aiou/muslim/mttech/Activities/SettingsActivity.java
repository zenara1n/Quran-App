package aiou.muslim.mttech.Activities;

import aiou.muslim.mttech.SamplePresenter.SampleView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import aiou.muslim.mttech.Adapters.CitiesAdapter;
import aiou.muslim.mttech.Adapters.SpinnerCustomAdapter;
import aiou.muslim.mttech.Manager.DBManager;
import aiou.muslim.mttech.Models.CitiesModel;
import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SamplePresenter;
import aiou.muslim.mttech.SharedData.SharedClass;
import aiou.muslim.mttech.service.SampleService;

public class SettingsActivity extends AppCompatActivity implements SampleView, View.OnClickListener  {

    ImageView backBtn;
    MethodsCustomAdapter methodsCustomAdapter;
    int flag = 2;
    private IntentFilter intentFilter;
    private SamplePresenter samplePresenter;
    private ProgressDialog progressDialog;
    public static LinearLayout locationLayout;
    DBManager dbManager;
    List<CitiesModel> citiesList = new ArrayList<>();
    List<CitiesModel> filterData = new ArrayList<>();
    CitiesAdapter citiesAdapter;
    LinearLayout btnCurrent, btnCity, btnMethod;
    TextView txtCurrent;
    public static TextView txtCountry, txtCity, txtLocation, txtMethod;
    TextView txtFajrOn, txtFajrOff, txtZohrOn, txtZohrOff, txtAsarOn, txtAsarOff, txtMaghribOn, txtMaghribOff, txtIshaOn, txtIshaOff, txtCounterOn, txtCounterOff;
    Spinner citySpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        txtCounterOff=findViewById(R.id.txtCounterOff);
        txtCounterOff.setOnClickListener(this);
        txtCounterOn=findViewById(R.id.txtCounterOn);
        txtCounterOn.setOnClickListener(this);
        txtFajrOn=findViewById(R.id.txtFajrOn);
        txtFajrOn.setOnClickListener(this);
        txtFajrOff=findViewById(R.id.txtFajrOff);
        txtFajrOff.setOnClickListener(this);
        txtZohrOn=findViewById(R.id.txtZohrOn);
        txtZohrOn.setOnClickListener(this);
        txtZohrOff=findViewById(R.id.txtZohrOff);
        txtZohrOff.setOnClickListener(this);
        txtAsarOn=findViewById(R.id.txtAsarOn);
        txtAsarOn.setOnClickListener(this);
        txtAsarOff=findViewById(R.id.txtAsarOff);
        txtAsarOff.setOnClickListener(this);
        txtMaghribOn=findViewById(R.id.txtMaghribOn);
        txtMaghribOn.setOnClickListener(this);
        txtMaghribOff=findViewById(R.id.txtMaghribOff);
        txtMaghribOff.setOnClickListener(this);
        txtIshaOn=findViewById(R.id.txtIshaOn);
        txtIshaOn.setOnClickListener(this);
        txtIshaOff=findViewById(R.id.txtIshaOff);
        txtIshaOff.setOnClickListener(this);

        checkAlarmStatus();
        checkCounterSound();

        flag = SharedClass.getMethod(SettingsActivity.this);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnMethod = findViewById(R.id.btnMethod);
        txtMethod = findViewById(R.id.txtMethod);
        txtMethod.setText(SharedClass.methods[flag]);
        txtCountry = findViewById(R.id.txtCountry);
        txtCity = findViewById(R.id.txtCity);
        txtLocation = findViewById(R.id.txtLocation);
        txtCurrent = findViewById(R.id.txtCurrent);
        citySpinner = findViewById(R.id.citySpinner);
        btnCurrent = findViewById(R.id.btnCurrent);
        btnCity = findViewById(R.id.btnCity);

        dbManager = new DBManager(this);
        dbManager.open();
        try {
            dbManager.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        locationLayout = findViewById(R.id.locationLayout);
        if (SharedClass.getLocationFlag(SettingsActivity.this) == 0) {
            locationLayout.setVisibility(View.GONE);
        } else if (SharedClass.getLocationFlag(SettingsActivity.this) == 1) {
            btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_stroke);
            txtCurrent.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            locationLayout.setVisibility(View.VISIBLE);
            txtCountry.setText(SharedClass.getLocationDetails(SettingsActivity.this, "country"));
            txtCity.setText(SharedClass.getLocationDetails(SettingsActivity.this, "city"));
            txtLocation.setText(SharedClass.getLocationDetails(SettingsActivity.this, "lat") + "," + SharedClass.getLocationDetails(SettingsActivity.this, "lng"));
        } else if (SharedClass.getLocationFlag(SettingsActivity.this) == 2) {
            btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_solid);
            txtCurrent.setTextColor(getResources().getColor(R.color.white));
            locationLayout.setVisibility(View.VISIBLE);
            txtCountry.setText(SharedClass.getLocationDetails(SettingsActivity.this, "country"));
            txtCity.setText(SharedClass.getLocationDetails(SettingsActivity.this, "city"));
            txtLocation.setText(SharedClass.getLocationDetails(SettingsActivity.this, "lat") + "," + SharedClass.getLocationDetails(SettingsActivity.this, "lng"));
        }

        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_solid);
                txtCurrent.setTextColor(getResources().getColor(R.color.white));

                samplePresenter = new SamplePresenter(SettingsActivity.this);
                displayProgress();
                startService(new Intent(SettingsActivity.this, SampleService.class));
            }
        });

        SpinnerCustomAdapter spinnerCustomAdapter = new SpinnerCustomAdapter(SettingsActivity.this, dbManager.getCountries());
        citySpinner.setAdapter(spinnerCustomAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    btnCurrent.setBackgroundResource(R.drawable.btn_bg_signin_stroke);
                    txtCurrent.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    showDialog(citySpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodsDialog();
            }
        });

    }

    void checkAlarmStatus(){
        if (SharedClass.getAlarmStatus(SettingsActivity.this, 1)==1){
            txtFajrOn.setTextColor(getResources().getColor(R.color.white));
            txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtFajrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtFajrOff.setTextColor(getResources().getColor(R.color.white));
            txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtFajrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 2)==1){
            txtZohrOn.setTextColor(getResources().getColor(R.color.white));
            txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtZohrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtZohrOff.setTextColor(getResources().getColor(R.color.white));
            txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtZohrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 3)==1){
            txtAsarOn.setTextColor(getResources().getColor(R.color.white));
            txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtAsarOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtAsarOff.setTextColor(getResources().getColor(R.color.white));
            txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtAsarOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 4)==1){
            txtMaghribOn.setTextColor(getResources().getColor(R.color.white));
            txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtMaghribOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtMaghribOff.setTextColor(getResources().getColor(R.color.white));
            txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtMaghribOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }

        if (SharedClass.getAlarmStatus(SettingsActivity.this, 5)==1){
            txtIshaOn.setTextColor(getResources().getColor(R.color.white));
            txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtIshaOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtIshaOff.setTextColor(getResources().getColor(R.color.white));
            txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtIshaOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }
    }

    void checkCounterSound(){
        if (SharedClass.getTasbeenSound(SettingsActivity.this)==1){
            txtCounterOn.setTextColor(getResources().getColor(R.color.white));
            txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtCounterOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }else{
            txtCounterOff.setTextColor(getResources().getColor(R.color.white));
            txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
            txtCounterOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtFajrOn:
                txtFajrOn.setTextColor(getResources().getColor(R.color.white));
                txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtFajrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 1, 1);
                break;
            case R.id.txtFajrOff:
                txtFajrOff.setTextColor(getResources().getColor(R.color.white));
                txtFajrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtFajrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtFajrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 1, 0);
                break;
            case R.id.txtZohrOn:
                txtZohrOn.setTextColor(getResources().getColor(R.color.white));
                txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtZohrOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 2, 1);
                break;
            case R.id.txtZohrOff:
                txtZohrOff.setTextColor(getResources().getColor(R.color.white));
                txtZohrOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtZohrOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtZohrOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 2, 0);
                break;
            case R.id.txtAsarOn:
                txtAsarOn.setTextColor(getResources().getColor(R.color.white));
                txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtAsarOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 3, 1);
                break;
            case R.id.txtAsarOff:
                txtAsarOff.setTextColor(getResources().getColor(R.color.white));
                txtAsarOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtAsarOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtAsarOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 3, 0);
                break;
            case R.id.txtMaghribOn:
                txtMaghribOn.setTextColor(getResources().getColor(R.color.white));
                txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtMaghribOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 4, 1);
                break;
            case R.id.txtMaghribOff:
                txtMaghribOff.setTextColor(getResources().getColor(R.color.white));
                txtMaghribOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtMaghribOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtMaghribOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 4, 0);
                break;
            case R.id.txtIshaOn:
                txtIshaOn.setTextColor(getResources().getColor(R.color.white));
                txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtIshaOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 5, 1);
                break;
            case R.id.txtIshaOff:
                txtIshaOff.setTextColor(getResources().getColor(R.color.white));
                txtIshaOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtIshaOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtIshaOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.setAlarmStatus(SettingsActivity.this, 5, 0);
                break;
            case R.id.txtCounterOn:
                txtCounterOn.setTextColor(getResources().getColor(R.color.white));
                txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtCounterOff.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.saveTasbeenSound(SettingsActivity.this, 1);
                break;
            case R.id.txtCounterOff:
                txtCounterOff.setTextColor(getResources().getColor(R.color.white));
                txtCounterOff.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtCounterOn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtCounterOn.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                SharedClass.saveTasbeenSound(SettingsActivity.this, 0);
                break;
            default:
                break;
        }
    }

    public class MethodsCustomAdapter extends BaseAdapter {

        Context context;

        public MethodsCustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return SharedClass.methods.length;
        }

        @Override
        public Object getItem(int position) {
            return SharedClass.methods[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = View.inflate(context, R.layout.custom_method_cell, null);

            TextView codeTxt = rowView.findViewById(R.id.countrynametxt);
            codeTxt.setText(SharedClass.methods[position]);

            if (position==flag){
                codeTxt.setTextColor(getResources().getColor(R.color.white));
                codeTxt.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            return rowView;
        }
    }

    public void methodsDialog() {

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.method_cell);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(SettingsActivity.this) / 100) * 80), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        ListView methodsLV = (ListView) dialog.findViewById(R.id.methodsLV);
        methodsCustomAdapter = new MethodsCustomAdapter(SettingsActivity.this);
        methodsLV.setAdapter(methodsCustomAdapter);
        methodsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                flag = i;
                SharedClass.setMethod(SettingsActivity.this, i);
                txtMethod.setText(SharedClass.methods[i]);
                methodsCustomAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showDialog(String country) {

        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.dialog_rv);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(SettingsActivity.this) / 100) * 80), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView languagesRV = (RecyclerView) dialog.findViewById(R.id.langyagesRV);
        languagesRV.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < dbManager.getCities(country).size(); i++) {
            CitiesModel languageModel = new CitiesModel();
            languageModel.setId(dbManager.getCities(country).get(i).getId());
            languageModel.setCountry(dbManager.getCities(country).get(i).getCountry());
            languageModel.setCity(dbManager.getCities(country).get(i).getCity());
            languageModel.setLatitude(dbManager.getCities(country).get(i).getLatitude());
            languageModel.setLongitude(dbManager.getCities(country).get(i).getLongitude());
            citiesList.add(languageModel);
            filterData.add(languageModel);
        }
        citiesAdapter = new CitiesAdapter(SettingsActivity.this, citiesList, dialog);
        languagesRV.setAdapter(citiesAdapter);

        final EditText searchEdt = (EditText) dialog.findViewById(R.id.searchEdt);
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (citiesList.size() > 0) {
                    citiesList.clear();
                }

                filter(searchEdt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.show();

    }

    public void filter(String text) {
        citiesList.clear();

        if (text.length() == 0) {
            citiesList.clear();
        }

        for (int i = 0; i < filterData.size(); i++) {

            if (filterData.get(i).getCity().toLowerCase().contains(text)) {

                CitiesModel languageModel = new CitiesModel();
                languageModel.setId(filterData.get(i).getId());
                languageModel.setCountry(filterData.get(i).getCountry());
                languageModel.setCity(filterData.get(i).getCity());
                languageModel.setLatitude(filterData.get(i).getLatitude());
                languageModel.setLongitude(filterData.get(i).getLongitude());
                if (!citiesList.contains(languageModel.getCity())) {
                    citiesList.add(languageModel);
                }
            }

            citiesAdapter.notifyDataSetChanged();
        }
    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public void setText(String text) {
        String[] split = text.trim().split(",");
        double lat = Double.parseDouble(split[0]);
        double lng = Double.parseDouble(split[1]);

        Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            locationLayout.setVisibility(View.VISIBLE);

            txtCountry.setText(country);
            if (!stateName.equals("")) {
                SharedClass.setLocationFlag(SettingsActivity.this, country,
                        stateName, String.valueOf(lat),
                        String.valueOf(lng), 2);
                txtCity.setText(stateName);
            }else if (!cityName.equals("")) {
                SharedClass.setLocationFlag(SettingsActivity.this, country,
                        cityName, String.valueOf(lat),
                        String.valueOf(lng), 2);
                txtCity.setText(cityName);
            }
            txtLocation.setText(text.trim());

            Log.d("locationis", address+"\n"+cityName+"\n"+stateName+"\n"+country);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProgress(String text) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(text);
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private IntentFilter getIntentFilter() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(SampleService.ACTION_LOCATION_CHANGED);
            intentFilter.addAction(SampleService.ACTION_LOCATION_FAILED);
            intentFilter.addAction(SampleService.ACTION_PROCESS_CHANGED);
        }
        return intentFilter;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SampleService.ACTION_LOCATION_CHANGED)) {
                samplePresenter.onLocationChanged((Location) intent.getParcelableExtra(SampleService.EXTRA_LOCATION));
            } else if (action.equals(SampleService.ACTION_LOCATION_FAILED)) {
                //noinspection WrongConstant
                samplePresenter.onLocationFailed(intent.getIntExtra(SampleService.EXTRA_FAIL_TYPE, FailType.UNKNOWN));
            } else if (action.equals(SampleService.ACTION_PROCESS_CHANGED)) {
                //noinspection WrongConstant
                samplePresenter.onProcessTypeChanged(intent.getIntExtra(SampleService.EXTRA_PROCESS_TYPE,
                        ProcessType.GETTING_LOCATION_FROM_CUSTOM_PROVIDER));
            }
        }
    };

}
