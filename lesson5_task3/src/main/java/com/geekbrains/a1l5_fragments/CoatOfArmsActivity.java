package com.geekbrains.a1l5_fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.geekbrains.a1l5_fragments.fragments.CoatOfArmsFragment;
import java.util.Objects;

public class CoatOfArmsActivity extends AppCompatActivity {

    private static  final String TAG = "33333";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coatofarms);

        //получаем индекс из интента
        int index = Objects.requireNonNull(getIntent().getExtras()).getInt("index");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //после изменения строки списка и переворота экрана надо передать актуальную позицию
            //делать это буду через активность вызовом метода фрагмента
            Log.d(TAG, "CoatOfArmsActivity onCreate index = " + index);
            Intent intent = new Intent(CoatOfArmsActivity.this, MainActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
            // Если устройство перевернули в альбомную ориентацию,
            // то надо эту activity закрыть и убрать из стэка
            finish();
            return;
        }

        // Если эта activity запускается первый раз (с каждым новым гербом первый раз)
        // то перенаправим параметр фрагменту
        if (savedInstanceState == null) {
            //создаём фрагмент, передавая индекс в аргументы фрагмента
            CoatOfArmsFragment details = CoatOfArmsFragment.newInstance(index);
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        }
    }
}
