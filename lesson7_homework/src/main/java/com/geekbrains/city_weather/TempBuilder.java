package com.geekbrains.city_weather;

import android.content.Context;

import java.util.Random;

public class TempBuilder {
    public String getTemperature(Context context){
        Random random = new Random();
        String temper = context.getString(R.string.temper);
        String grad = context.getString(R.string.grad);
        int rnd =30 - random.nextInt(25);
        return temper + " " + rnd + " " + grad;

    }
}
