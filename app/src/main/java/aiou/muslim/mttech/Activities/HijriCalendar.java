package aiou.muslim.mttech.Activities;
import java.util.Locale;

public class HijriCalendar {
    static final int DAYS_IN_30HIJRI_YEAR_CYCLE = 10631;
    static final int ABS_DAYS_FIRST_HIJRI_YEAR = 227013;
    static final int COMMON_HIJRI_YEAR_DAYS = 354;
    private static final int MAX_HIJRI_YEAR = 9666;
    private static final int MAX_HIJRI_MONTH = 4;
    private static final int MAX_HIJRI_DAY = 3;

    private static final int MIN_HIJRI_YEAR = 2;
    private static final int MIN_HIJRI_MONTH = 1;
    private static final int MIN_HIJRI_DAY = 1;
    private static int Year = 1;
    private static int Month = 1;
    private static int Day = 1;
    private static int[] HijriMonthDays = 	{0, 30, 59, 89, 118, 148, 177, 207, 236, 266, 295, 325, 354};
    private static String[] HijriMonths = {"Muharram", "Safar", "Rabi I", "Rabi II", "Jumada I", "Jumada II", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhu al-Qada", "Dhu al-Hijjah"};
    private static String[] AHijriMonths = {"محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
            "رمضان", "شوال", "ذو القعدة", "ذو الحجة"};

    /**
     * @param y year
     * @param m month
     * @param d day
     * @return false if there is error in entered date
     * @throws Exception
     */
    public static int getHijriDate(int y, int m, int d) throws Exception{
        int i=GreCalendar.CheckYearMonthDayRangeG(y,m,d);
        if(i==1){
            long absDays = GreCalendar.getAbsGreDate(y,m,d);
            GetHijri(absDays);
            return 1;
        }else{
            //System.out.println("Out Of Range");
            return i;
        }
    }
    /**
     *
     * @param absDays
     */
    private static void GetHijri(long absDays){
        long absHijriDays = absDays - ABS_DAYS_FIRST_HIJRI_YEAR;
        int yearHijri = (int)(((absHijriDays)/DAYS_IN_30HIJRI_YEAR_CYCLE)*30 +1);

        int numOf30Cycle = (int)((absDays-ABS_DAYS_FIRST_HIJRI_YEAR)/DAYS_IN_30HIJRI_YEAR_CYCLE);


        long LeftHijriDays = (int)(absHijriDays - numOf30Cycle*DAYS_IN_30HIJRI_YEAR_CYCLE);

        long daysOfHijriYear = GetDaysInHijriYear(yearHijri);
        while(LeftHijriDays>daysOfHijriYear){
            LeftHijriDays -= daysOfHijriYear;
            yearHijri++;
            daysOfHijriYear = GetDaysInHijriYear(yearHijri);
        }
        Year = yearHijri;
        //////////////////calculate month
        int monthHijri = 1;
        for(monthHijri=1;monthHijri < 12 && LeftHijriDays>GetDaysInHijriMonth(yearHijri,monthHijri);monthHijri++){
            LeftHijriDays -= GetDaysInHijriMonth(yearHijri,monthHijri);
        }
        Month = monthHijri;
        if(LeftHijriDays <= 0){
            Day = 1;
        }else Day = (int)LeftHijriDays;
    }
    private static int GetDaysInHijriMonth(int year, int month) {
        switch(month){
            case 1:return 30;
            case 2:return 29;
            case 3:return 30;
            case 4:return 29;
            case 5:return 30;
            case 6:return 29;
            case 7:return 30;
            case 8:return 29;
            case 9:return 30;
            case 10:return 29;
            case 11:return 30;
            case 12:
                if(IsLeapYearH(year))return 30;
                else return 29;
            default: return -1;
        }
    }
    private static long GetDaysInHijriYear(int year) {
        return COMMON_HIJRI_YEAR_DAYS + (IsLeapYearH(year) ? 1:0);
    }
    private static boolean IsLeapYearH(int year) {
        return ((((year*11)+14)%30)<11);
    }
    /**
     * @param y
     * @param m
     * @param d
     * @return absolute hijri date: number of days from given hijri year to 1/1/0001 A.D.
     */
    public static long getAbsHijriDate(int y, int m, int d){
        return (long)(DayUpToHijriYear(y) + HijriMonthDays(m) + d);
    }
    private static long HijriMonthDays(int m) {
        return HijriMonthDays[m-1];
    }
    private static long DayUpToHijriYear(int y){
        long NumDays;
        int NumYear30;//30 cycle in current year
        int NumYearLeft;

        //compute how many 30 year cycle in current year
        // because NumYear30 is int; any fraction will be truncated
        //-1 to remove the current year (not complete year)
        NumYear30 = (y-1)/30;
        // -1 to remove the current year (not complete year)
        NumYearLeft = y - NumYear30*30 - 1;
        // compute the number of absolute days up to current year
        NumDays = NumYear30*10631L +227013L;
        //long GetHijriDaysInYear()
        while(NumYearLeft>0){
            //common year is 354, leap year 355 days.
            NumDays +=354 +(IsLeapYearH(NumYearLeft) ? 1:0);
            NumYearLeft--;
        }
        return NumDays;
    }


    /**
     *
     * @param year
     * @param month
     * @param day
     * @return false year or month or day is out of range
     * -1 Year is out of range 2 TO 9666
     * -2 Month is out of range MAX Hijri DATE is 3/4/96666
     * -3 Day is out of range MAX Hijri DATE is 3/4/96666
     * -4 Month is out of range MIN Hijri DATE is 1/1/2
     * -5 Day is out of range MIN Hijri DATE is 1/1/2
     * -6 Month is wrong. 1 to 12
     * -7 Day of month is wrong. 1 to 30 or 29 (depends on month)
     */
    public static int CheckYearMonthDayRangeH(int year, int month, int day){
        if(year < MIN_HIJRI_YEAR || year > MAX_HIJRI_YEAR){
            return -10;
        } else if(year == MAX_HIJRI_YEAR){
            if(month > MAX_HIJRI_MONTH){
                return -20;
            }else if(month == MAX_HIJRI_MONTH){
                if(day > MAX_HIJRI_DAY){
                    return -30;
                }
            }
        }else if(year == MIN_HIJRI_YEAR){
            if(month < MIN_HIJRI_MONTH){
                return -40;
            }else if(month == MIN_HIJRI_MONTH){
                if(day < MIN_HIJRI_DAY){
                    return -50;
                }
            }
        }
        if(month < 1 || month > 12){
            return -60;
        }
        if(day < 1 || day > GetDaysInHijriMonth(year,month)){
            return -70;
        }
        return 1;
    }
    /**
     * @return day
     */
    public static int getDay() {
        return Day;
    }
    /**
     * @return month
     */
    public static int getMonth() {
        return Month;
    }
    /**
     * @return year
     */
    public static int getYear() {
        return Year;
    }
    /**
     *
     * @return the name of hijri month
     */
    public static String getMonthS(){
        if(Locale.getDefault().getLanguage().equals("ar"))
            return AHijriMonths[Month - 1];
        else
            return HijriMonths[Month - 1];
    }
}
