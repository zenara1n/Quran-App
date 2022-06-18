package aiou.muslim.mttech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import aiou.muslim.mttech.Activities.AboutUs;
import aiou.muslim.mttech.Receiver.Restarter;
import aiou.muslim.mttech.Receiver.YourService;
import aiou.muslim.mttech.SamplePresenter.SampleView;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import aiou.muslim.mttech.Adapters.DashboardAdapter;
import aiou.muslim.mttech.SharedData.SharedClass;
import aiou.muslim.mttech.service.SampleService;

import static aiou.muslim.mttech.Receiver.Notification_receiver.TASK_ID_TO_DISPLAY;

public class Dashboard extends AppCompatActivity implements SampleView {

    private AdView mAdView;
    int drawerpos = 0;
    DrawerLayout drawerLayout;
    ListView optionsLV;
    OptionsAdapter optionsAdapter;
    Intent mServiceIntent;
    private YourService mYourService;
    TextView headingTxt, dayTxt,mCity,monthNameText,time_praye_textr;
    private IntentFilter intentFilter;
    private SamplePresenter samplePresenter;
    private ProgressDialog progressDialog;
    ImageView menuBtn;
    SharedPreferences preferences;
    int flag = 0;
    public int counter = 0;
    private String monthName,monthDay,weekName;
    BroadcastReceiver _broadcastReceiver;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("HH:mm");

    Integer[] opions_icons_blue = {R.drawable.share_filled, R.drawable.star_filled, R.drawable.about_us, R.drawable.app_filled};
    String[] opions = {"Share App", "Rate Us", "About Us", "More Apps"};
    RecyclerView dashboardRV;
    DashboardAdapter dashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MobileAds.initialize(this, getResources().getString(R.string.admob__app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        TextClock tClock = (TextClock) findViewById(R.id.textClock);
//        tView = (TextView) findViewById(R.id.textview1);
//        btn = (Button)findViewById(R.id.btnGet);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tView.setText("Time: "+tClock.getText());
//            }
//        });
//    }


        monthName = (String) android.text.format.DateFormat.format("MMM", new Date());
        monthDay = (String) android.text.format.DateFormat.format("MM", new Date());
        weekName = (String) android.text.format.DateFormat.format("EEEE", new Date());

        monthNameText = findViewById(R.id.monthNameText);


        monthNameText.setText(weekName + ", " + monthDay + " " + monthName);



//        if (dom == 1) {
//            String monthName = "January";
//        }
//        if (dom == 2) {
//            String monthName = "February";
//        }
//        if (dom == 3) {
//            String monthName = "March";
//        }
//        if (dom == 4) {
//            String monthName = "April";
//        }
//        if (dom == 5) {
//            String monthName = "May";
//        }
//        if (dom == 6) {
//            String monthName = "June";
//        }
//        if (dom == 7) {
//            String monthName = "July";
//        }
//        if (dom == 8) {
//            String monthName = "August";
//        }
//        if (dom == 9) {
//            String monthName = "September";
//        }
//        if (dom == 10) {
//            String monthName = "October";
//        }
//        if (dom == 11) {
//            String monthName = "November";
//        }
//        if (dom == 12) {
//            String monthName = "December";
//        }


        drawerLayout = findViewById(R.id.main_drawer_layout);
        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer((int) Gravity.START);
            }
        });
        mCity = findViewById(R.id.mCity);


        mCity.setText(SharedClass.getLocationDetails(Dashboard.this, "country") + ", " + SharedClass.getLocationDetails(Dashboard.this, "city"));


        optionsLV = findViewById(R.id.optionsLV);
        optionsAdapter = new OptionsAdapter();
        optionsLV.setAdapter(optionsAdapter);
        optionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerpos = i;
                optionsAdapter.notifyDataSetChanged();
                switch (i) {
                    case 0:
                        shareApp();
                        break;
                    case 1:
                        rateapp();
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), AboutUs.class));
                        finish();
                        break;
                    case 3:
                        moreapps();
                        break;
                }
                drawerLayout.closeDrawer((int) Gravity.START);
            }
        });

        SimpleDateFormat sdff = new SimpleDateFormat("HH:mm:ss");
        String str = sdff.format(new Date());
        Log.d("dateraza", str);

        headingTxt = findViewById(R.id.headingTxt);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        dayTxt = findViewById(R.id.dayTxt);
