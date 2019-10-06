package ru.geekbrains.a1l5_lifeCicles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "33333";
    private TextView firstRunTextView;
    static final String INPUT = "input";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstRunTextView = findViewById(R.id.textViewInfo);
        if (savedInstanceState == null){
            firstRunTextView.setText("Первый запуск!");
        }
        else{
            firstRunTextView.setText("Повторный запуск!");
        }
        makeMessage("MainActivity onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeMessage("MainActivity onStart()");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        firstRunTextView.setText("Повторный запуск!!");
        makeMessage("MainActivity onRestoreInstanceState()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeMessage("MainActivity onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeMessage("MainActivity onPause()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        makeMessage("MainActivity onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeMessage("MainActivity onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeMessage("MainActivity onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeMessage("MainActivity onDestroy()");
    }

    public void onClick(View v) {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;

        switch (v.getId()) {
            case R.id.buttonAdd:
                //Вставляем фрагмент
                fm = getSupportFragmentManager();
                fragment = fm.findFragmentById(R.id.conteiner);
                if (fragment == null) {
                    fragment = BlankFragment.newInstance("Новый фрагмент");
                    fm.beginTransaction()
                            .add(R.id.conteiner, fragment)
                            .commit();
                }
                break;
            case R.id.buttonRemove:
                //Удаляем фрагмент
                fragment = fm.findFragmentById(R.id.conteiner);
                if (fragment != null) {
                    fm.beginTransaction()
                            .remove(fragment)
                            .commit();
                }
                break;
            default:
                break;
        }
    }

    private void makeMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, message);
    }
}
