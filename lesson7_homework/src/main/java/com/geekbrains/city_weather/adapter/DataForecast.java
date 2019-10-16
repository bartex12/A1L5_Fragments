package com.geekbrains.city_weather.adapter;

import android.graphics.drawable.Drawable;

public class DataForecast {
    String day;
    Drawable weatherDrawble;
    String temp;

    public DataForecast(String day, Drawable weather, String temp) {
        this.day = day;
        this.weatherDrawble = weather;
        this.temp = temp;
    }
}
