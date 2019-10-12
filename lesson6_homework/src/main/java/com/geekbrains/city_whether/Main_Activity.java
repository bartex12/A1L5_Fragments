package com.geekbrains.city_whether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.geekbrains.city_whether.frag.ChooseCityFrag;

import java.util.ArrayList;
import java.util.Objects;

public class Main_Activity extends AppCompatActivity {

    private static final String TAG = "33333";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            int position = getIntent().getIntExtra("index",0);
            ArrayList<String> cityMarked = getIntent().getStringArrayListExtra(P.CITY_MARKED);
            //ghb первой загрузке cityMarked=nullБ поэтому страхуемся
            if (cityMarked==null){
                cityMarked = new ArrayList<>();
            }
            //находим фрагмент
            ChooseCityFrag chooseCityFrag = (ChooseCityFrag)getSupportFragmentManager().
                    findFragmentById(R.id.citiesWhether);
            //вызываем из активности метод фрагмента для передачи актуальной позиции
            Objects.requireNonNull(chooseCityFrag).getCurrentPositionAndList(position, cityMarked);

            Log.d(TAG, "MainActivity onCreate position = " + position +
                " cityMarked = " + cityMarked);
    }
    // переопределение метода onBackPressed() пришлось убрать, иначе при нажатии кнопки "назад"
    //переход по фрагментам идёт через 2 позиции!!!
    }

