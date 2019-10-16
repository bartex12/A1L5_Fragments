package com.geekbrains.city_weather.adapter;

import android.graphics.drawable.Drawable;

public class DataForecast {
    String day;
    Drawable whetherDrawble;
    String temp;
    public DataForecast(String day, Drawable whether, String temp) {
        this.day = day;
        this.whetherDrawble = whether;
        this.temp = temp;
    }
}
