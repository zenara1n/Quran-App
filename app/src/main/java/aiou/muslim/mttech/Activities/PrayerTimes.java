package aiou.muslim.mttech.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import aiou.muslim.mttech.Helper.HttpHandler;
import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SharedData.SharedClass;

public class PrayerTimes extends AppCompatActivity {

    Element fajr, sunrise, zohr, asr, maghrib, isha;
    String sfajr, ssunrise, szohr, sasr, smaghrib, sisha;

    ImageView backBtn;
    String timeStamp;
    ArrayList<String> NotifSalatMessages = new ArrayList<>();
    ArrayList<String> NotifMessages = new ArrayList<>();
    TextView mFajr, mDuhur, mAsr, mMaghrib, mIsha, mCity, methodTxt, monthTxten, monthTxtar, dayTxt, dateTxt, weekDayEn, weekDayAr,
            sunrisetxt, sunsettxt, dohrTxtEn, dohrTxtAr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_times);
        getSupportActionBar().hide();

        dohrTxtEn = findViewById(R.id.dohrTxtEn);
        dohrTxtAr = findViewById(R.id.dohrTxtAr);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if (dayOfTheWeek.equals("Friday")) {
            dohrTxtEn.setText("Friday Prayer");
            dohrTxtAr.setText("الجمعہ");
        }

        sunrisetxt = findViewById(R.id.sunrisetxt);
        sunsettxt = findViewById(R.id.sunsettxt);
        weekDayEn = findViewById(R.id.weekDayEn);
        weekDayAr = findViewById(R.id.weekDayAr);
        dayTxt = findViewById(R.id.dayTxt);
        dateTxt = findViewById(R.id.dateTxt);
        monthTxten = findViewById(R.id.monthTxten);
        monthTxtar = findViewById(R.id.monthTxtar);
        methodTxt = findViewById(R.id.methodTxt);
        methodTxt = findViewById(R.id.methodTxt);
        methodTxt.setText(SharedClass.methods[SharedClass.getMethod(PrayerTimes.this)]);
        backBtn = findViewById(R.id.backBtn);

        SharedPreferences salatpref = getSharedPreferences("lastprayertimes", MODE_PRIVATE);

        sfajr = salatpref.getString("fajr",null);
        szohr = salatpref.getString("duhur",null);
        sasr = salatpref.getString("asr",null);
        smaghrib = salatpref.getString("maghrib",null);
        sisha = salatpref.getString("isha",null);


        mFajr = findViewById(R.id.fajr);
        mDuhur = findViewById(R.id.duhur);
        mAsr = findViewById(R.id.asr);
        mMaghrib = findViewById(R.id.maghrib);
        mIsha = findViewById(R.id.isha);

        mFajr.setText(sfajr);
        mDuhur.setText(szohr);
        mAsr.setText(sasr);
        mMaghrib.setText(smaghrib);
        mIsha.setText(sisha);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        Log.d("timestampis", timeStamp);

        NotifMessages();
        if (isNetworkStatusAvailable(getApplicationContext())) {
//            Toast.makeText(getApplicationContext(), getString(R.string.internetfound), Toast.LENGTH_SHORT).show();
            SharedClass.lat = Double.parseDouble(SharedClass.getLocationDetails(PrayerTimes.this, "lat"));
            SharedClass.lng = Double.parseDouble(SharedClass.getLocationDetails(PrayerTimes.this, "lng"));
            new GetPrayerTimes().execute();
        } else {
//            Toast.makeText(getApplicationContext(), getString(R.string.internetlost), Toast.LENGTH_SHORT).show();
            LoadPreviousSalatData();
        }

        new GetOtherTimes().execute("https://sunsetsunrisetime.com/prayer/" + SharedClass.getLocationDetails(PrayerTimes.this, "city").trim().toLowerCase());

    }

    boolean isNetworkStatusAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
    }

    public void LoadPreviousSalatData() {
        SharedPreferences salatpref = getSharedPreferences("lastprayertimes", MODE_PRIVATE);
        TextView mxFajr, mxDuhur, mxAsr, mxMaghrib, mxIsha, mxCity;
        mxFajr = findViewById(R.id.fajr);
        mxDuhur = findViewById(R.id.duhur);
        mxAsr = findViewById(R.id.asr);
        mxMaghrib = findViewById(R.id.maghrib);
        mxIsha = findViewById(R.id.isha);
        mxCity = findViewById(R.id.city);
        String GETPrayerCity = salatpref.getString("city", getString(R.string.city));
        String GETfajr = salatpref.getString("fajr", "00:00");
        String GETduhur = salatpref.getString("duhur", "00:00");
        String GETasr = salatpref.getString("asr", "00:00");
        String GETmaghrib = salatpref.getString("maghrib", "00:00");
        String GETisha = salatpref.getString("isha", "00:00");

        mxCity.setText(GETPrayerCity);
        mxFajr.setText(GETfajr);
        mxDuhur.setText(GETduhur);
        mxAsr.setText(GETasr);
        mxMaghrib.setText(GETmaghrib);
        mxIsha.setText(GETisha);
    }


    private class GetOtherTimes extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                Document mBlogDocument = Jsoup.connect(params[0]).get();

                Log.e("ABCD", String.valueOf(mBlogDocument));

                fajr = null;
                sunrise = null;
                zohr = null;
                asr = null;
                maghrib = null;
                isha = null;

                try {
                    Elements tmp = mBlogDocument.select("p[class=text-center]");

                    Log.d("ABCD",tmp.text());

                    Iterator<Element> it = tmp.iterator();
                    fajr = it.next();
                    fajr.append(String.valueOf(it.next()));
                    sunrise = it.next();
                    sunrise.append(String.valueOf(it.next()));
                    zohr = it.next();
                    zohr.append(String.valueOf(it.next()));
                    asr = it.next();
                    asr.append(String.valueOf(it.next()));
                    maghrib = it.next();
                    maghrib.append(String.valueOf(it.next()));
                    isha = it.next();
                    isha.append(String.valueOf(it.next()));

//                    int count = 0;
//                    while (it.hasNext()) {
//                        count++;
//                        if (count == 1 || count == 2) {
//                            Element o = it.next();
//                            fajr = o;
//                            Log.e("ABCD",fajr.text());
//                            break;
//                        }
//
//                    }



//                    fajr = mBlogDocument.select("p[id=digital-clock-2]");
//                    sunrise = mBlogDocument.select("p[id=digital-clock-2]");
//                    zohr = mBlogDocument.select("p[id=digital-clock-3]");
//                    asr = mBlogDocument.select("p[id=digital-clock-4]");
//                    maghrib = mBlogDocument.select("p[id=digital-clock-5]");
//                    isha = mBlogDocument.select("p[id=digital-clock-6]");
                    String s = tmp.text();
                    String[] arr = s.split(" ");


                } catch (Exception e) {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            Toast.makeText(getApplicationContext(), getString(R.string.prayertimesloaded), Toast.LENGTH_LONG).show();
            SharedPreferences salatpref = getSharedPreferences("lastprayertimes", MODE_PRIVATE);
//            editor.putString("fajr", fajr.text());
//            editor.putString("duhur", zohr.text());
//            editor.putString("asr", asr.text());
//            editor.putString("maghrib", maghrib.text());
//            editor.putString("isha", isha.text());
//            editor.apply();
//            Toast.makeText(getApplicationContext(), fajr.text(), Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = salatpref.edit();


            editor.putString("fajr", sfajr);
            editor.putString("duhur", szohr);
            editor.putString("asr", sasr);
            editor.putString("maghrib", smaghrib);
            editor.putString("isha", sisha);
            editor.apply();
//            Toast.makeText(getApplicationContext(), fajr.text(), Toast.LENGTH_LONG).show();



            mFajr = findViewById(R.id.fajr);
            mDuhur = findViewById(R.id.duhur);
            mAsr = findViewById(R.id.asr);
            mMaghrib = findViewById(R.id.maghrib);
            mIsha = findViewById(R.id.isha);
            mCity = findViewById(R.id.city);

            mCity.setText(SharedClass.getLocationDetails(PrayerTimes.this, "country") + ", " + SharedClass.getLocationDetails(PrayerTimes.this, "city"));

//            sunsettxt.setText(sunset);
//            weekDayEn.setText(weekdayen);
//            weekDayAr.setText(weekdayar);
//            dayTxt.setText(hijriday);
//            dateTxt.setText(datehijri);
//            monthTxten.setText(hijrimonthen);
//            monthTxtar.setText(hijrimonthar);
//            Log.e("ABCDE",fajr.text());
            sunrisetxt.setText(ssunrise);
            mFajr.setText(sfajr);
            mDuhur.setText(szohr);
            mAsr.setText(sasr);
            mMaghrib.setText(smaghrib);
            mIsha.setText(sisha);

        }
    }

    private class GetPrayerTimes extends AsyncTask<Void, Void, Void> {
        SharedPreferences salatpref = getSharedPreferences("lastprayertimes", MODE_PRIVATE);
        String GETPrayerCity = salatpref.getString("city", "Rawalpindi");
        String fajr, duhur, asr, maghrib, isha, hijriday, datehijri, hijrimonthen, hijrimonthar, weekdayen, weekdayar, sunrise, sunset;
        Boolean Passed = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
//            String url = "http://api.aladhan.com/v1/timings/" + timeStamp + "?latitude=" + SharedClass.lat + "&longitude=" + SharedClass.lng + "&method=2"/*+SharedClass.getMethod(PrayerTimes.this)*/;
            String url = "http://api.aladhan.com/v1/timingsByCity?city=" + SharedClass.getLocationDetails(PrayerTimes.this, "city") + "&country=" + SharedClass.getLocationDetails(PrayerTimes.this, "country") + "&method=" + SharedClass.getMethod(PrayerTimes.this);
            String jsonStr = sh.makeServiceCall(url);
            Log.e("TAG", "Response from url: " + jsonStr);
            Log.e("TAG", "thisurl: " + url);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject data = jsonObj.getJSONObject("data");
                    JSONObject prayertimes = data.getJSONObject("timings");
                    JSONObject prayerdate = data.getJSONObject("date");
                    JSONObject hijridate = prayerdate.getJSONObject("hijri");
                    JSONObject month = hijridate.getJSONObject("month");
                    JSONObject week = hijridate.getJSONObject("weekday");

                    hijrimonthen = month.getString("en");
                    hijrimonthar = month.getString("ar");
                    hijriday = hijridate.getString("day");
                    datehijri = hijridate.getString("date");
                    weekdayen = week.getString("en");
                    weekdayar = week.getString("ar");

                    fajr = prayertimes.getString("Fajr");
                    duhur = prayertimes.getString("Dhuhr");
                    asr = prayertimes.getString("Asr");
                    maghrib = prayertimes.getString("Maghrib");
                    isha = prayertimes.getString("Isha");
                    sunrise = prayertimes.getString("Sunrise");
                    sunset = prayertimes.getString("Sunset");
                    Log.e("ABCD",fajr+duhur);


                    sfajr = fajr;
                    szohr = duhur;
                    sasr = asr;
                    smaghrib = maghrib;
                    sisha = isha;
                    ssunrise = sunrise;
                    ssunrise = sunrise;

                    Passed = true;
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getString(R.string.errorloadprayertimes), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e("TAG", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.errorloadprayertimes), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mFajr = findViewById(R.id.fajr);
            Log.e("TAG",fajr+" "+mFajr.getText());
            mDuhur = findViewById(R.id.duhur);
            mAsr = findViewById(R.id.asr);
            mMaghrib = findViewById(R.id.maghrib);
            mIsha = findViewById(R.id.isha);
            mCity = findViewById(R.id.city);

            if (SharedClass.lat == 0.0) {
                mCity.setText(GETPrayerCity);
            } else {
                mCity.setText(SharedClass.getLocationDetails(PrayerTimes.this, "country") + ", " + SharedClass.getLocationDetails(PrayerTimes.this, "city"));
            }

            sunrisetxt.setText(sunrise);
            sunsettxt.setText(sunset);
            weekDayEn.setText(weekdayen);
            weekDayAr.setText(weekdayar);
            dayTxt.setText(hijriday);
            dateTxt.setText(datehijri);
            monthTxten.setText(hijrimonthen);
            monthTxtar.setText(hijrimonthar);
//            mFajr.setText(fajr);
//            mDuhur.setText(duhur);
//            mAsr.setText(asr);
//            mMaghrib.setText(maghrib);
//            mIsha.setText(isha);
        }
    }

    public void NotifMessages() {
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought1) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought2) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought3) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought4) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought5) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought6) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought7) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought8) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought9) + "“");
        NotifMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.thought10) + "“");
        NotifMessages.add(getString(R.string.AdkarExtra1));
        NotifMessages.add(getString(R.string.AdkarExtra2));
        NotifMessages.add(getString(R.string.AdkarExtra3));
        NotifMessages.add(getString(R.string.AdkarExtra4));
        NotifMessages.add(getString(R.string.AdkarExtra5));
        NotifMessages.add(getString(R.string.AdkarExtra6));
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought1) + "“");
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought2) + "“");
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought3) + "“");
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought4) + "“");
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought5) + "“");
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought6) + "“");
        NotifSalatMessages.add(getString(R.string.menu_fadlGod1) + " ”" + getString(R.string.SalatThought7) + "“");
        NotifSalatMessages.add(getString(R.string.SalatExtra1));
        NotifSalatMessages.add(getString(R.string.SalatExtra2));
        NotifSalatMessages.add(getString(R.string.SalatExtra3));
        NotifSalatMessages.add(getString(R.string.SalatExtra4));
        NotifSalatMessages.add(getString(R.string.SalatExtra5));
    }

}