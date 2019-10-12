package com.geekbrains.city_whether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.geekbrains.city_whether.frag.ChooseCityFrag;

import java.util.ArrayList;
import java.util.Objects;

public class Main_Activity extends AppCompatActivity {

    private static final String TAG = "33333";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            int position = getIntent().getIntExtra(P.CURRENT_POSITION_DETAIL,0);
            ArrayList<String> cityMarked = getIntent().getStringArrayListExtra(P.CITY_MARKED);
            //при первой загрузке cityMarked=nullБ поэтому страхуемся
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.navigation_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.d(TAG, "ListOfSmetasNames onOptionsItemSelected id = " + id);
        switch (id){
            case R.id.navigation_about:

                return true;

            case R.id.navigation_settings:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    }

