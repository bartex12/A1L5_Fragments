package com.geekbrains.city_weather.frag;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.geekbrains.city_weather.DetailActivity;
import com.geekbrains.city_weather.P;
import com.geekbrains.city_weather.R;
import com.geekbrains.city_weather.cityAdapter.RecyclerViewCityAdapter;
import com.geekbrains.city_weather.data_loader.CityWeatherDataLoader;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private boolean isExistWhetherFrag;  // Можно ли расположить рядом фрагмент с погодой
    private int currentPosition = 0;    // Текущая позиция (выбранный город)

    private boolean isWind;
    private boolean isPressure;

    private RecyclerView recyclerViewMarked; //RecyclerView для списка ранее выбранных городов
    private ArrayList<String> cityMarked = new ArrayList<>(); //список ранее выбранных городов
    private RecyclerViewCityAdapter recyclerViewCityAdapter; //адаптер для RecyclerView

    Handler handler = new Handler();

    public ChooseCityFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_choose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initRecycledView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить рядом данные в другом фрагменте
        isExistWhetherFrag = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции города в списке спиннера
            currentPosition = savedInstanceState.getInt(P.CURRENT_CITY, 0);
            Log.d(TAG, "onViewCreated savedInstanceState   currentPosition "+ currentPosition);
            cityMarked = savedInstanceState.getStringArrayList(P.CURRENT_CITY_MARKED);
            Log.d(TAG, "onViewCreated savedInstanceState cityMarked.size()= "+
                    Objects.requireNonNull(cityMarked).size());

            //adapter.notifyDataSetChanged() не работает, придётся так
            this.initRecycledView();
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
        outState.putInt(P.CURRENT_CITY, currentPosition);
        outState.putStringArrayList(P.CURRENT_CITY_MARKED, cityMarked);
        Log.d(TAG, "ChooseCityFrag savedInstanceState cityMarked.size()= "+ cityMarked.size());
        super.onSaveInstanceState(outState);
    }

    private void initViews(View view) {
        recyclerViewMarked = view.findViewById(R.id.recycledViewMarked);
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
                //проверяем есть ли город в списке cityMarked
                if (!isCityInList(city)){
                    //если нет, добавляем его
                    cityMarked.add(city); //добавляем город в список ранее выбранных городов
                }
                Log.d(TAG, "cityMarked.add(city) cityMarked.size() = " + cityMarked.size());
                recyclerViewCityAdapter.notifyDataSetChanged(); // - перерисует сразу весь список
                isWind = checkBoxWind.isChecked();
                isPressure = checkBoxPressure.isChecked();

                // показываем погоду в городе с учётом ориентации экрана
                showCityWhetherWithOrientation();
            }
        });
    }

    private boolean isCityInList(String city) {
        for (int i = 0; i < cityMarked.size(); i++){
            if (cityMarked.get(i).equals(city)){
                return true;
            }
        }
        return false;
    }

    // показываем погоду в городе с учётом ориентации экрана
    private void showCityWhetherWithOrientation() {
        //если альбомная ориентация,то
        if (isExistWhetherFrag) {
            Log.d(TAG, "buttonShow onClick isExistWhetherFrag = " + isExistWhetherFrag);
            showCityWhether();
            //а если портретная, то
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(P.CURRENT_POS, currentPosition);
            intent.putExtra(P.CITY_MARKED, cityMarked);
            startActivity(intent);
        }
    }

    // показываем погоду в городе с учётом ориентации экрана
    private void showCityWhetherWithOrientation2(String city) {
        //если альбомная ориентация,то
        if (isExistWhetherFrag) {
            Log.d(TAG, "buttonShow onClick isExistWhetherFrag = " + isExistWhetherFrag);
            showCityWhether();
            //а если портретная, то
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(P.INPUT_CITY, city);
            intent.putExtra(P.CURRENT_POS, currentPosition);
            intent.putExtra(P.CITY_MARKED, cityMarked);
            startActivity(intent);
        }
    }

    private void initRecycledView() {
        //используем встроенный LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //реализуем интерфейс адаптера, в  его методе onCityClick получим имя города и его позицию
        RecyclerViewCityAdapter.OnCityClickListener onCityClickListener =
                new RecyclerViewCityAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String city, int position) {
                //изменяем текущюю позицию
                currentPosition = position;
                // если альбомная ориентация, подтверждаем нажатие строки списка с городами
                if (isExistWhetherFrag) {
                    Snackbar.make(Objects.requireNonNull(getView()),city, Snackbar.LENGTH_SHORT).show();
                }
                // показываем погоду в городе с учётом ориентации экрана
                showCityWhetherWithOrientation();
            }
        };
        //передадим адаптеру в конструкторе список выбранныз городов и ссылку на интерфейс
        //в принципе, надо через adapter.setOnCityClickListener, но хочу попробовать так
        //понятно, что это  неуниверсально, так как адаптер теперь зависит от конкретного интерфейся
        recyclerViewCityAdapter = new RecyclerViewCityAdapter(cityMarked, onCityClickListener);

        recyclerViewMarked.setLayoutManager(layoutManager);
        recyclerViewMarked.setAdapter(recyclerViewCityAdapter);
    }

    // Показать погоду во фрагменте рядом со спиннером в альбомной ориентации
    private void showCityWhether(){

        Log.d(TAG, "showCityWhether  isExistWhetherFrag =  " + isExistWhetherFrag);
            // Проверим, что фрагмент с погодой существует в activity - обращение по id фрагмента
        WeatherFragment whetherFrag = (WeatherFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.whether_in_citys);

            //для отладки
            if (whetherFrag != null){
                Log.d(TAG, "whetherFrag.getIndex= " + whetherFrag.getIndex() +
                        "  currentPosition = " + currentPosition);
            }
            // Если фрагмент не создан или он не соответствует выбранному городу, то ...
            if (whetherFrag == null || whetherFrag.getIndex() != currentPosition) {
                // ... создаем новый фрагмент с текущей позицией для вывода погоды
                whetherFrag = WeatherFragment.newInstance(currentPosition);

                // ... и выполняем транзакцию по замене фрагмента
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.whether_in_citys, whetherFrag, P.WEATHER_FRAFMENT_TAG);  // замена фрагмента
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// эффект
                //ft.addToBackStack(null);
                ft.addToBackStack(P.SOME_KEY); //добавление, чтобы получать по кнопке "назад"
                ft.commit();
            }
    }

    //получаем актуальное значение currentPosition и cityMarked при перевороте экрана в DetailActivity
    //хотя в погодном приложении работает и без этого метода - обновление же по кнопке
    public void getCurrentPositionAndList(int actualPosition, ArrayList<String> cityMarked){
        currentPosition = actualPosition;
        spinnerTowns.setSelection(currentPosition);
        this.cityMarked = cityMarked;
        this.initRecycledView();
        try {
            Log.d(TAG, "ChooseCityFrag getCurrentPosition actualPosition = " + currentPosition +
                    " cityMarked.size() = " + cityMarked.size());
        }catch (NullPointerException e){
            e.getStackTrace();
        }
    }

    //получаем погодные данные с сервера  в JSON формате
    public void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = CityWeatherDataLoader.getJSONData(city);
                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), R.string.place_not_found,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        //находим фрагмент
        WeatherFragment weatherFrag = (WeatherFragment) getFragmentManager().
                findFragmentByTag(P.WEATHER_FRAFMENT_TAG);
        //вызываем из активности метод фрагмента для передачи актуальной позиции и списка городов
        // Objects.requireNonNull(chooseCityFrag).getCurrentPositionAndList(position, cityMarked);
        Toast.makeText(getActivity(), weatherFrag.toString(),
                Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), jsonObject.toString(),
//                Toast.LENGTH_LONG).show();
    }
}

