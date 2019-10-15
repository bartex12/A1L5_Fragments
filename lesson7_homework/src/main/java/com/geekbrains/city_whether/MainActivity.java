package com.geekbrains.city_whether;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.geekbrains.city_whether.frag.ChooseCityFrag;
import com.geekbrains.city_whether.preferences.SettingsActivity;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "33333";
    private SharedPreferences prefSetting;
    boolean isShowCheckboxes;

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

        //устанавливаем значения по умолчанию при первой загрузке
        androidx.preference.PreferenceManager
                .setDefaultValues(this, R.xml.pref_setting, false);
    }
    // переопределение метода onBackPressed() пришлось убрать, иначе при нажатии кнопки "назад"
    //переход по фрагментам идёт через 2 позиции!!!


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
        setCheckboxesInFrsgment(isShowCheckboxes);
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
        switch (id){
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

    //Создать и открыть диалог выхода из программы
    private void openQuitDialog() {
        final AlertDialog.Builder bilder = new AlertDialog.Builder(this);
        bilder.setTitle(getResources().getString(R.string.aboutApp));
        bilder.setIcon(R.drawable.sun);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.bottom_dialog, null);
        final Button buttonDialog = view.findViewById(R.id.buttonDialog);
        buttonDialog.setVisibility(View.GONE);
        bilder.setView(view);

        bilder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        bilder.show();
    }

    // показываем/скрываем чекбоксы на экране выбора города
    private void setCheckboxesInFrsgment(boolean isShowCheckboxes) {
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

