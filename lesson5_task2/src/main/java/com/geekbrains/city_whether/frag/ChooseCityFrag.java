package com.geekbrains.city_whether.frag;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import com.geekbrains.city_whether.DetailActivity;
import com.geekbrains.city_whether.R;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseCityFrag extends Fragment {
    private static final String TAG = "33333";
    private Button buttonShow;
    private CheckBox checkBoxWind;
    private CheckBox checkBoxPressure;
    private Spinner spinnerTowns;
    private String city = "";

    public static final String CITY = "CITY";
    public static final String WIND = "WIND";
    public static final String PRESSURE = "PRESSURE";
    public static final String CURRENT_POS = "CURRENT_POS";

    private boolean isExistWhetherFrag;  // Можно ли расположить рядом фрагмент с погодой
    private int currentPosition = 0;    // Текущая позиция (выбранный город)

    private boolean isWind;
    private boolean isPressure;

    public ChooseCityFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить рядом данные в другом фрагменте
        isExistWhetherFrag = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
            Log.d(TAG, "savedInstanceState != null  currentPosition "+ currentPosition);
        }

        // Если можно нарисовать рядом данные, то сделаем это
        if (isExistWhetherFrag) {
            Log.d(TAG, "onActivityCreated  isExistWhetherFrag "+ isExistWhetherFrag);
            showCityWhether();
        }
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view) {

        buttonShow =  view.findViewById(R.id.buttonShow);
        checkBoxWind = view.findViewById(R.id.checkBoxWind);
        checkBoxWind.setChecked(true);
        checkBoxWind.setEnabled(false);
        checkBoxPressure = view.findViewById(R.id.checkBoxPressure);
        checkBoxPressure.setChecked(true);
        checkBoxPressure.setEnabled(false);

        String[] towns = getResources().getStringArray(R.array.towns); //получаем массив городов из ресурсов
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.spinner_item, towns); //ставим адаптер со своим лейаутом
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTowns = view.findViewById(R.id.spinnerTowns);
        spinnerTowns.setAdapter(adapterSpinner); //подключанм адаптер к списку

        spinnerTowns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                //так можно получить город через адаптер
                String str = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinnerTowns onItemSelected Город " + str +
                        " currentPosition = " + currentPosition );
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //а так можно получить город через спиннер
                city =  spinnerTowns.getSelectedItem().toString();
                isWind = checkBoxWind.isChecked();
                isPressure = checkBoxPressure.isChecked();

                //если альбомная ориентация,то
                if (isExistWhetherFrag){
                    Log.d(TAG, "buttonShow onClick isExistWhetherFrag = " + isExistWhetherFrag);
                    showCityWhether();
                    //а если портретная, то
                }else {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(CURRENT_POS, currentPosition);
                    intent.putExtra(CITY, city);
                    intent.putExtra(WIND, isWind);
                    intent.putExtra(PRESSURE, isPressure);
                    startActivity(intent);
                }
            }
        });
    }

    // Показать погоду. Ecли возможно, то показать рядом со спиннером,
    // если нет, то открыть вторую activity
    private void showCityWhether(){

        Log.d(TAG, "showCityWhether  isExistWhetherFrag =  " + isExistWhetherFrag);
            // Проверим, что фрагмент с погодой существует в activity - обращение по id фрагмента
            WhetherFragment whetherFrag = (WhetherFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.whether_in_citys);

            //для отладки
            if (whetherFrag != null){
                Log.d(TAG, "whetherFrag.getIndex= " + whetherFrag.getIndex() +
                        "  currentPosition = " + currentPosition);
            }

            // Если есть необходимость, то выведем погоду
            if (whetherFrag == null || whetherFrag.getIndex() != currentPosition) {
                // Создаем новый фрагмент с текущей позицией для вывода погоды
                whetherFrag = WhetherFragment.newInstance(currentPosition);

                // Выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.whether_in_citys, whetherFrag);  // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// эффект
                //ft.addToBackStack(null);
                ft.addToBackStack("Some_Key"); //добавление, чтобы получать по кнопке "назад"
                ft.commit();
            }
    }

    //получаем актуальное значение currentPosition при перевороте экрана в DetailActivity
    //хотя в погодном приложении работает и без этого метода - обновление же по кнопке
    public void getCurrentPosition(int actualPosition){
        currentPosition = actualPosition;
        spinnerTowns.setSelection(currentPosition);
        Log.d(TAG, "ChooseCityFrag getCurrentPosition actualPosition = " + currentPosition);
    }
}
