package ru.geekbrains.a1l5_lifeCicles;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private static final String TAG = "[BlankFragment]";
    private String inputString = "";

    public BlankFragment() {
        // Required empty public constructor
    }

     static BlankFragment newInstance(String str){
        Bundle args = new Bundle();
        args.putString(MainActivity.INPUT, str);
        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        makeMessage("BlankFragment: onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeMessage("BlankFragment: onCreate()");
        Log.d(TAG, "BlankFragment: onCreate()");
        inputString = Objects.requireNonNull(getArguments()).getString(MainActivity.INPUT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        makeMessage("BlankFragment: onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeMessage("BlankFragment: onViewCreated()");
        TextView info = view.findViewById(R.id.textViewFragmentInfo);
        info.setText(inputString);
    }

    @Override
    public void onStart() {
        super.onStart();
        makeMessage("BlankFragment: onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        makeMessage("BlankFragment: onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        makeMessage("BlankFragment: onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        makeMessage("BlankFragment: onStop()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        makeMessage("BlankFragment: onDetach()");
    }

    private void makeMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, message);
    }
}
