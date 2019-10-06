package com.geekbrains.a1l5_fragments.fragments;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.geekbrains.a1l5_fragments.R;

import java.util.Objects;

public class CoatOfArmsFragment extends Fragment {

    //создание фрагмента с аргументами
    public static CoatOfArmsFragment newInstance(int index) {
        CoatOfArmsFragment fragment = new CoatOfArmsFragment();    // создание
        // Передача параметра
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    // Получить индекс из списка (фактически из параметра)
    int getIndex() {
        return Objects.requireNonNull(getArguments()).getInt("index", 0);
    }

    @Override
    @SuppressLint("Recycle")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Определить какой герб надо показать, и показать его
        ImageView coatOfArms = new ImageView(getActivity());

        // Получить из ресурсов массив указателей на изображения гербов
         TypedArray images = getResources().obtainTypedArray(R.array.coatofarms_imgs);
        // Выбрать по индексу подходящий
        coatOfArms.setImageResource(images.getResourceId(getIndex(), -1));
        return coatOfArms;     // Вместо макета используем сразу картинку
    }
}
