package com.example.astroweathercz2;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Astronomy implements Serializable {
    private AstroCalculator astroCalculator;
    private AstroDateTime astroDateTime;

    public void setAstroCalculator(Double latitude, Double longitude) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) +1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        //  timezoneOffset - Time zone offset (in hours)
        int timezoneOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
        // daylightSaving - Flag indicating if daylight saving is applicable or not
        // ... jpdl
        boolean daylightSaving = Calendar.getInstance().getTimeZone().inDaylightTime(new Date());
        astroDateTime = new AstroDateTime(year, month, day, hour, minute, second, timezoneOffset, daylightSaving);

        // AstroCalculator.Location(double latitude, double longitude);
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(latitude, longitude);

        astroCalculator = new AstroCalculator(astroDateTime, astroLocation);
    }


    public ArrayList<String> getSunInfo() {
//        Map<String, String> mapSunInfo = new TreeMap<>();
        ArrayList<String> arrayList = new ArrayList<>();

        AstroCalculator.SunInfo sunAstroInfo = astroCalculator.getSunInfo();
//        Function returns azimuth of sunrise for specific date and location
        String azimuthRise = String.valueOf(sunAstroInfo.getAzimuthRise());
//        Function returns azimuth of sunset for specific date and location
        String azimuthSet = String.valueOf(sunAstroInfo.getAzimuthSet());
//        Function returns time of sunrise for specific date and location
        String sunRise = sunAstroInfo.getSunrise().toString();
//        Function returns time of sunset for specific date and location
        String sunSet = sunAstroInfo.getSunset().toString();
//        Function returns time of civil morning twilight for specific date and location
        String twilightMorning = sunAstroInfo.getTwilightMorning().toString();
//        Function returns time of civil evening twilight for specific date and location
        String twilightEvening = sunAstroInfo.getTwilightEvening().toString();

        arrayList.add("Wschód słońca: " + sunRise);
        arrayList.add("Azymut: "+ azimuthRise);
        arrayList.add("Zachód słońca: " + sunSet);
        arrayList.add("Azymut: " + azimuthSet);
        arrayList.add("Civil morning twilight: " +  twilightMorning);
        arrayList.add("Civil evening twilight: " + twilightEvening);

        return arrayList;
    }

    public ArrayList<String> getMoonInfo() {
        /*Wchód i zachód (czas).
        • Najbliższy nów i pełnia (data).
        • Faza księżyca (w procentach).
        • Dzień miesiąca synodycznego.*/

        ArrayList<String> arrayList = new ArrayList<>();
        AstroCalculator.MoonInfo moonAstroInfo = astroCalculator.getMoonInfo();
//        Function returns moon age
//        String moonAge = String.valueOf(moonAstroInfo.getAge());

//        Function returns time of moonrise for specific date and location
        String moonRise = moonAstroInfo.getMoonrise().toString();
        //        Function returns time of moonset for specific date and location
        String moonSet = moonAstroInfo.getMoonset().toString();
        //        Function returns date and time of next new moon phase
        // we need only date
        int yearNextMoon = moonAstroInfo.getNextNewMoon().getYear();
        int monthNextMoon = moonAstroInfo.getNextNewMoon().getMonth();
        int dayNextMoon = moonAstroInfo.getNextNewMoon().getDay();
        String nextNewMoon = String.format("%s-%s-%s", yearNextMoon, monthNextMoon, dayNextMoon);
        //        Function returns date and time of next full moon phase
        // we need only date
        int yearFullMoon = moonAstroInfo.getNextFullMoon().getYear();
        int monthFullMoon = moonAstroInfo.getNextFullMoon().getMonth();
        int dayFullMoon = moonAstroInfo.getNextFullMoon().getDay();
        String fullMoon = String.format("%s-%s-%s", yearFullMoon, monthFullMoon, dayFullMoon);
        //        Function returns moon illumination
        // %.4 -> returns 2 char fractional part filling with 0
        // multiply by 100%
        String moonIllumination = String.format(Locale.getDefault(), "%.2f", moonAstroInfo.getIllumination() * 100);
//        Dzień miesiąca synodycznego.???????
//        Miesiąc synodyczny[1], lunacja[2] – średni czas pomiędzy kolejnymi nowiami[a] Księżyca

//        int yearNewNextMoon = moonAstroInfo.getNextNewMoon().getYear();
//        int monthNewNextMoon = moonAstroInfo.getNextNewMoon().getMonth();
//        int dayNewNextMoon = moonAstroInfo.getNextNewMoon().getDay();

        int nowYear = astroDateTime.getYear();
        int nowMonth = astroDateTime.getMonth();
        int nowDay = astroDateTime.getDay();
        String nextNewestNewMoon = String.format("%s-%s-%s", nowYear, nowMonth, nowDay);

        long timeDifferenceMilliseconds = 0;
        try {
            Date oldDate = new SimpleDateFormat("yyyy-mm-dd").parse(nextNewMoon);
            Date newDate = new SimpleDateFormat("yyyy-mm-dd").parse(nextNewestNewMoon);
            long oldTime = oldDate.getTime();
            long newTime = newDate.getTime();
            timeDifferenceMilliseconds = Math.abs(newTime - oldTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        long synodicMonthDay = synodicMonthDayCalculator(nextNewMoon/*, yearNextMoon, monthNextMoon, dayNextMoon*/);
        long synodicMonthDay = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);

        arrayList.add("Wschód księżyca: " + moonRise);
        arrayList.add("Zachód księżyca: " + moonSet);
        arrayList.add("Najbliższy nów: " + nextNewMoon);
        arrayList.add("Najbliższa pełnia: " + fullMoon);
        arrayList.add("Faza księżyca: " + moonIllumination);

        arrayList.add("Dzień miesiąca synodycznego: " + synodicMonthDay);

        return arrayList;
    }

    private long synodicMonthDayCalculator(String nextNewMoon) {

        AstroCalculator.MoonInfo moonAstroInfo;
        moonAstroInfo = astroCalculator.getMoonInfo();
        int yearNewNextMoon = moonAstroInfo.getNextNewMoon().getYear();
        int monthNewNextMoon = moonAstroInfo.getNextNewMoon().getMonth();
        int dayNewNextMoon = moonAstroInfo.getNextNewMoon().getDay();
        String nextNewestNewMoon = String.format("%s-%s-%s", yearNewNextMoon, monthNewNextMoon, dayNewNextMoon);
        long timeDifferenceMilliseconds = 0;
        try {
            Date oldDate = new SimpleDateFormat("yyyy-mm-dd").parse(nextNewMoon);
            Date newDate = new SimpleDateFormat("yyyy-mm-dd").parse(nextNewestNewMoon);
            long oldTime = oldDate.getTime();
            long newTime = newDate.getTime();
            timeDifferenceMilliseconds = Math.abs(newTime - oldTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        https://stackoverflow.com/questions/4673527/converting-milliseconds-to-a-date-jquery-javascript
//        https://stackoverflow.com/questions/5351483/calculate-date-time-difference-in-java
//        int synodicMonthDay = new Date(diff).getDay();
        long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
//        long synodicMonthDay = diffDays;

        return diffDays;
    }

}