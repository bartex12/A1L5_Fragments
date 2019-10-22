package com.geekbrains.city_whether.frag;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhetherFragment extends Fragment {
    private static final String TAG = "33333";
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
    }

    private void initViews(View view) {

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
}