package com.geekbrains.city_whether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.geekbrains.city_whether.frag.ChooseCityFrag;
import com.geekbrains.city_whether.frag.WhetherFragment;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "33333";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //получаем текущую позицию города в списке городов из интента
        int currentPosition = Objects.requireNonNull(getIntent()
                .getExtras()).getInt(P.CURRENT_POS);
        ArrayList<String> cityMarked = getIntent()
                .getStringArrayListExtra(P.CITY_MARKED);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //после изменения строки списка и переворота экрана надо передать актуальную позицию
            //делать это буду через активность вызовом метода фрагмента
            Log.d(TAG, "DetailActivity onCreate currentPosition = " + currentPosition);
            Intent intent = new Intent(DetailActivity.this, Main_Activity.class);
            intent.putExtra("currentPosition", currentPosition);
            intent.putExtra(P.CITY_MARKED, cityMarked);
            startActivity(intent);
            // Если устройство перевернули в альбомную ориентацию,
            // то надо эту activity закрыть и убрать из стэка
            finish();
            return;
        }

        // Если эта activity запускается первый раз (с каждым новым городом первый раз)
        // то перенаправим параметр фрагменту
        Log.d(TAG, "DetailActivity  savedInstanceState = "+ savedInstanceState);
        if (savedInstanceState == null) {
            //создаём фрагмент, передавая индекс в аргументы фрагмента
            WhetherFragment details = WhetherFragment.newInstance(currentPosition);
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        }
    }
}
