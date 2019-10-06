package com.geekbrains.city_whether;

import android.content.Context;
import android.util.Log;
import java.util.Calendar;
import java.util.GregorianCalendar;

class GreetingsBuilder {

    private static final String TAG = "33333";

    String getGreetings(Context context) {
         Calendar calendar = new GregorianCalendar();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);  // час через календарь теперь так
        Log.d(TAG, "currentHour = " + currentHour);

        String result;
        String timeInfo = " ( "+ context.getString(R.string.on) + " " + currentHour + ":00 )";
        // час сейчас
        if (5 <= currentHour && currentHour < 12 ) {      // Если утро
            result = context.getString(R.string.weather_morning);
        } else if (12 <= currentHour && currentHour < 18) {  // Если день
            result = context.getString(R.string.weather_afternoon);
        } else if (18 <= currentHour && currentHour < 21) {    // Если вечер
            result = context.getString(R.string.weather_evening);
        } else {
            result = context.getString(R.string.weather_night);   // Если поздний вечер или ночь
        }
        return result + timeInfo;
    }
}
