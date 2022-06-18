package aiou.muslim.mttech.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aiou.muslim.mttech.Adapters.NextWeatherAdapter;
import aiou.muslim.mttech.Adapters.TodayWeatherAdapter;
import aiou.muslim.mttech.Models.WeatherModel;
import aiou.muslim.mttech.R;
import aiou.muslim.mttech.SharedData.SharedClass;

public class WeatherWidget extends AppCompatActivity implements View.OnClickListener {

    public String url = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+SharedClass.lat+"&lon="+SharedClass.lng+"&cnt=17&appid=ac6f2688dbfdc24772be777529947e27";
    public String dailyurl = "http://api.openweathermap.org/data/2.5/forecast?lat="+SharedClass.lat+"&lon="+SharedClass.lng+"&lang=en&appid=ac6f2688dbfdc24772be777529947e27";
    private boolean firstEntry = true;
    private List<WeatherModel> weatherList;
    List<WeatherModel> weathers = new ArrayList<>();
    NextWeatherAdapter nextWeatherAdapter;
    TodayWeatherAdapter todayWeatherAdapter;
    RecyclerView nextWeatherRV, dailyRV;
    ImageView backBtn;
    LinearLayout btnRefresh;
    TextView timeTxt, ampmTxt, dayTxt, txtTemp, feelsText, minmaxTxt, descTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_widget);
        getSupportActionBar().hide();

        descTxt = findViewById(R.id.descTxt);
        minmaxTxt = findViewById(R.id.minmaxTxt);
        feelsText = findViewById(R.id.feelsText);
        txtTemp = findViewById(R.id.txtTemp);
        dayTxt = findViewById(R.id.dayTxt);
        timeTxt = findViewById(R.id.timeTxt);
        ampmTxt = findViewById(R.id.ampmTxt);
        btnRefresh = findViewById(R.id.btnRefresh);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        weatherList = new ArrayList<>();
        dailyRV = findViewById(R.id.dailyRV);
        dailyRV.setItemAnimator(new DefaultItemAnimator());

        nextWeatherRV = findViewById(R.id.nextWeatherRV);
        nextWeatherRV.setLayoutManager(new LinearLayoutManager(this));
        getSingleTodayWeather();
        getNextWeather();
        getTodayWeather();
        Log.d("response", "response");

        timeTxt.setText(getCurrentTime("time"));
        ampmTxt.setText(getCurrentTime("ampm").toUpperCase());
        dayTxt.setText(getDayValue());

    }

    public void getSingleTodayWeather() {
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherWidget.this);
        StringRequest request = new StringRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?lat="+SharedClass.lat+"&lon="+SharedClass.lng+"&appid=ac6f2688dbfdc24772be777529947e27", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String jsonData = response;
                    if (jsonData != null) {
                        JSONObject Jobject = new JSONObject(jsonData);
                        JSONObject main = Jobject.getJSONObject("main");
                        float temp = Math.round(Float.valueOf(main.getString("temp")) - 272.15f);
                        float temp_min = Math.round(Float.valueOf(main.getString("temp_min")) - 272.15f);
                        float temp_max = Math.round(Float.valueOf(main.getString("temp_max")) - 272.15f);
                        float humidity = Math.round(Float.valueOf(main.getString("humidity")) - 272.15f);
//                        String sea_level = main.getString("sea_level");
                        float feels_like = Math.round(Float.valueOf(main.getString("feels_like")) - 272.15f);

                        JSONArray weather = Jobject.getJSONArray("weather");
                        String desc = weather.getJSONObject(0).getString("description");
                        String icon = weather.getJSONObject(0).getString("icon");

                        txtTemp.setText(String.valueOf(Math.round(temp))+"°C");
                        feelsText.setText("Feels like: "+String.valueOf(Math.round(feels_like)));
                        minmaxTxt.setText("Min "+Math.round(temp_min)+"° | Max "+Math.round(temp_max)+"°");
                        descTxt.setText(desc);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        requestQueue.add(request);
    }

    public void getNextWeather() {
        weathers.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherWidget.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String jsonData = response;
                    if (jsonData != null) {
                        JSONObject Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONArray("list");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            JSONObject main = object.getJSONObject("temp");
                            JSONArray weather = object.getJSONArray("weather");
                            String desc = weather.getJSONObject(0).getString("description");
                            Log.i("URL_WITHER", "desc : " + desc);
                            String icon = weather.getJSONObject(0).getString("icon");
                            String date = object.getString("dt");
                            String temp = main.getString("min");
                            String temp_min = main.getString("min");
                            String temp_max = main.getString("max");
                            String humidity = object.getString("humidity");
                            String windSpeed = object.getString("speed");

                            weathers.add(new WeatherModel
                                    (date, Math.round(Float.valueOf(temp) - 272.15f) + "",
                                            Math.round(Float.valueOf(temp_min) - 272.15f) + "",
                                            Math.round(Float.valueOf(temp_max) - 272.15f) + "",
                                            icon, desc, humidity, windSpeed));
                        }

                        Log.d("size", weathers.size() + "");
                        if (weathers.size() > 0) {
                            nextWeatherAdapter = new NextWeatherAdapter(WeatherWidget.this, weathers);
                            nextWeatherRV.setAdapter(nextWeatherAdapter);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        requestQueue.add(request);
    }

    public void getTodayWeather() {
        weatherList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherWidget.this);
        StringRequest request = new StringRequest(Request.Method.GET, dailyurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String jsonData = response;
                    if (jsonData != null) {
                        JSONObject Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONArray("list");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            JSONObject main = object.getJSONObject("main");
                            JSONArray weather = object.getJSONArray("weather");
                            String desc = weather.getJSONObject(0).getString("description");
                            Log.i("URL_WITHER", "desc : " + desc);
                            String icon = weather.getJSONObject(0).getString("icon");
                            String date = object.getString("dt_txt");
                            String temp = main.getString("temp");
                            String temp_min = main.getString("temp_min");
                            String temp_max = main.getString("temp_max");
                            String humidity = main.getString("humidity");
                            JSONObject wind = object.getJSONObject("wind");
                            String windSpeed = wind.getString("speed");

                            weatherList.add(new WeatherModel
                                    (date, Math.round(Float.valueOf(temp) - 272.15f) + "",
                                            Math.round(Float.valueOf(temp_min) - 272.15f) + "",
                                            Math.round(Float.valueOf(temp_max) - 272.15f) + "",
                                            icon, desc, humidity, windSpeed));
                        }

                        if (weatherList.size() > 0) {
                            todayWeatherAdapter = new TodayWeatherAdapter(weatherList, WeatherWidget.this);
                            dailyRV.setAdapter(todayWeatherAdapter);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.btnRefresh:
                timeTxt.setText(getCurrentTime("time"));
                ampmTxt.setText(getCurrentTime("ampm").toUpperCase());
                dayTxt.setText(getDayValue());
                getSingleTodayWeather();
                getNextWeather();
                getTodayWeather();
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public String getCurrentTime(String query) {
        String value = "";
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String dateToStr = format.format(today);
        SimpleDateFormat format1 = new SimpleDateFormat("a");
        String dateToStr1 = format1.format(today);
        if (query.equals("time")) {
            value = dateToStr;
        } else {
            value = dateToStr1;
        }
        return value;
    }

    public String getDayValue() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());
        String date = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        return dayOfTheWeek + ", " + month_name + " " + date + "- " + year;
    }

}