//        dayTxt.setText(dayOfTheWeek);

        samplePresenter = new SamplePresenter(this);
        buildAlertMessageNoGps();

        dashboardRV = findViewById(R.id.dashboardRV);
        dashboardRV.setLayoutManager(new GridLayoutManager(this, 2));
        dashboardAdapter = new DashboardAdapter(Dashboard.this);
        dashboardRV.setAdapter(dashboardAdapter);

        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }


        time_praye_textr = findViewById(R.id.time_praye_textr);

        startTimer();
    }



    private Timer timer;
    private TimerTask timerTask;

    public void startTimer () {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String[] time1 = preferences.getString("fajr", "05:32").split(":");
                int fajrhh = Integer.parseInt(time1[0].trim());
                int fajrmm = Integer.parseInt(time1[1].trim());
                String[] time2 = preferences.getString("duhur", "12:21").split(":");
                int zohrhh = Integer.parseInt(time2[0].trim());
                int zohrmm = Integer.parseInt(time2[1].trim());
                String[] time3 = preferences.getString("asr", "15:33").split(":");
                int asarhh = Integer.parseInt(time3[0].trim());
                int asarmm = Integer.parseInt(time3[1].trim());
                String[] time4 = preferences.getString("maghrib", "18:02").split(":");
                int maghribhh = Integer.parseInt(time4[0].trim());
                int maghribmm = Integer.parseInt(time4[1].trim());
                String[] time5 = preferences.getString("isha", "19:10").split(":");
                int ishahh = Integer.parseInt(time5[0].trim());
                int ishamm = Integer.parseInt(time5[1].trim());

                if (DateFormat.is24HourFormat(getApplicationContext())) {
                    if (fajrhh < 12) {
                        fajrhh = fajrhh + 12;
                    }
                    if (zohrhh < 12) {
                        zohrhh = zohrhh + 12;
                    }
                    if (asarhh < 12) {
                        asarhh = asarhh + 12;
                    }
                    if (maghribhh < 12) {
                        maghribhh = maghribhh + 12;
                    }
                    if (ishahh < 12) {
                        ishahh = ishahh + 12;
                    }
                } else {
                    if (fajrhh > 12) {
                        fajrhh = fajrhh - 12;
                    }
                    if (zohrhh > 12) {
                        zohrhh = zohrhh - 12;
                    }
                    if (asarhh > 12) {
                        asarhh = asarhh - 12;
                    }
                    if (maghribhh > 12) {
                        maghribhh = maghribhh - 12;
                    }
                    if (ishahh > 12) {
                        ishahh = ishahh - 12;
                    }
                }


                Calendar today = Calendar.getInstance();
                int hh = today.get(Calendar.HOUR_OF_DAY);
                int mm = today.get(Calendar.MINUTE);
                int ss = today.get(Calendar.SECOND);

                if (ss == 5) {
                    flag = 0;
                }

                if (hh == fajrhh && mm == fajrmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 1) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        time_praye_textr.setText("fajr");
//                showNotification(getApplicationContext(), "fajr", "Its fajr time", openTaskIntent, 1, "الورد اليومي");
                        Log.i("Countfajar", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == zohrhh && mm == zohrmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 2) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        time_praye_textr.setText("zohr");
//                showNotification(getApplicationContext(), "zohr", "Its zohr time", openTaskIntent, 2, "الورد اليومي");
                        Log.i("Countzohr", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == asarhh && mm == asarmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 3) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        time_praye_textr.setText("asur");
//                showNotification(getApplicationContext(), "asar", "Its asar time", openTaskIntent, 3, "الورد اليومي");
                        Log.i("Countasar", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == maghribhh && mm == maghribmm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 4) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        time_praye_textr.setText("maghrib");
//                showNotification(getApplicationContext(), "maghrib", "Its maghrib time", openTaskIntent, 4, "الورد اليومي");
                        Log.i("Countmaghrib", "=========  " + (counter++));
                        flag = 1;
                    }
                } else if (hh == ishahh && mm == ishamm && ss == 0 && SharedClass.getAlarmStatus(getApplicationContext(), 5) == 1) {
                    Intent openTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
                    openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);
                    if (flag == 0) {
                        time_praye_textr.setText("isha");
//                showNotification(getApplicationContext(), "isha", "Its isha time", openTaskIntent, 5, "الورد اليومي");
                        Log.i("Countisha", "=========  " + (counter++));
                        flag = 1;
                    }
                }


            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }



    private void buildAlertMessageNoGps() {
        Dexter.withActivity(Dashboard.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
//                    displayProgress();
                    startService(new Intent(Dashboard.this, SampleService.class));
                } else {
                    buildAlertMessageNoGps();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                int y = 0;
            }
        }).check();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
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
        try {
            String[] split = text.trim().split(",");
            SharedClass.lat = Double.parseDouble(split[0]);
            SharedClass.lng = Double.parseDouble(split[1]);

            Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(SharedClass.lat, SharedClass.lng, 1);
                String address = addresses.get(0).getSubLocality();
                String cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

                if (!stateName.equals("")) {
                    /*SharedClass.setLocationFlag(Dashboard.this, country,
                            stateName, String.valueOf(SharedClass.lat),
                            String.valueOf(SharedClass.lng), 2);*/
                } else if (!cityName.equals("")) {
                    /*SharedClass.setLocationFlag(Dashboard.this, country,
                            cityName, String.valueOf(SharedClass.lat),
                            String.valueOf(SharedClass.lng), 2);*/
                }

                Log.d("locationis", address + "\n" + cityName + "\n" + stateName + "\n" + country);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            buildAlertMessageNoGps();
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

    public void moreapps() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Dellmont Aslam")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Made+By+Leo")));
        }
    }

    private void rateapp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Islamic Utilities App");
            String shareMessage = "\nEasiest way to use best Islamic utilities\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public class OptionsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return opions.length;
        }

        @Override
        public Object getItem(int i) {
            return opions[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(Dashboard.this, R.layout.drawer_categories_cell, null);

            LinearLayout cell = rowView.findViewById(R.id.cell);
            TextView optionname = rowView.findViewById(R.id.optionname);
            optionname.setText(opions[i]);
            optionname.setCompoundDrawablesWithIntrinsicBounds(opions_icons_blue[i], 0, 0, 0);

            if (drawerpos == i) {
                cell.setBackgroundResource(R.drawable.btn_bg_signin_solid_white);
                optionname.setTextColor(Dashboard.this.getResources().getColor(R.color.colorPrimary));
                setTextViewDrawableColor(optionname, R.color.colorPrimary);
            }

            return rowView;
        }
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

}