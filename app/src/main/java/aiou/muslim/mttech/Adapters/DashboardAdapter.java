package aiou.muslim.mttech.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import aiou.muslim.mttech.Activities.AllahNames;
import aiou.muslim.mttech.Activities.AzkarCategoriesActivity;
import aiou.muslim.mttech.Activities.CompassActivity;
import aiou.muslim.mttech.Activities.CounterActivity;
import aiou.muslim.mttech.Activities.HijriGregorianConv;
import aiou.muslim.mttech.Activities.PrayerTimes;
import aiou.muslim.mttech.Activities.QuranMain;
import aiou.muslim.mttech.Activities.SettingsActivity;
import aiou.muslim.mttech.Activities.SixKalmasActivity;
import aiou.muslim.mttech.Activities.WeatherWidget;
import aiou.muslim.mttech.Activities.ZakatCalculator;
import aiou.muslim.mttech.Masjid;
import aiou.muslim.mttech.R;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyAdapter> {

    private InterstitialAd mInterstitialAd;
    String[] options = {"Prayer Timings","Nearby Masjid", "Tasbeeh Counter", "Dates Conversion", "Qibla Compass", "Learn Quran", "6 kalma", "Azkar", "Zakat Calculator", "Weather", "Allah 99 Names", "Settings"};
    Integer[] icons = {R.drawable.timings,R.drawable.mosque,R.drawable.counter, R.drawable.dua, R.drawable.compass, R.drawable.quran, R.drawable.kalma, R.drawable.dua, R.drawable.calculator, R.drawable.weather, R.drawable.allahnames, R.drawable.settings};
    private LayoutInflater inflater;
    Context context;

    public DashboardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater==null){
            inflater=LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.dashboard_cell, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        holder.optionTxt.setText(options[position]);
        holder.optionimage.setImageResource(icons[position]);
    }

    @Override
    public int getItemCount() {
        return options.length;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {

        TextView optionTxt;
        ImageView optionimage;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            optionTxt=itemView.findViewById(R.id.optionTxt);
            optionimage=itemView.findViewById(R.id.optionimage);

            MobileAds.initialize(context, context.getResources().getString(R.string.admob__app_id));
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interstitial_adunit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showandstart(getAdapterPosition());
                }
            });

        }
    }


    public void showandstart(final int pos) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            fun(pos);
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                fun(pos);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                fun(pos);
            }

        });
    }

    public void fun(final int pos){
        if (pos==0){
            context.startActivity(new Intent(context, PrayerTimes.class));
        }
        if (pos==1){
            context.startActivity(new Intent(context, Masjid.class));
        }
        if (pos==2){
            context.startActivity(new Intent(context, CounterActivity.class));
        }
        if (pos==3){
            context.startActivity(new Intent(context, HijriGregorianConv.class));
        }
        if (pos==4){
            context.startActivity(new Intent(context, CompassActivity.class));
        }
        if (pos==5){
            context.startActivity(new Intent(context, QuranMain.class));
        }
        if (pos==6){
            context.startActivity(new Intent(context, SixKalmasActivity.class));
        }
        if (pos==7){
            context.startActivity(new Intent(context, AzkarCategoriesActivity.class));
        }
        if (pos==8){
            context.startActivity(new Intent(context, ZakatCalculator.class));
        }
        if (pos==9){
            context.startActivity(new Intent(context, WeatherWidget.class));
        }
        if (pos==10){
            context.startActivity(new Intent(context, AllahNames.class));
        }
        if (pos==11){
            context.startActivity(new Intent(context, SettingsActivity.class));
        }
    }

}