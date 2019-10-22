package com.geekbrains.a1l5_fragments;

import android.os.Bundle;
import android.util.Log;
import com.geekbrains.a1l5_fragments.fragments.CitiesFragment;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static  final String TAG = "33333";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras()!=null){
            int position = getIntent().getIntExtra("index",0);
            Log.d(TAG, "MainActivity onCreate position = " + position);
            CitiesFragment citiesFragment = (CitiesFragment)getSupportFragmentManager().
                    findFragmentById(R.id.cities);
            //вызываем метод фрагмента для передачи актуальной позиции
            Objects.requireNonNull(citiesFragment).getCurrentPosition(position);
        }
    }
    // переопределение метода onBackPressed() пришлось убрать, иначе при нажатии кнопки "назад"
    //переход по фрагментам идёт через 2 позиции!!!
}
