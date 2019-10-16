package com.geekbrains.city_weather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.geekbrains.city_weather.frag.WeatherFragment;
import com.geekbrains.city_weather.preferences.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "33333";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    finish();
                    return true;
                case R.id.navigation_about:
                    AboutDialog aboutDialog = new AboutDialog();
                    aboutDialog.show(getSupportFragmentManager(),
                            getResources().getString(R.string.dialog));
                    return true;
                case R.id.navigation_settings:
                    Log.d(TAG, "onNavigationItemSelected");
                    Intent intentSettings = new Intent(DetailActivity.this,
                            SettingsActivity.class);
                    startActivity(intentSettings);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //получаем текущую позицию города в списке городов из интента
        int currentPosition = Objects.requireNonNull(getIntent()
                .getExtras()).getInt(P.CURRENT_POS);
        ArrayList<String> cityMarked = getIntent()
                .getStringArrayListExtra(P.CITY_MARKED);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //после изменения строки списка и переворота экрана надо передать актуальную позицию
            //делать это буду через активность вызовом метода фрагмента
            Log.d(TAG, "DetailActivity onCreate currentPosition = " + currentPosition);
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.putExtra(P.CURRENT_POSITION_DETAIL, currentPosition);
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
            WeatherFragment details = WeatherFragment.newInstance(currentPosition);
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details, P.WEATHER_FRAFMENT_TAG)
                    .commit();
        }
    }
}
