package com.geekbrains.city_weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.geekbrains.city_weather.dialogs.DialogCityAdd;
import com.geekbrains.city_weather.dialogs.DialogCityChange;
import com.geekbrains.city_weather.frag.ChooseCityFrag;
import com.geekbrains.city_weather.preferences.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import static com.geekbrains.city_weather.AppConstants.CITY_MARKED;
import static com.geekbrains.city_weather.AppConstants.CURRENT_CITY;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "33333";
    boolean isShowCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //передаём фрагменту из интента название города и список ранее выбранных городов
        initFragWithExtra();
        initFab();
        //устанавливаем из настроек значения по умолчанию для первой загрузки
        androidx.preference.PreferenceManager
                .setDefaultValues(this, R.xml.pref_setting, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"MainActivity onResume");
        //получаем настройки из активности настроек
        SharedPreferences prefSetting = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        //получаем из файла настроек состояние чекбокса
        isShowCheckboxes = prefSetting.getBoolean("showCheckBoxes", true);
        Log.d(TAG,"MainActivity onResume isShowCheckboxes = " + isShowCheckboxes);
        // показываем/скрываем чекбоксы на экране выбора города
        setCheckboxesInFragment(isShowCheckboxes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "ListOfSmetasNames onOptionsItemSelected id = " + id);
        switch (id) {
            case R.id.navigation_choose_city:
                showChangecityDialogFragment();
                return true;

            case R.id.navigation_add:
                DialogFragment dialogFragment = new DialogCityAdd();
                dialogFragment.show(getSupportFragmentManager(), "addCity");
                return true;

            case R.id.navigation_about:
                //TODO
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
                return true;

            case R.id.navigation_settings:
                Log.d(TAG, "OptionsItem = navigation_settings");
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangecityDialogFragment();
            }
        });
    }

    private void initFragWithExtra() {
        String currentCity = getIntent().getStringExtra(CURRENT_CITY);
        ArrayList<String> cityMarked = getIntent().getStringArrayListExtra(CITY_MARKED);
        //при первой загрузке currentCity=null, поэтому страхуемся
        if (currentCity == null) {
            currentCity = "Saint Petersburg";
        }
        //при первой загрузке cityMarked=null, поэтому страхуемся
        if (cityMarked == null) {
            cityMarked = new ArrayList<>();
        }
        //находим фрагмент
        ChooseCityFrag chooseCityFrag = (ChooseCityFrag) getSupportFragmentManager().
                findFragmentById(R.id.citiesWhether);
        //вызываем из активности метод фрагмента для передачи актуальной позиции и списка городов
        Objects.requireNonNull(chooseCityFrag).getCurrentPositionAndList(currentCity, cityMarked);

        Log.d(TAG, "MainActivity onCreate currentCity = " + currentCity +
                " cityMarked = " + cityMarked);
    }

    //диалог сохранения, оформленный как класс с указанием имени файла
    private void showChangecityDialogFragment() {
        DialogFragment dialogFragment = new DialogCityChange();
        dialogFragment.show(getSupportFragmentManager(), "changeCity");
    }

    // показываем/скрываем чекбоксы на экране выбора города
    private void setCheckboxesInFragment(boolean isShowCheckboxes) {
        ChooseCityFrag fr = (ChooseCityFrag) getSupportFragmentManager().
                findFragmentById(R.id.citiesWhether);
        View view = Objects.requireNonNull(fr).getView();
        CheckBox checkBoxWind = Objects.requireNonNull(view).findViewById(R.id.checkBoxWind);
        CheckBox checkBoxPressure = Objects.requireNonNull(view).findViewById(R.id.checkBoxPressure);
        if (isShowCheckboxes) {
            checkBoxWind.setVisibility(View.VISIBLE);
            checkBoxPressure.setVisibility(View.VISIBLE);
        } else {
            checkBoxWind.setVisibility(View.GONE);
            checkBoxPressure.setVisibility(View.GONE);
        }
    }
}

