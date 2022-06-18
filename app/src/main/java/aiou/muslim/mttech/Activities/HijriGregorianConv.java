package aiou.muslim.mttech.Activities;

import java.util.Calendar;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kenmeidearu.materialdatetimepicker.date.DatePickerDialog;

import aiou.muslim.mttech.R;

public class HijriGregorianConv extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageView backBtn;
    int y=0,m=0,d=0,y_=0,m_=0,d_=0;
    TextView inday,inmonth,inyear, txtHijri, txtGregorian;
    RadioButton tohijri,togregorian;
    TextView outdayw,outday,outmonth,outyear;
    Button pickDateBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear);
        getSupportActionBar().hide();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pickDateBtn = (Button) findViewById(R.id.pickDateBtn);

        txtGregorian = (TextView)findViewById(R.id.txtGregorian);
        txtHijri = (TextView)findViewById(R.id.txtHijri);
        inday = (TextView)findViewById(R.id.inday);
        inmonth = (TextView)findViewById(R.id.inmonth);
        inyear = (TextView)findViewById(R.id.inyear);
        tohijri = (RadioButton)findViewById(R.id.tohijri);
        togregorian = (RadioButton)findViewById(R.id.togregorian);
        outdayw = (TextView)findViewById(R.id.outdayw);
        outday = (TextView)findViewById(R.id.outday);
        outmonth = (TextView)findViewById(R.id.outmonth);
        outyear = (TextView)findViewById(R.id.outyear);

        try {
            setInit();
        } catch (Exception e) {
            error(0);//display error message
        }

        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        HijriGregorianConv.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH),
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

    }

    private void setInit() throws Exception{
        Day now = new Day();
        inday.setText(Integer.valueOf(now.getDayOfMonth()).toString());
        inmonth.setText(Integer.valueOf(now.getMonthNo()).toString());
        inyear.setText(Integer.valueOf(now.getYear()).toString());
        int i=getDate();
        if(i==1){
            ok();
        }else{
            if (Locale.getDefault().getLanguage().equals("ar"))
                Aerror(i);
            else
                error(i);
        }
    }

    public void convert(View view) throws NumberFormatException, Exception{
        int i=getDate();
        if(i==1){
            ok();
        }else{
            if (Locale.getDefault().getLanguage().equals("ar"))
                Aerror(i);
            else
                error(i);
        }
    }

    public int getDate() throws Exception{
        String sy = inyear.getText().toString();
        String sm = inmonth.getText().toString();
        String sd = inday.getText().toString();
        if(sy.length()==0 || sm.length()==0 || sd.length()==0){
            return -8;
        }
        try{
            y_ = Integer.valueOf(sy);
            m_ = Integer.valueOf(sm);
            d_ = Integer.valueOf(sd);
        } catch(NumberFormatException e){
            return -9;
        }
        if(tohijri.isChecked()){
            int i=HijriCalendar.getHijriDate( y_, m_ , d_ );
            if(i==1){
                y = HijriCalendar.getYear();
                m = HijriCalendar.getMonth();
                d = HijriCalendar.getDay();
                outday.setText(Integer.valueOf(d).toString());
                outmonth.setText(HijriCalendar.getMonthS());
                outyear.setText(Integer.valueOf(y).toString());
                outdayw.setText(GreCalendar.getDayW(y_,m_,d_));

            }else{
                return i;
            }
        }else if(togregorian.isChecked()){
            int i=GreCalendar.getGreDate(y_, m_, d_);
            if(i==1){
                y = GreCalendar.getYear();
                m = GreCalendar.getMonth();
                d = GreCalendar.getDay();
                outday.setText(Integer.valueOf(d).toString());
                outmonth.setText(GreCalendar.getMonthS());
                outyear.setText(Integer.valueOf(y).toString());
                outdayw.setText(GreCalendar.getDayW(y,m,d));
            }else{
                return i;
            }
        }

        txtHijri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtHijri.setTextColor(getResources().getColor(R.color.white));
                txtHijri.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtGregorian.setTextColor(getResources().getColor(R.color.dullblack));
                txtGregorian.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                int i= 0;
                try {
                    i = HijriCalendar.getHijriDate( y_, m_ , d_ );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(i==1){
                    y = HijriCalendar.getYear();
                    m = HijriCalendar.getMonth();
                    d = HijriCalendar.getDay();
                    outday.setText(Integer.valueOf(d).toString());
                    outmonth.setText(HijriCalendar.getMonthS());
                    outyear.setText(Integer.valueOf(y).toString());
                    outdayw.setText(GreCalendar.getDayW(y_,m_,d_));

                }/*else{
                    return i;
                }*/
            }
        });

        txtGregorian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtGregorian.setTextColor(getResources().getColor(R.color.white));
                txtGregorian.setBackgroundResource(R.drawable.btn_bg_signin_solid_square);
                txtHijri.setTextColor(getResources().getColor(R.color.dullblack));
                txtHijri.setBackgroundResource(R.drawable.btn_bg_signin_stroke_square);
                int i=GreCalendar.getGreDate(y_, m_, d_);
                if(i==1){
                    y = GreCalendar.getYear();
                    m = GreCalendar.getMonth();
                    d = GreCalendar.getDay();
                    outday.setText(Integer.valueOf(d).toString());
                    outmonth.setText(GreCalendar.getMonthS());
                    outyear.setText(Integer.valueOf(y).toString());
                    outdayw.setText(GreCalendar.getDayW(y,m,d));
                }/*else{
                    return i;
                }*/
            }
        });

        return 1;
    }

    private void error(int i) {
        String error = "";
        switch(i){
            case -1:
                error="Year Is Out Of Range (623 to 9999)";
                inyear.setTextColor(Color.RED);
                break;
            case -2:
                error="Date Is Out Of Range. (MAX. Gregorian Date = December 31, 9999)";
                inmonth.setTextColor(Color.RED);
                break;
            case -3:
                error="Date Is Out Of Range. (MAX. Gregorian Date = December 31, 9999)";
                inday.setTextColor(Color.RED);
                break;
            case -4:
                error="Date Is Out Of Range. (MIN. Gregorian Date = July 7, 623)";
                inmonth.setTextColor(Color.RED);
                break;
            case -5:
                error="Date Is Out Of Range. (MIN. Gregorian Date = July 7, 623)";
                inday.setTextColor(Color.RED);
                break;
            case -6:
                error="Error in Month (1 to 12)";
                inmonth.setTextColor(Color.RED);
                break;
            case -7:
                error="Error in Day (1 to 30/31 depends on Gregorian month)";
                inday.setTextColor(Color.RED);
                break;
            case -8:error="Please Enter Date"; break;
            case -9:error="Non Numeric Input Data"; break;
            case -10:
                error="Year Is Out Of Range (2 to 9666)";
                inyear.setTextColor(Color.RED);
                break;
            case -20:
                error="Date Is Out Of Range (MAX. Hijri Date = Rabi I 3, 9666)";
                inmonth.setTextColor(Color.RED);
                break;
            case -30:
                error="Date Is Out Of Range (MAX. Hijri Date = Rabi I 3, 9666)";
                inday.setTextColor(Color.RED);
                break;
            case -40:
                error="Date Is Out Of Range (MIN. Hijri Date = Muharram 1, 2)";
                inmonth.setTextColor(Color.RED);
                break;
            case -50:
                error="Date Is Out Of Range. (MIN. Hijri Date = Muharram 1, 2)";
                inday.setTextColor(Color.RED);
                break;
            case -60:
                error="Month Is Out Of Range (1 to 12)";
                inmonth.setTextColor(Color.RED);
                break;
            case -70:
                error="Day Is Out Of Range (1 to 29/30 depends on Hijri month)";
                inday.setTextColor(Color.RED);
                break;
            default:error="Error";
        }

        outdayw.setText("");
        outday.setText("");
        outmonth.setText("");
        outyear.setText("");
        Toast.makeText(HijriGregorianConv.this, error, Toast.LENGTH_LONG).show();
    }
    private void Aerror(int i) {
        String error = "";
        switch(i){
            case -1:
                error="623-9999 السنة الميلادية خارج النطاق";
                inyear.setTextColor(Color.RED);
                break;
            case -2:
                error="التاريخ الميلادي خارج النطاق أقصى تاريخ هو 31 ديسمبر 9999";
                inmonth.setTextColor(Color.RED);
                break;
            case -3:
                error="التاريخ الميلادي خارج النطاق أقصى تاريخ هو 31 ديسمبر 9999";
                inday.setTextColor(Color.RED);
                break;
            case -4:
                error="التاريخ الميلادي خارج النطاق أدنى تاريخ هو 7 يوليو 623";
                inmonth.setTextColor(Color.RED);
                break;
            case -5:
                error="التاريخ الميلادي خارج النطاق أقصى تاريخ هو 7 يوليو 623";
                inday.setTextColor(Color.RED);
                break;
            case -6:
                error="خطأ في الشهر (من 1 إلى 12)ء";
                inmonth.setTextColor(Color.RED);
                break;
            case -7:
                error="خطأ في اليوم (من 1 إلى 30 أو 31 حسب الشهر الميلادي)ء";
                inday.setTextColor(Color.RED);
                break;
            case -8:error="أدخل التاريخ إذا سمحت"; break;
            case -9:error="البيانات المدخلة خاطئة"; break;
            case -10:
                error="2-9666 السنة الهجرية خارج النطاق";
                inyear.setTextColor(Color.RED);
                break;
            case -20:
                error="التاريخ الهجري خارج النطاق أقصى تاريخ هو 3 ربيع الأول 9666";
                inmonth.setTextColor(Color.RED);
                break;
            case -30:
                error="التاريخ الهجري خارج النطاق أقصى تاريخ هو 3 ربيع الأول 9666";
                inday.setTextColor(Color.RED);
                break;
            case -40:
                error="التاريخ الهجري خارج النطاق أقل تاريخ هو 1 محرم 2";
                inmonth.setTextColor(Color.RED);
                break;
            case -50:
                error="التاريخ الهجري خارج النطاق أقل تاريخ هو 1 محرم 2";
                inday.setTextColor(Color.RED);
                break;
            case -60:
                error="خطأ في الشهر (1 إلى 12)ء";
                inmonth.setTextColor(Color.RED);
                break;
            case -70:
                error="خطأ في اليوم (من 1 إلى 30 أو 29 حسب الشهر الهجري)ء";
                inday.setTextColor(Color.RED);
                break;
            default:error="خطأ";
        }

        outdayw.setText("");
        outday.setText("");
        outmonth.setText("");
        outyear.setText("");
        Toast.makeText(HijriGregorianConv.this, error, Toast.LENGTH_LONG).show();
    }
    private void ok(){
        inday.setTextColor(getResources().getColor(R.color.white));
        inmonth.setTextColor(getResources().getColor(R.color.white));
        inyear.setTextColor(getResources().getColor(R.color.white));
    }
    /**
     *
     * @param view
     * @throws Exception
     */
    public void today(View view) throws Exception{
        tohijri.setChecked(true);
        try {
            setInit();
        } catch (Exception e) {
        }
    }

    public void clear(View view) throws Exception{
        ok();
        inday.setText("");
        inmonth.setText("");
        inyear.setText("");

        outdayw.setText("");
        outday.setText("");
        outmonth.setText("");
        outyear.setText("");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, int second) {
        inday.setText(String.valueOf(dayOfMonth));
        inmonth.setText(String.valueOf((monthOfYear+1)));
        inyear.setText(String.valueOf(year));

        y_ = year;
        m_ = monthOfYear+1;
        d_ = dayOfMonth;
    }
}