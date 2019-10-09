package com.geekbrains.city_whether.frag;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private RecyclerView recyclerView;
    private WhetherCardAdapter cardAdapter;

    private TextView greetingsTextView;
    private TextView textViewWhether;
    private TextView textViewTemper;
    private TextView textViewWind;
    private TextView textViewPressure;
    private ImageView imageViewWhether;

    boolean isWind;
    boolean isPressure;

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

        recyclerView = view.findViewById(R.id.recyclerView);
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

        Drawable drawable = new PictureBuilder().getDrawableIcon(getActivity(), textWhether);
        imageViewWhether.setImageDrawable(drawable);

        String textTemper = new TempBuilder().getTemperature(getActivity());
        textViewTemper.setText(textTemper);

        String wind = new WindBuilder().getWindSpeed(getActivity());
        textViewWind.setText(wind);

        String press = new PressureBuilder().getPressure(getActivity());
        textViewPressure.setText(press);

    }

    private void  initRecyclerView(){
        DataForecast[] data = new DataForecast[] {
                new DataForecast("Завтра",ContextCompat
                        .getDrawable(Objects.requireNonNull(getActivity()), R.drawable.sun),
                        "+15"),
                new DataForecast("Послезавтра",ContextCompat
                        .getDrawable(Objects.requireNonNull(getActivity()), R.drawable.rain),
                        "+20"),
                new DataForecast("Через 2 дня",ContextCompat.
                        getDrawable(Objects.requireNonNull(getActivity()), R.drawable.partly_cloudy),
                        "+17"),
                new DataForecast("Через 3 дня",ContextCompat.
                        getDrawable(Objects.requireNonNull(getActivity()), R.drawable.sun),
                        "+18"),
                new DataForecast("Через 4 дня",ContextCompat.
                        getDrawable(Objects.requireNonNull(getActivity()), R.drawable.boom),
                        "+14")};

        ArrayList<DataForecast> list = new ArrayList<>(data.length);
        list.addAll(Arrays.asList(data));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cardAdapter = new WhetherCardAdapter(list);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardAdapter);
    }
}