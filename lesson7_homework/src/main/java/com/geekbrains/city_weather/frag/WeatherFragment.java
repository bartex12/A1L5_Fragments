package com.geekbrains.city_weather.frag;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geekbrains.city_weather.R;
import com.geekbrains.city_weather.TempBuilder;
import com.geekbrains.city_weather.adapter.DataForecast;
import com.geekbrains.city_weather.adapter.WeatherCardAdapter;
import com.geekbrains.city_weather.data_loader.CityWeatherDataLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
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
    private TextView cityTextView;
    private TextView textViewLastUpdate;
    private TextView textViewWhether;
    private TextView textViewTemper;
    private TextView textViewWind;
    private TextView textViewPressure;
    private TextView textViewIcon;
    private Handler handler = new Handler();

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
        updateWeatherData(getCity());
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "WeatherFragment onResume");

        SharedPreferences prefSetting = androidx.preference.PreferenceManager
                .getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
        //получаем из файла настроек количество знаков после запятой
        boolean isShowCheckboxes = prefSetting.getBoolean("showCheckBoxes", true);
        Log.d(TAG, "WeatherFragment initViews isShowCheckboxes = " + isShowCheckboxes);

        // показываем/скрываем данные о ветре и давлении
        showWindAndPressure(isShowCheckboxes);
    }

    private void initViews(View view) {
        recyclerViewForecast = view.findViewById(R.id.recyclerViewForecast);
        cityTextView = view.findViewById(R.id.greetingsTextView);
        textViewLastUpdate = view.findViewById(R.id.textViewLastUpdate);
        textViewWhether = view.findViewById(R.id.textViewWhether);
        textViewTemper = view.findViewById(R.id.textViewTemper);
        textViewWind = view.findViewById(R.id.textViewWind);
        textViewPressure = view.findViewById(R.id.textViewPressure);
        textViewIcon = view.findViewById(R.id.textViewIcon);
    }

    private void initFonts() {
        Typeface weatherFont = Typeface.createFromAsset(Objects.
                requireNonNull(getActivity()).getAssets(), "fonts/weather.ttf");
        textViewIcon.setTypeface(weatherFont);
    }

    //получаем погодные данные с сервера  в JSON формате
    private void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = CityWeatherDataLoader.getJSONData(city);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), R.string.place_not_found,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        Log.d(TAG, "json: " + jsonObject.toString());
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");

            Log.e(TAG, "details = " + details + " main = " + main);
            setPlaceName(jsonObject);
            setUpdatedText(jsonObject);
            setDescription(details);
            setWind(wind);
            setPressure(main);
            setCurrentTemp(main);

            setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(TAG, "One or more fields not found in the JSON data");
        }
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
        WeatherCardAdapter cardAdapter = new WeatherCardAdapter(list);

        recyclerViewForecast.setLayoutManager(layoutManager);
        recyclerViewForecast.setAdapter(cardAdapter);
    }

    // показываем/скрываем данные о ветре и давлении
    private void showWindAndPressure(boolean isShowCheckboxes) {
        if (isShowCheckboxes) {
            textViewWind.setVisibility(View.VISIBLE);
            textViewPressure.setVisibility(View.VISIBLE);
        } else {
            textViewWind.setVisibility(View.GONE);
            textViewPressure.setVisibility(View.GONE);
        }
    }

    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        //выводим строки в текстовых полях
        cityTextView.setText(cityText);
    }

    private void setUpdatedText(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        String updatedText = "Last update: " + updateOn;
        textViewLastUpdate.setText(updatedText);
    }

    private void setDescription(JSONObject details) throws JSONException {
        String descriptionText = details.getString("description").toUpperCase();
        textViewWhether.setText(descriptionText);
    }

    private void setWind(JSONObject jsonObject) throws JSONException {
        String wind = jsonObject.getString("speed");
        String windSpeed = Objects.requireNonNull(getActivity()).getString(R.string.windSpeed);
        String ms = getActivity().getString(R.string.ms);
        String windText = windSpeed + " " + wind + " " + ms;
        textViewWind.setText(windText);
    }

    private void setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format(Locale.getDefault(), "%.1f",
                main.getDouble("temp")) + "\u2103";
        textViewTemper.setText(currentTextText);
    }

    private void setPressure(JSONObject main) throws JSONException {
        String pressure = main.getString("pressure");
        String press = Objects.requireNonNull(getActivity()).getString(R.string.press);
        String hPa = getActivity().getString(R.string.hPa);
        String pressureText = press + " " + pressure + " " + hPa;
        textViewPressure.setText(pressureText);
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                //icon = "\u2600";
                icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    //icon = "\u2601";
                    icon = getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textViewIcon.setVisibility(View.GONE);
        } else {
            textViewIcon.setText(icon);
        }
    }

}
//1 hPa = 0.75006375541921 mmHg