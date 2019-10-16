package com.geekbrains.city_weather.frag;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekbrains.city_weather.GreetingsBuilder;
import com.geekbrains.city_weather.PictureBuilder;
import com.geekbrains.city_weather.PressureBuilder;
import com.geekbrains.city_weather.R;
import com.geekbrains.city_weather.TempBuilder;
import com.geekbrains.city_weather.WeatherBuilder;
import com.geekbrains.city_weather.WindBuilder;
import com.geekbrains.city_weather.adapter.DataForecast;
import com.geekbrains.city_weather.adapter.WeatherCardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private static final String TAG = "33333";

    private RecyclerView recyclerViewForecast;
    private WeatherCardAdapter cardAdapter;
    Typeface weatherFont;

    private TextView greetingsTextView;
    private TextView textViewWhether;
    private TextView textViewTemper;
    private TextView textViewWind;
    private TextView textViewPressure;
    private TextView textViewIcon;
    private ImageView imageViewWhether;

    private SharedPreferences prefSetting;
    private boolean isShowCheckboxes;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String city) {
        WeatherFragment fragment = new WeatherFragment();
        // Передача параметра
        Bundle args = new Bundle();
        args.putString("city", city);
        fragment.setArguments(args);
        return fragment;
    }

    // Получить город из аргументов
    String getCity() {
        return Objects.requireNonNull(getArguments()).getString("city", "Moscow");
    }

    @Override
    @SuppressLint("Recycle")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_whether, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initFonts();
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "WeatherFragment onResume");

        prefSetting = androidx.preference.PreferenceManager
                .getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
        //получаем из файла настроек количество знаков после запятой
        isShowCheckboxes = prefSetting.getBoolean("showCheckBoxes", true);
        Log.d(TAG, "WeatherFragment initViews isShowCheckboxes = " + isShowCheckboxes);

        // показываем/скрываем данные о ветре и давлении
        showWindAndPressure(isShowCheckboxes);
    }

    private void initViews(View view) {

        recyclerViewForecast = view.findViewById(R.id.recyclerViewForecast);
        greetingsTextView = view.findViewById(R.id.greetingsTextView);
        textViewWhether = view.findViewById(R.id.textViewWhether);
        textViewTemper = view.findViewById(R.id.textViewTemper);
        textViewWind = view.findViewById(R.id.textViewWind);
        textViewPressure = view.findViewById(R.id.textViewPressure);
        textViewIcon = view.findViewById(R.id.textViewIcon);
        imageViewWhether = view.findViewById(R.id.imageViewWhether);

        String town = getCity();

        //формируем строку в зависимости от времени суток
        String text =town + ": " + new GreetingsBuilder().getGreetings(Objects.requireNonNull(getActivity()));
        //выводим строки в текстовых полях
        greetingsTextView.setText(text);

        String textWhether = new WeatherBuilder().getWhether(getActivity());
        textViewWhether.setText(textWhether);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageViewWhether.setVisibility(View.GONE);
        }else {
            Drawable drawable = new PictureBuilder().getDrawableIcon(getActivity(), textWhether);
            imageViewWhether.setImageDrawable(drawable);
        }

        String textTemper = new TempBuilder().getTemperature(getActivity());
        textViewTemper.setText(textTemper);

        String wind = new WindBuilder().getWindSpeed(getActivity());
        textViewWind.setText(wind);

        String press = new PressureBuilder().getPressure(getActivity());
        textViewPressure.setText(press);

        textViewIcon.setText("Здесь был Вася");

    }

    private void initFonts() {
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        textViewIcon.setTypeface(weatherFont);
    }

    private void  initRecyclerView(){
        DataForecast[] data = new DataForecast[] {
                new DataForecast(getString(R.string.tomorrow),ContextCompat
                        .getDrawable(Objects.requireNonNull(getActivity()), R.drawable.sun),
                        new TempBuilder().getTemperature(getActivity())),
                new DataForecast(getString(R.string.oneDay),ContextCompat
                        .getDrawable(Objects.requireNonNull(getActivity()), R.drawable.rain),
                        new TempBuilder().getTemperature(getActivity())),
                new DataForecast(getString(R.string.after2days),ContextCompat.
                        getDrawable(Objects.requireNonNull(getActivity()), R.drawable.partly_cloudy),
                        new TempBuilder().getTemperature(getActivity())),
                new DataForecast(getString(R.string.after3days),ContextCompat.
                        getDrawable(Objects.requireNonNull(getActivity()), R.drawable.sun),
                        new TempBuilder().getTemperature(getActivity())),
                new DataForecast(getString(R.string.after4days),ContextCompat.
                        getDrawable(Objects.requireNonNull(getActivity()), R.drawable.boom),
                        new TempBuilder().getTemperature(getActivity()))};

        ArrayList<DataForecast> list = new ArrayList<>(data.length);
        list.addAll(Arrays.asList(data));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        cardAdapter = new WeatherCardAdapter(list);

        recyclerViewForecast.setLayoutManager(layoutManager);
        recyclerViewForecast.setAdapter(cardAdapter);
    }

    // показываем/скрываем данные о ветре и давлении
    private void showWindAndPressure(boolean isShowCheckboxes) {
        if (isShowCheckboxes) {
            textViewWind.setVisibility(View.VISIBLE);
            textViewPressure.setVisibility(View.VISIBLE);
        } else {
            textViewWind.setVisibility(View.INVISIBLE);
            textViewPressure.setVisibility(View.INVISIBLE);
        }
    }
}