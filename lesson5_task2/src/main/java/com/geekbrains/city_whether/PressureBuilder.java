package com.geekbrains.city_whether;

import android.content.Context;

import java.util.Random;

class PressureBuilder {

    String getPressure(Context context){
        Random random = new Random();
        int rndPressure = 750 + random.nextInt(20);
        String press = context.getString(R.string.press);
        String mm = context.getString(R.string.mm);
        return press + " " + rndPressure + " " + mm;
    }
}
