package com.geekbrains.city_whether;

import android.content.Context;
import java.util.Random;

public class WindBuilder {
    public String getWindSpeed(Context context){
        Random random = new Random();
        int rndWindSpeed = 1 + random.nextInt(14);
        String windSpeed = context.getString(R.string.windSpeed);
        String ms = context.getString(R.string.ms);
        return windSpeed + " " + rndWindSpeed + " " + ms;
    }
}
