package aiou.muslim.mttech.Activities;
import java.util.Locale;

public class GreCalendar {
    static final int COMMON_GREGORIAN_YEAR_DAYS = 365;
    static final int DAYS_IN_400GREGORIAN_YEAR_CYCLE = 146097;
    private static final int MAX_GRE_YEAR = 9999;
    private static final int MAX_GRE_MONTH = 12;
    private static final int MAX_GRE_DAY = 31;
    private static final int MIN_GRE_YEAR = 623;
    private static final int MIN_GRE_MONTH = 7;
    private static final int MIN_GRE_DAY = 7;
    private static int Year;
    private static int Month;
    private static int Day;
    private static int[] GreMonthDays = 	{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
    private static String[] GreMonths = {"January", "February", "March", "April", "May", "June", "July", "August",
            "Septemper", "October", "November", "December"};
    private static String[] AGreMonths = {"يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس",
            "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};

    public static int getGreDate(int y, int m, int d){
        int i = HijriCalendar.CheckYearMonthDayRangeH(y,m,d);
        if(i==1){
            long absDays = HijriCalendar.getAbsHijriDate(y,m,d);
            GetGregorain(absDays);
            return 1;
        } else {
            return i;
        }

    }

    private static void GetGregorain(long absDays) {
        int year400 = (int)(absDays/DAYS_IN_400GREGORIAN_YEAR_CYCLE);
        int yearGre = (int)(year400)*400 + 1; // no of 400 year
        int LeftGreDays = (int)(absDays - year400*DAYS_IN_400GREGORIAN_YEAR_CYCLE);
        long daysOfGreYear = GetDaysInGreYear(yearGre);
        while(LeftGreDays > daysOfGreYear){
            yearGre++;
            LeftGreDays -= daysOfGreYear;
            daysOfGreYear = GetDaysInGreYear(yearGre);
        }
        Year = yearGre;

        int monthGre = 1;
        for(monthGre=1;monthGre < 12 && LeftGreDays>GetDaysInGreMonth(yearGre,monthGre);monthGre++){
            LeftGreDays -= GetDaysInGreMonth(yearGre,monthGre);
        }
        Month = monthGre;
        ///////////////////////////////////////////////////////
        if(LeftGreDays <= 0){
            //this should not happen at all
            Day = 1;
        }else Day = LeftGreDays;
    }
    private static int GetDaysInGreMonth(int year, int month) {
        switch(month){
            case 1:return 31;
            case 2:
                return (IsLeapYearG(year)? 29:28);
            case 3:return 31;
            case 4:return 30;
            case 5:return 31;
            case 6:return 30;
            case 7:return 31;
            case 8:return 31;
            case 9:return 30;
            case 10:return 31;
            case 11:return 30;
            case 12:return 31;
            default: return -1;
        }
    }
    private static long GetDaysInGreYear(int y) {
        return 365 + (IsLeapYearG(y) ? 1:0);
    }
    /**
     * @param y
     * @param m
     * @param d
     * @return long
     */
    public static long getAbsGreDate(int y, int m, int d){
        //////absolute days until last year (y-1)
        int daysYear,daysMonth,absDays;
        daysYear = (y-1)*COMMON_GREGORIAN_YEAR_DAYS + NumOfLeapYear(y - 1) ;
        //////
        //days in current year
        daysMonth = GreMonthDays[m-1];
        absDays = daysYear + daysMonth + d;
        if(m>2){
            absDays += (IsLeapYearG(y)? 1:0);
        }
        return absDays;
    }
    private static int NumOfLeapYear(int year) {
        int numOfLeapDays = 0;
        for(int i = year; i>3;i--){
            numOfLeapDays += (IsLeapYearG(i)? 1:0);
        }
        return numOfLeapDays;
    }
    private static boolean IsLeapYearG(int year) {
        if(year%4 == 0){
            if(year%100==0){
                if(year%400 == 0){
                    return true;
                }else return false;
            }else return true;
        }else return false;
    }
    /**
     * Check in the given gregorian date is true
     * @param year
     * @param month
     * @param day
     * @return false if year or month or day is out of range
     * @throws Exception
     * -1 Year is out of range 623 TO 9999
     * -2 Month is out of range MAX Gre. DATE is 31/12/9999
     * -3 Day is out of range MAX Gre. DATE is 31/12/9999
     * -4 Month is out of range MIN Gre. DATE is 7/7/623
     * -5 Day is out of range MIN Gre. DATE is 7/7/623
     * -6 Month is wrong. 1 to 12
     * -7 Day of month is wrong. 1 to 30 or 31 (depends on month)
     *
     */
    public static int CheckYearMonthDayRangeG(int year, int month, int day) throws Exception{
        if(year < MIN_GRE_YEAR || year > MAX_GRE_YEAR){
            return -1;
        }else if(year == MAX_GRE_YEAR){
            if(month > MAX_GRE_MONTH){
                return -2;
            }else if(month == MAX_GRE_MONTH){
                if(day > MAX_GRE_DAY){
                    return -3;
                }
            }
        }else if(year == MIN_GRE_YEAR){
            if(month < MIN_GRE_MONTH){
                return -4;
            }else if(month == MIN_GRE_MONTH){
                if(day < MIN_GRE_DAY){
                    return -5;
                }
            }
        }
        if(month < 1 || month > 12){
            return -6;
        }
        if(day < 1 || day > GetDaysInGreMonth(year,month)){
            return -7;
        }
        return 1;
    }

    /**
     * @return Gregorian day of month
     */
    public static int getDay() {
        return Day;
    }
    /**
     * @return Gregorian month
     */
    public static int getMonth() {
        return Month;
    }
    /**
     * @return Gregorian year
     */
    public static int getYear() {
        return Year;
    }
    /**
     *
     * @return Gregorian month
     */
    public static String getMonthS(){
        if (Locale.getDefault().getLanguage().equals("ar"))
            return AGreMonths[Month - 1];
        else
            return GreMonths[Month - 1];
    }
    /**
     *
     * @param year
     * @param month
     * @param day
     * @return the name of day of week
     */
    public static String getDayW(int year,int month, int day){
        Day d = new Day(year,month-1,day);
        if (Locale.getDefault().getLanguage().equals("ar"))
            return d.AgetDayName();
        else
            return d.getDayName();

    }

}