package com.geekbrains.city_weather;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class PictureBuilder {

    public Drawable getDrawableIcon(Context context, String srt){
        Drawable drawable = null;
        String sun =  context.getResources().getString(R.string.Sun);
        String var_clouds =  context.getResources().getString(R.string.var_clouds);
        String little_rain =  context.getResources().getString(R.string.little_rain);
        String rain =  context.getResources().getString(R.string.rain);
        String boom =  context.getResources().getString(R.string.boom);
        if (srt.equals(sun)){
            drawable = ContextCompat.getDrawable(context, R.drawable.sun);
        }else if (srt.equals(var_clouds)){
            drawable = ContextCompat.getDrawable(context, R.drawable.partly_cloudy);
        }else if (srt.equals(little_rain)){
            drawable = ContextCompat.getDrawable(context, R.drawable.little_rain);
        }else if (srt.equals(rain)){
            drawable = ContextCompat.getDrawable(context, R.drawable.rain);
        }else if (srt.equals(boom)){
            drawable = ContextCompat.getDrawable(context, R.drawable.boom);
        }
        return drawable;
    }
}
