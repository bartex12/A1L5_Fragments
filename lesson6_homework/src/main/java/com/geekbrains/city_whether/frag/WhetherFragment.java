package com.geekbrains.city_whether.frag;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.geekbrains.city_whether.GreetingsBuilder;
import com.geekbrains.city_whether.PictureBuilder;
import com.geekbrains.city_whether.PressureBuilder;
import com.geekbrains.city_whether.R;
import com.geekbrains.city_whether.TempBuilder;
import com.geekbrains.city_whether.WhetherBuilder;
import com.geekbrains.city_whether.WindBuilder;
import com.geekbrains.city_whether.adapter.DataForecast;
import com.geekbrains.city_whether.adapter.WhetherCardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhetherFragment extends Fragment {
    private static final String TAG = "33333";

    private RecyclerView recyclerViewForecast;
    private WhetherCardAdapter cardAdapter;

    private TextView greetingsTextView;
    private TextView textViewWhether;
    private TextView textViewTemper;
    private TextView textViewWind;
    private TextView textViewPressure;
    private ImageView imageViewWhether;

    private SharedPreferences prefSetting;
    private boolean isShowCheckboxes;

    public WhetherFragment() {
        // Required empty public constructor
    }

    public static WhetherFragment newInstance(int position) {
        WhetherFragment fragment = new WhetherFragment();
        // Передача параметра
        Bundle args = new Bundle();
        args.putInt("index", position);
        fragment.setArguments(args);
        return fragment;
    }

    // Получить индекс из списка (фактически из параметра)
    int getIndex() {

        return Objects.requireNonNull(getArguments()).getInt("index", 0);
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
        initRecyclerView();
    }

    private void initViews(View view) {

        recyclerViewForecast = view.findViewById(R.id.recyclerViewForecast);
        greetingsTextView = view.findViewById(R.id.greetingsTextView);
        textViewWhether = view.findViewById(R.id.textViewWhether);
        textViewTemper = view.findViewById(R.id.textViewTemper);
        textViewWind = view.findViewById(R.id.textViewWind);
        textViewPressure = view.findViewById(R.id.textViewPressure);
        imageViewWhether = view.findViewById(R.id.imageViewWhether);

        String[] towns = getResources().getStringArray(R.array.towns);
        String town = towns[getIndex()];

        //формируем строку в зависимости от времени суток
        String text =town + ": " + new GreetingsBuilder().getGreetings(Objects.requireNonNull(getActivity()));
        //выводим строки в текстовых полях
        greetingsTextView.setText(text);

        String textWhether = new WhetherBuilder().getWhether(getActivity());
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

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"WhetherFragment onResume");
        prefSetting = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //получаем из файла настроек количество знаков после запятой
        isShowCheckboxes = prefSetting.getBoolean("showCheckBoxes", true);
        Log.d(TAG,"WhetherFragment initViews isShowCheckboxes = " + isShowCheckboxes);

        if (isShowCheckboxes){
            textViewWind.setVisibility(View.VISIBLE);
            textViewPressure.setVisibility(View.VISIBLE);
        }else {
            textViewWind.setVisibility(View.INVISIBLE);
            textViewPressure.setVisibility(View.INVISIBLE);
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
        cardAdapter = new WhetherCardAdapter(list);

        recyclerViewForecast.setLayoutManager(layoutManager);
        recyclerViewForecast.setAdapter(cardAdapter);
    }
}