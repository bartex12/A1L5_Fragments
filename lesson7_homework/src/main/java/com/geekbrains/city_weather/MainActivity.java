package com.geekbrains.city_weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.geekbrains.city_weather.frag.ChooseCityFrag;
import com.geekbrains.city_weather.preferences.SettingsActivity;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "33333";
    private SharedPreferences prefSetting;
    boolean isShowCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //передаём фрагменту из интента позицию и список ранее выбранных городов
        initFragWithExtra();
        //устанавливаем из настроек значения по умолчанию для первой загрузки
        androidx.preference.PreferenceManager
                .setDefaultValues(this, R.xml.pref_setting, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"MainActivity onResume");

        //получаем настройки из активности настроек
        prefSetting = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        //получаем из файла настроек состояние чекбокса
        isShowCheckboxes = prefSetting.getBoolean("showCheckBoxes", true);
        Log.d(TAG,"MainActivity onResume isShowCheckboxes = " + isShowCheckboxes);

        // показываем/скрываем чекбоксы на экране выбора города
        setCheckboxesInFragment(isShowCheckboxes);
    }

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
        switch (id) {
            case R.id.navigation_about:
                openQuitDialog();
                return true;

            case R.id.navigation_settings:
                Log.d(TAG, "OptionsItem = navigation_settings");
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragWithExtra() {
        int position = getIntent().getIntExtra(P.CURRENT_POSITION_DETAIL, 0);
        ArrayList<String> cityMarked = getIntent().getStringArrayListExtra(P.CITY_MARKED);
        //при первой загрузке cityMarked=nullБ поэтому страхуемся
        if (cityMarked == null) {
            cityMarked = new ArrayList<>();
        }
        //находим фрагмент
        ChooseCityFrag chooseCityFrag = (ChooseCityFrag) getSupportFragmentManager().
                findFragmentById(R.id.citiesWhether);
        //вызываем из активности метод фрагмента для передачи актуальной позиции и списка городов
        Objects.requireNonNull(chooseCityFrag).getCurrentPositionAndList(position, cityMarked);

        Log.d(TAG, "MainActivity onCreate position = " + position +
                " cityMarked = " + cityMarked);
    }


    //Создать и открыть диалог выхода из программы
    private void openQuitDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_city);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ChooseCityFrag fr = (ChooseCityFrag) getSupportFragmentManager().
                        findFragmentById(R.id.citiesWhether);
                fr.updateWeatherData(input.getText().toString());

            }
        });
        builder.show();
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

